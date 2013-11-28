var Task = require("../wizard").Task;
var needle = require("needle");

var MWConfigTask = function(wizard) {
	this.title = "configuration for host";
	this.description = "";
	this.wizard = wizard;
};

MWConfigTask.prototype = new Task();
MWConfigTask.prototype.configPath = "/config/system/deployment/mwconfigs";
module.exports = MWConfigTask;

MWConfigTask.prototype.checkUnit = function(mwc, countDown) {
	var needle = require('needle');
	var opts = {
		username : mwc.user,
		password : mwc.password,
		auth : 'digest',
		headers : {
			Connection : 'keep-alive'
		}
	};
	var url = 'http://' + mwc.address + ':' + mwc.mport + '/management';
	needle.get(url, opts, function(err, resp, body) {
		if (err) {
			console.error("connect to %s error , this error is : %s", url, err);
			mwc.result = false;
			mwc.message = err;
			countDown.down();
			return;
		}
		if (resp.statusCode == 401) {
			console.log(resp.headers);
			mwc.result = false;
			mwc.message = " authorization error";
		} else {
			console.log(body);
			mwc.result = true;
		}
		countDown.down();
	});

};