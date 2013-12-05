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
	var cd = new CountDown(configs.length, function() {
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
		checkFunc(e, cd);
	});
};
Task.prototype.filterConfig = function(hosts, configs) {
	if (hosts) {
		for (var i = 0; i < configs.length; i++) {
			for (var k = 0; k < hosts.length; k++) {
				if (hosts[k].id == configs[i].id) {
					configs[i].result = 0;
				}
			}
		}
	} else {
		for (var j = 0; j < configs.length; j++) {
			configs[j].result = 0;
		}
	}
};

Task.prototype.check = function(hosts, callBack) {
	var self = this;
	this.queryConfig(function(error, configs) {
		if (error) {
			callBack(error, self);
			return;
		} else {
			self.filterConfig(hosts, configs);
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

Task.prototype.deleteConfig = function(config, callBack) {
	var task = this;
	var client = global.zkCli;
	var path = task.configPath + "/" + config.id;
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
			tx.create(task.configPath + "/" + configs[i].id, new Buffer(JSON
					.stringify(configs[i])));
		}
		tx.commit(function(error, results) {
			if (error) {
				console.log(
						'Failed to execute the transaction: %s, results: %j',
						error, results);
			}
			callBack(error);
		});
	});
};
module.exports = Task;