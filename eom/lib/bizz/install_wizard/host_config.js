var Task = require("../wizard").Task;
var CountDown = require("../../common/util.js").CountDown;
var Cmd = require("../../res/cmd");
var HostConfigTask = function() {
	this.title = "configuration for host";
	this.description = "";
};
HostConfigTask.prototype = new Task();
HostConfigTask.prototype.configPath = "/config/system/deployment/hosts";
HostCheckTask = function() {
	this.title = "check host";
	this.description = "";
};
HostCheckTask.prototype = new Task();
HostCheckTask.prototype.check = function(hosts, callBack) {
	var self = this;
	var hct = new HostConfigTask();
	hct.queryConfig(function(error, configs) {
		if (error) {
			callBack(error, self);
			return;
		} else {
			self.subTask = configs;
			var cd = new CountDown(configs.length, function() {
				var host;
				self.result = true;
				for (var i = 0; i < configs.length; i++) {
					host = configs[0];
					if (host.result === false) {
						self.result = false;
					}
				}
				callBack(null, self);
			});
			configs.forEach(function(e, i, a) {
				if (hosts) {
					var tag = false;
					for (var k = 0; k < hosts.length; k++) {
						if (hosts[k].id == e.id) {
							tag = true;
						}
					}
					if (!tag) {
						cd.down();
						return;
					}
				}
				var cmd = new Cmd("uptime", "remote");
				cmd.host = e;
				cmd.callBack = function(error, stdout, stderr) {					
					if (error) {
						cmd.host.result = false;
						cmd.host.message = "connect host fail, host info:"
								+ cmd.host.address;
					} else {
						cmd.host.result = true;
						cmd.host.message = "success to connect host :"
								+ cmd.host.address;
					}
					cd.down();
				};
				cmd.send();
			});
		}
	});
}
module.exports = HostConfigTask;

module.exports.HostCheckTask = HostCheckTask;