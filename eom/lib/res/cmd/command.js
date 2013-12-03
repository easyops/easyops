var Host = require("../host");
var Connection = require('ssh2');
var exec = require('child_process').exec;
var logger = require('../../common/util').logger;

var Cmd = function(cmd, type, args, host, callBack) {
	this.cmdLine = cmd;
	if (args) {
		this.args = args;
	} else {
		this.args = [];
	}
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
	this.stdout = null;
	this.stderr = null;
	this.error = null;
	this.exec_code = 0;
};

Cmd.prototype.remoteCommand =  function () {
	var emitter = global.emitter;
	var error;
	var stdout;
	var stderr;
	var self = this;
	var host = this.host;
	var c = new Connection();
	c.on('connect', function() {
		logger.info('Connection :: connect');
	});
	c.on('ready', function() {
		logger.info('Connection :: ready');
		c.exec(self.cmdLine, function(err, stream) {
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
				logger.info((extended === 'stderr' ? 'STDERR: ' : 'STDOUT: ')
						+ data);
			});
			stream.on('end', function() {
				logger.info('Stream :: EOF');
			});
			stream.on('close', function() {
				logger.info('Stream :: close');
				self.stdout = stdout;
				self.stderr = stderr;
				self.error = error;
				if (self.callBack) {
					self.callBack(error, stdout, stderr);
				}
			});
			stream.on('exit', function(code, signal) {
				logger.info('Stream :: exit :: code: ' + code + ', signal: '
						+ signal);
				self.exec_code = code;
				c.end();
			});
		});
	});
	c.on('error', function(err) {
		error = err;
		logger.error('Connection :: error :: ' + err);
		stderr = err;
		if(self.callBack){
			self.callBack(error, stdout, stderr);
		}
	});
	c.on('end', function() {
		logger.info('Connection :: end');
	});
	c.on('close', function(had_error) {
		logger.info('Connection :: close');
	});
	c.connect({
		host : host.address,
		port : host.port,
		username : host.user,
		password : host.password
	});
};

Cmd.prototype.send = function(callBack) {
	if (callBack) {
		this.callBack = callBack;
	}
	if (this.type == "sftp") {
		sftp(this.cmdLine, this.args[0], this.args[1], this.host, this.callBack);
		logger.info("sftp command :" + this.cmdLine);
	} else if (this.type == "remote") {
		this.remoteCommand();
		logger.info("remote command is :" + this.cmdLine);
	} else {
		localCommand(this.cmdLine, this.callBack);
		logger.info("local command is: " + this.cmdLine);
	}
};

function sftp(cmd, source, target, host, callBack) {
	var emitter = global.emitter;
	var error;
	var stdout;
	var stderr;
	var c = new Connection();
	c.on('connect', function() {
		logger.info('Connection :: connect');
	});
	c.on('ready', function() {
		var opts = {
			concurrency : 25,
			chunkSize : 32768,
			step : function(tt, c, t) {
				logger.info("tt:" + tt + ", c:" + c + ",t:" + t + "\n");
			}
		};
		logger.info('Connection :: ready');
		c.sftp(function(err, sftp) {
			if (err) {
				error = err;
			} else {
				error = null;
			}
			sftp.on('end', function() {
				logger.info('SFTP :: SFTP session closed');
			});
			if (cmd == "get") {
				logger.info("From host=" + host + ", get file=" + target
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
					} else {
						logger.info("no callBack function");
					}
				});
			} else if (cmd == "put") {
				logger.info("To host=" + host + ", put file=" + target
						+ ", from local file=" + source + "\n");
				sftp.fastPut(source, target, opts, function(err) {
					if (err) {
						error = err;
						console.error(err);
					} else {
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
		logger.info('Connection :: error :: ' + err);
	});
	c.on('end', function() {
		logger.info('Connection :: end');
	});
	c.on('close', function(had_error) {
		logger.info('Connection :: close');
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

		logger.info(stdout);
		logger.error(stderr);

		if (error !== null) {
			logger.info('exec error: ' + error);
		}
		if (callBack) {
			callBack(error, stdout, stderr);
		}
	});
}


module.exports = Cmd;
