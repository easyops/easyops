var host = require("../host");

var exec = require('child_process').exec, child;

var Cmd = function(cmd, args, type) {
	this._cmd = cmd;
	this._args = args;
	this.ret = true;
	this._type = type;
	this.host = host.Host();
	this.message = "";
};

Cmd.prototype.send = function(cbf) {
	if (this._type == "remote") {
		console.log("remote command is :"+this._cmd);
	} else {
		child = exec(this._cmd, function(error, stdout, stderr) {
			console.log('stdout: ' + stdout);
			console.log('stderr: ' + stderr);
			if (error !== null) {
				console.log('exec error: ' + error);
			}
			cbf(error,stdout,stderr);
		});
		return child;
	}
};

exports.Cmd = Cmd;