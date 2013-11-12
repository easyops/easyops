var authFilter = function(req, res, next) {
	var uri = req.path;
	if (!req.session.loginUser) {
		if (uri != "/login" && uri != "/doLogin" && uri != "/doLogout") {
			res.redirect("/login");
		}
	}
	next();
};

exports.authFilter = authFilter;