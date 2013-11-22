var ZNode = require("../../../lib/res/zk");

var Task = function(){
	var title = "";
	var description = "";
	var subTask = [];
	var curSubTask;
	var allWorkLoad = 100;
	var completeWorkLoad = 0;
	//function
	var precheck;
	//function
	var process;
	//function
	var finish;
	// show page uri
	var page;
	// run result: true or false;
	var result;
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