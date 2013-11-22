var Task = require("../wizard").Task;

var InitDBTask = function(){
	this.title = "configuration for host";
	this.description = "";	
};

InitDBTask.prototype = new Task();
module.exports = InitDBTask;