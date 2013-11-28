var Task = require("../wizard").Task;
var mysql = require('mysql');
var DBConfigTask = function(wizard) {
	this.title = "configuration for databse";
	this.description = "";
	this.wizard = wizard;
};
DBConfigTask.prototype = new Task();
DBConfigTask.prototype.configPath = "/config/system/deployment/dbconfigs";

DBConfigTask.prototype.checkUnit = function(db, countDown) {
	var connection = mysql.createConnection({
		host : db.address,
		user : db.user,
		password : db.password
	});

	connection.connect(function(err) {
		if (err) {
			console.error("connect to %s error: %s", db.id, err);
			db.result = false;
			db.message = "connect host fail, host :" + db.address;
			countDown.down();
		} else {
			connection.query('SELECT 1 + 1 AS solution', function(err, rows,
					fields) {
				if (err) {
					console.error("execute query error on %s error: %s", db.id, err);
					db.result = false;
					db.message = "connect host fail, host :" + db.address;
				} else {
					db.result = true;
					db.message = "success to connect mysql :" + db.address;
				}
				console.log('The solution is: ', rows[0].solution);
				connection.end();
				countDown.down();
			});
		}
	});

};

module.exports = DBConfigTask;