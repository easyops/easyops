var ZNode = require("../../../lib/res/zk");
var CountDown = require("../../common/util.js").CountDown;
var Task = function() {
	var title = "";
	var description = "";
	var subTask = [];
	var curSubTask;
	var allWorkLoad = 100;
	var completeWorkLoad = 0;
	// show page uri
	var page;
	// run result: true or false;
	var result;
};
Task.prototype.process = function() {
	console.log("not implemented");
};
Task.prototype.finish = function() {
	console.log("not implemented");
};
Task.prototype.procheck = function() {
	console.log("not implemented");
};
Task.prototype.configPath = "";
Task.prototype.queryConfig = function(callBack) {
	var task = this;
	var client = global.zkCli;
	var configsNode = new ZNode(client, null, task.configPath);
	configsNode.fetchChildren(function(error, node, stat) {

		if (error) {
			callBack(error, null);
			return;
		}
		var configs = [];
		for (var i = 0; i < node.children.length; i++) {
			configs.push(node.children[i].data);
		}

		callBack(error, configs);
		return;
	});
};

Task.prototype.checkConfigs = function(configs, callBack, checkFunc) {
	var count = 0;
	for (var i = 0; i < configs.length; i++) {
		if (configs[i].result === 0) {
			count++;
		}
	}
	var cd = new CountDown(count, function() {
		var result = true;
		for (var i = 0; i < configs.length; i++) {
			if (configs[i].result === false) {
				result = false;
			}
		}
		callBack(result);
	});
	console.log("got %s configs from zk", configs.length);
	configs.forEach(function(e, i, a) {
		if (e.result === 0) {
			checkFunc(e, cd);
		}
	});
};
Task.prototype.filterConfig = function(selected, configs) {
	if (selected) {
		for (var i = 0; i < configs.length; i++) {
			for (var k = 0; k < selected.length; k++) {
				if (selected[k].id == configs[i].id) {
					configs[i].result = 0;
				} else {
					configs[i].result = 1;
				}
			}
		}
	} else {
		for (var j = 0; j < configs.length; j++) {
			configs[j].result = 0;
		}
	}
};

Task.prototype.check = function(selected, callBack) {
	var self = this;
	this.queryConfig(function(error, configs) {
		if (error) {
			callBack(error, self);
			return;
		} else {
			self.filterConfig(selected, configs);
			self.subTask = configs;
			self.checkConfigs(configs, function(result) {
				console.log("result:" + result);
				self.result = result;
				callBack(null, self);
			}, self.checkUnit);
		}
	});
};

Task.prototype.addConfig = function(config, callBack) {
	var task = this;
	var client = global.zkCli;
	var path = task.configPath + "/" + config.id;
	var bf = new Buffer(JSON.stringify(config));
	client.create(path, bf, function(error, path) {
		callBack(error);
	});
};

Task.prototype.updateConfig = function(config, callBack) {
	var task = this;
	var client = global.zkCli;
	var path = task.configPath + "/" + config.id;
	var bf = new Buffer(JSON.stringify(config));
	client.setData(path, bf, function(error, stat) {
		callBack(error, stat);
	});
};

Task.prototype.deleteConfig = function(id, callBack) {
	var task = this;
	var client = global.zkCli;
	var path = task.configPath + "/" + id;
	client.remove(path, function(error) {
		callBack(error);
	});
};

Task.prototype.saveConfig = function(configs, callBack) {
	var task = this;
	var client = global.zkCli;
	var configsNode = new ZNode(client, null, task.configPath);
	configsNode.listChildren(function(error, node, stat) {
		if (error) {
			callBack(error);
			return;
		}
		var tx = client.transaction();
		for (var i = 0; i < node.children.length; i++) {
			tx.remove(node.children[i].path);
		}
		for (i = 0; i < configs.length; i++) {
			tx
					.create(task.configPath + "/" + configs[i].id, new Buffer(JSON
							.stringify(configs[i])));
		}
		tx.commit(function(error, results) {
			if (error) {
				console.log('Failed to execute the transaction: %s, results: %j', error, results);
			}
			callBack(error);
		});
	});
};
module.exports = Task;