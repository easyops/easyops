var Task = require("../wizard").Task;

var DBConfigTask = function(){
	this.title = "configuration for databse";
	this.description = "";	
};

DBConfigTask.prototype = new Task();
DBConfigTask.prototype.configPath = "/config/system/deployment/dbconfigs";
module.exports = DBConfigTask;