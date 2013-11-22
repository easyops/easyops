var Wizard = function(){
	var title = "";
	var description = "";
	var summary = "";
	var taskList = [];
	var curTask;
	var context;
};

var Context = function(){
	var console;
};

module.exports = Wizard;
module.exports.Task = require("./task.js");