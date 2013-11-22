var Task = require("../wizard").Task;

var DeployWarTask = function(){
	this.title = "configuration for host";
	this.description = "";	
};

DeployWarTask.prototype = new Task();
DeployWarTask.prototype.configPath = "/config/system/deployment/warconfigs";
module.exports = DeployWarTask;