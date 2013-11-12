var Host = require("../host");
var Connection = require('ssh2');
var exec = require('child_process').exec;

var Cmd = function(cmd, type, args, host, callBack) {
	this.cmdLine = cmd;
	this.args = args;
	this.ret = true;
	this.type = type;
	if (type != "local") {
		if (host) {
			this.host = host;
		} else {
			this.host = new Host();
		}
	}
	this.message = "";
	this.callBack = callBack;
};

Cmd.prototype.send = function() {
	if (this.type == "sftp") {
		sftp(this.cmdLine, this.args[0], this.args[1], this.host, this.callBack);
		console.log("sftp command :" + this.cmdLine);
	} else if (this.type == "remote") {
		remoteCommand(this.cmdLine, this.host, this.callBack);
		console.log("remote command is :" + this.cmdLine);
	} else {
		localCommand(this.cmdLine, this.callBack);
		console.log("local command is: " + this.cmdLine);
	}
};

function sftp(cmd, source, target, host, callBack) {
	var error;
	var stdout;
	var stderr;
	var c = new Connection();
	c.on('connect', function() {
		console.log('Connection :: connect');
	});
	c.on('ready', function() {
		var opts = {
			concurrency : 25,
			chunkSize : 32768,
			step : function(tt, c, t) {
				console.log("tt:" + tt + ", c:" + c + ",t:" + t + "\n");
			}
		};
		console.log('Connection :: ready');
		c.sftp(function(err, sftp) {
			if (err) {
				error = err;
			} else {
				error = null;
			}
			sftp.on('end', function() {
				console.log('SFTP :: SFTP session closed');
			});
			if (cmd == "get") {
				console.log("From host=" + host + ", get file=" + target
						+ ", save to file=" + source + "\n");
				sftp.fastGet(target, source, opts, function(err) {
					if (err)
						error = err;
					else {
						error = null;
						stdout = source;
						stderr = "";
					}
					sftp.end();
					c.end();
					if (callBack) {
						callBack(error, stdout, stderr);
					}
				});
			} else if (cmd == "put") {
				console.log("To host=" + host + ", put file=" + target
						+ ", frome local file=" + source + "\n");
				sftp.fastPut(source, target, opts, function(err) {
					if (err)
						error = err;
					else {
						error = null;
						stdout = source;
						stderr = "";
					}
					sftp.end();
					c.end();
					if (callBack) {
						callBack(error, stdout, stderr);
					}
				});
			}
		});
	});
	c.on('error', function(err) {
		console.log('Connection :: error :: ' + err);
	});
	c.on('end', function() {
		console.log('Connection :: end');
	});
	c.on('close', function(had_error) {
		console.log('Connection :: close');
	});
	c.connect({
		host : host.address,
		port : host.port,
		username : host.user,
		password : host.password
	});

}

function localCommand(cmd, callBack) {
	var child = exec(cmd, function(error, stdout, stderr) {
		console.log('stdout: ' + stdout);
		console.log('stderr: ' + stderr);
		if (error !== null) {
			console.log('exec error: ' + error);
		}
		if (callBack) {
			callBack(error, stdout, stderr);
		}
	});
}

function remoteCommand(cmd, host, callBack) {
	var error;
	var stdout;
	var stderr;
	var c = new Connection();
	c.on('connect', function() {
		console.log('Connection :: connect');
	});
	c.on('ready', function() {
		console.log('Connection :: ready');
		c.exec('uptime', function(err, stream) {
			if (!err) {
				error = null;
			} else {
				error = err;
			}
			stream.on('data', function(data, extended) {
				if (extended === 'stderr') {
					stderr += data;
				} else {
					stdout += data;
				}
				console.log((extended === 'stderr' ? 'STDERR: ' : 'STDOUT: ')
						+ data);
			});
			stream.on('end', function() {
				console.log('Stream :: EOF');
			});
			stream.on('close', function() {
				console.log('Stream :: close');
				callBack(error, stdout, stderr);
			});
			stream.on('exit', function(code, signal) {
				console.log('Stream :: exit :: code: ' + code + ', signal: '
						+ signal);
				c.end();
			});
		});
	});
	c.on('error', function(err) {
		error = err;
		console.log('Connection :: error :: ' + err);
		stderr = err;
		callBack(error, stdout, stderr);
	});
	c.on('end', function() {
		console.log('Connection :: end');
	});
	c.on('close', function(had_error) {
		console.log('Connection :: close');
	});
	c.connect({
		host : host.address,
		port : host.port,
		username : host.user,
		password : host.password
	});
}
module.exports = Cmd;
