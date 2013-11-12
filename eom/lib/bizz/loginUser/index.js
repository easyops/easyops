var Cmd = require("../../res/cmd");

var LoginUser = function() {
	this.userId = "";
	this.password = "";
	this.status = 0;
	this.loginTime = new Date();
	this.loginFailCount = 0;
};

LoginUser.prototype.doLogin = function(userId, password, callBack) {
	var c = new Cmd("uptime", "remote");
	c.host.address = global.authHost;
	c.host.user = userId;
	c.host.password = password;
	c.callBack = function(error, stdout, stderr) {
		var message = "";
		var result = true;
		if (error) {
			result = false;
			message = error.toString();
		}
		callBack(result, message);
	};
	c.send();
};

LoginUser.prototype.getLoginUser = function(req) {
	return req.session.loginUser;
};
module.exports = LoginUser;