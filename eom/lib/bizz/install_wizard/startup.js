var Task = require("../wizard").Task;

var StartupTask = function(wizard){
	this.title = "configuration for host";
	this.description = "";	
	this.wizard = wizard;
};

StartupTask.prototype = new Task();
module.exports = StartupTask;