var LoginUser = require("../bizz/loginUser");

exports.index = function(req, res) {
	res.render('index');

};

exports.console = function(req, res) {
	res.render("console");
};

exports.sendMessage = function(req, res) {
	var mes = req.param("message");
	var clientId = req.param("clientId");
	global.socket_io.sockets.clients().forEach(function(socket) {
		if (socket.clientId == clientId) {
			socket.emit("message", mes);
		}
	});
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