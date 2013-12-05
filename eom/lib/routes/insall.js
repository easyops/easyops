var Wizard = require("../bizz/install_wizard");

function getWizard(req, res) {
	if (!req.session.wizard) {
		new Wizard().go(req, res);
	}
	return req.session.insWiz;

}

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
};
