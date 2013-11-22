var Wizard = require("../wizard");
var HostConfigTask = require("host_config.js");
var MWConfigTask = require("mw_config.js");
var DBConfigTask = require("db_config.js");
var DeployLibTask = require("deploy_lib.js");
var DeployWarTask = require("deploy_war.js");
var InitDBTask = require("init_database.js");
var StartupTask = require("startup.js");
var InstallWizard = function() {
	this.title = "Install the Veris Billing System";
	this.description = "A wizard to guid you to install Veris Billing System";
	this.taskList.add(new HostConfigTask());
	this.taskList.add(new DBConfigTask());
	this.taskList.add(new MWConfigTask());
	this.taskList.add(new DeployLibTask());
	this.taskList.add(new DeployWarTask());
	this.taskList.add(new InitDBTask());
	this.taskList.add(new StartupTask());

};

InstallWizard.prototype.go = function(req, res) {
	req.session.insWiz = this;
};