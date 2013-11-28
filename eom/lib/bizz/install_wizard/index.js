var Wizard = require("../wizard");
var HostConfigTask = require("host_config.js");
var MWConfigTask = require("mw_config.js");
var DBConfigTask = require("db_config.js");
var DeployLibTask = require("deploy_lib.js");
var DeployWarTask = require("deploy_war.js");
var InitDBTask = require("init_database.js");
var InstallTask = require("install.js");
var StartupTask = require("startup.js");
var InstallWizard = function() {
	this.title = "Install the Veris Billing System";
	this.description = "A wizard to guid you to install Veris Billing System";
	this.taskList.push(new HostConfigTask());
	this.taskList.push(new DBConfigTask());
	this.taskList.push(new MWConfigTask());
	this.taskList.push(new InitDBTask());
	this.taskList.push(new InstallTask());
	this.taskList.push(new StartupTask());
};

InstallWizard.prototype.go = function(req, res) {
	req.session.insWiz = this;
};