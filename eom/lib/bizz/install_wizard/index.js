var Wizard = require("../wizard");
var HostConfigTask = require("./host_config.js");
var MWConfigTask = require("./mw_config.js");
var DBConfigTask = require("./db_config.js");
var DeployLibTask = require("./deploy_lib.js");
var DeployWarTask = require("./deploy_war.js");
var InitDBTask = require("./init_database.js");
var InstallTask = require("./install_process.js");
var StartupTask = require("./startup.js");
var SysConfig = require("./sys_config.js");
var DomainConfig = require("./domain_config.js");
var InstallWizard = function() {
	this.title = "Install the Veris Billing System";
	this.description = "A wizard to guid you to install Veris Billing System";
	this.host = new HostConfigTask();
	this.db = new DBConfigTask();
	this.mw = new MWConfigTask();
	this.initDB = new InitDBTask();
	this.install = new InstallTask();
	this.startup = new StartupTask();
	this.taskList = [];
	this.taskList.push(this.host);
	this.taskList.push(this.db);
	this.taskList.push(this.mw);
	this.taskList.push(this.initDB);
	this.taskList.push(this.install);
	this.taskList.push(this.startup);
	this.SysConfig = SysConfig;
	this.domainConfig = new DomainConfig();
};

InstallWizard.prototype.go = function(req, res) {
	req.session.insWiz = this;
};
module.exports = InstallWizard;