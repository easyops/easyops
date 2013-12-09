var Wizard = require("../bizz/install_wizard");

function getWizard(req, res) {
	if (!req.session.wizard) {
		new Wizard().go(req, res);
	}
	return req.session.insWiz;

}

exports.sysConfig = function(req, res) {
	var wizard = getWizard(req, res);
	wizard.SysConfig.getSysConfig(function(error, config) {
		if (error) {
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				data : config
			});
		}
	});
};

exports.saveSysConfig = function(req, res) {
	var wizard = getWizard(req, res);
	var config = req.param("config");
	wizard.SysConfig.saveSysConfig(config, function(error, all_config) {
		if (error) {
			console.error(error);
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				data : all_config
			});
		}
	});
};

exports.configs = function(req, res) {
	var wizard = getWizard(req, res);
	var step = parseInt(req.param("step"), 0);
	wizard.taskList[step].queryConfig(function(error, configs) {
		if (error) {
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				data : configs
			});
		}
	});
};

exports.addConfig = function(req, res) {
	var wizard = getWizard(req, res);
	var step = parseInt(req.param("step"), 0);
	var config = req.param("config");
	wizard.taskList[step].addConfig(config, function(error){
		if (error) {
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				message : "success to add config"
			});
		}
	});
};

exports.deleteConfig = function(req, res) {
	var wizard = getWizard(req, res);
	var step = parseInt(req.param("step"), 0);
	var id = req.param("id");
	wizard.taskList[step].deleteConfig(id, function(error){
		if (error) {
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				message : "success to delete config"
			});
		}
	});
};

exports.checkConfig = function(req, res) {
	var wizard = getWizard(req, res);
	var step = parseInt(req.param("step"), 0);
	var id = req.param("id");
	var selected = [{}];
	selected[0].id = id;
	wizard.taskList[step].check(selected, function(error){
		if (error) {
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				message : "success to add config"
			});
		}
	});
};

exports.getAllDomain = function(req, res) {
	var wizard = getWizard(req, res);
	dc = wizard.domainConfig;
	dc.queryConfig(function(error, domains) {
		if (error) {
			res.json({
				result : "fail",
				message : error
			});
		} else {
			res.json({
				result : "success",
				data : domains
			});
		}
	});
};
