var Task = require("../wizard").Task;

var DeployWarTask = function(wizard){
	this.title = "configuration for host";
	this.description = "";	
	this.wizard = wizard;
};

DeployWarTask.prototype = new Task();
DeployWarTask.prototype.configPath = "/config/system/deployment/warconfigs";
module.exports = DeployWarTask;