var LoginUser = require("../bizz/loginUser");

exports.index = function(req, res) {
	res.render('index');

};

exports.console = function(req, res) {
	res.render("console");
};

exports.sendMessage = function(req, res) {
	var str = req.param("command");
	var Cmd = require("../res/cmd");
	var cmd = new Cmd();
	cmd.cmdLine = str;
	cmd.type = "local";
	cmd.send();
};

exports.login = function(req, res) {
	res.render('login');
};
exports.doLogin = function(req, res) {
	console.log(" do Login");
	var userId = req.param("userId");
	var password = req.param("password");
	var loginUser = new LoginUser();
	if (global.authHost) {
	} else {
		if (process.argv[2]) {
			global.authHost = process.argv[2];
		} else {
			global.authHost = req.host;
		}
	}
	loginUser.doLogin(userId, password, function(result, message) {
		if (result === true) {
			req.session.loginUser = loginUser;
			res.json({
				result : "success",
				message : message
			});
		} else {
			res.json({
				result : "fail",
				message : message
			});
		}
	});
};
exports.doLogout = function(req, res) {

};
exports.install = require("./install");