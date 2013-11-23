var Task = require("../wizard").Task;
var Cmd = require("../../res/cmd");
var HostConfigTask = function() {
	this.title = "configuration for host";
	this.description = "";
};
HostConfigTask.prototype = new Task();
HostConfigTask.prototype.configPath = "/config/system/deployment/hosts";

HostConfigTask.prototype.checkUnit = function(host, countDown) {
	var cmd = new Cmd("uptime", "remote");
	cmd.host = host;
	cmd.callBack = function(error, stdout, stderr) {
		if (error) {
			cmd.host.result = false;
			cmd.host.message = "connect host fail, host :" + cmd.host.address;
		} else {
			cmd.host.result = true;
			cmd.host.message = "success to connect host :" + cmd.host.address;
		}
		countDown.down();
	};
	cmd.send();
};

module.exports = HostConfigTask;