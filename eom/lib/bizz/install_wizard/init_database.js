var Task = require("../wizard").Task;

var InitDBTask = function(wizard){
	this.title = "configuration for host";
	this.description = "";	
	this.wizard = wizard;
};

InitDBTask.prototype = new Task();
module.exports = InitDBTask;