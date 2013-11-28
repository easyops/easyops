var Task = require("../wizard").Task;

var DeployLibTask = function(wizard){
	this.title = "configuration for host";
	this.description = "";	
	this.wizard = wizard;
};
DeployLibTask.prototype = new Task();
DeployLibTask.prototype.configPath = "/config/system/deployment/libconfigs";
module.exports = DeployLibTask;
