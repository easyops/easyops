var Task = require("../wizard").Task;

var MWConfigTask = function(){
	this.title = "configuration for host";
	this.description = "";	
};

MWConfigTask.prototype = new Task();
HostConfigTask.prototype.configPath = "/config/system/deployment/mwconfigs";
module.exports = MWConfigTask;