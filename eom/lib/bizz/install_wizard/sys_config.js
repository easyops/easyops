var ZNode = require("../../../lib/res/zk");

var sys_config_path = "/config/system/deployment/config";
exports.getSysConfig = function(callBack) {
	var client = global.zkCli;
	var node = new ZNode(client, null, sys_config_path);
	node.fetchData(function(error, n, s) {
		callBack(error, n.data);
	});
};
exports.saveSysConfig = function(config, callBack) {
	var client = global.zkCli;
	var node = new ZNode(client, null, sys_config_path);
	node.fetchData(function(error, n, s) {
		var exist = true;
		if (error) {
			if (error.code == -101) {
				exist = false;
			} else {
				callBack(error);
				return;
			}
		}
		if (exist) {
			var old = n.data;
			for ( var x in config) {
				old[x] = config[x];
			}
			n.data = old;
			n.save(function(error, node, stat) {
				callBack(error, old);
			});
		} else {
			node.data = config;
			node.create(function(error, n, p) {
				callBack(error, config);
			});
		}
	});

};
