var Task = require("../wizard").Task;

var DeployLibTask = function(){
	this.title = "configuration for host";
	this.description = "";	
};

DeployLibTask.prototype = new Task();
DeployLibTask.prototype.configPath = "/config/system/deployment/libconfigs";
module.exports = DeployLibTask;