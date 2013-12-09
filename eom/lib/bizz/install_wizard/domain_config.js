var Task = require("../wizard").Task;

var DomainConfigTask = function(wizard) {
	this.title = "configuration for domain";
	this.description = "";
	this.wizard = wizard;
};
DomainConfigTask.prototype = new Task();
DomainConfigTask.prototype.configPath = "/config/system/deployment/domains";

module.exports = DomainConfigTask;