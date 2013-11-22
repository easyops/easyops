var Task = require("../wizard").Task;

var StartupTask = function(){
	this.title = "configuration for host";
	this.description = "";	
};

StartupTask.prototype = new Task();
module.exports = StartupTask;