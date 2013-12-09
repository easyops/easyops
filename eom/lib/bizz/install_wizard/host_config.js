var Task = require("../wizard").Task;
var Cmd = require("../../res/cmd");
var logger = require("../../common/util").logger;
var async = require("async");
var SysConfig = require("./sys_config");
var HostConfigTask = function(wizard) {
	this.title = "configuration for host";
	this.description = "";
	this.wizard = wizard;
};
HostConfigTask.prototype = new Task();
HostConfigTask.prototype.configPath = "/config/system/deployment/hosts";

function getRealAuth(host) {
	if (host.globalUser === "true") {
		if (global.sys_config) {
			host.user = global.sys_config.user;
			host.password = global.sys_config.password;
		}
	}
	return host;
}

HostConfigTask.prototype.checkUnit = function(host, countDown) {
	var cmd = new Cmd("uptime", "remote");
	cmd.host = getRealAuth(host);
	cmd.callBack = function(error, stdout, stderr) {
		if (error) {
			host.result = false;
			host.message = "connect host fail, host :" + host.address;
			logger.info("check for connection to host " + cmd.host.id + " error!");
			countDown.down();
		} else {
			host.result = true;
			host.message = "success to connect host :" + host.address;
			logger.info("check for connection to host " + cmd.host.id + " success!");
			var fun_init = function(callBack) {
				var context = {};
				context.sh_file = "check_env.sh";
				context.localShellPath = "resources/shell";
				context.remoteShellPath = "./shell";
				context.step = "init";
				callBack(null, context);
			};

			var fun_check_folder = function(cnt, callBack) {
				cnt.step = "check_folder";
				if (cnt.error) {
					callBack(cnt.error, cnt);
					return;
				}
				var cmd = new Cmd("cd " + cnt.remoteShellPath, "remote");
				cmd.host = host;
				cmd.send(function(error, stdout, stderr) {
					console.log("cmd.exec_code:" + cmd.exec_code);
					if (error) {
						cnt.error = error;
						callBack(null, cnt);
						return;
					}
					if (cmd.exec_code !== 0) {
						cnt.error = cmd.error + ":" + cmd.stderr;
						callBack(null, cnt);
						return;
					}
					callBack(null, cnt);
				});

			};

			var fun_create_folder = function(cnt, callBack) {
				cnt.step = "create_folder";
				if (cnt.error) {
					logger.debug("folder " + cnt.remoteShellPath + " is't exist");
					cnt.error = null;
					var cmd = new Cmd("mkdir " + cnt.remoteShellPath, "remote");
					cmd.host = host;
					cmd.send(function(error, stdout, stderr) {
						if (error) {
							cnt.error = error;
							callBack(error, cnt);
							return;
						} else {
							if (cmd.exec_code !== 0) {
								cnt.error = cmd.error + ":" + cmd.stderr;
								callBack(cnt.error, cnt);
								return;
							}
							callBack(null, cnt);
						}
					});
					callBack(cnt.error, cnt);
					return;
				} else {
					callBack(null, cnt);
					return;
				}
			};

			var fun_deliver_script = function(cnt, callBack) {
				cnt.step = "put file";
				if (cnt.error) {
					callBack(cnt.error, cnt);
					return;
				}
				var ftp = new Cmd("put", "sftp");
				ftp.host = host;
				ftp.args[0] = cnt.localShellPath + "/" + cnt.sh_file;
				ftp.args[1] = cnt.remoteShellPath + "/" + cnt.sh_file;
				ftp.send(function(err, stdout, stderr) {
					if (err) {
						cnt.error = err;
					} else {
						cnt.shell_exe = "sh check_env.sh";
					}
					callBack(cnt.error, cnt);
				});
			};

			var fun_exe_script = function(cnt, callBack) {
				cnt.step = "exec_file";
				if (cnt.error) {
					callBack(cnt.error, cnt);
					return;
				}
				var exe = new Cmd(cnt.shell_exe, "remote");
				exe.host = host;
				exe.send(function(err, stdout, stderr) {
					if (err) {
						cnt.error = err;
						cnt.result = false;
					} else {
						cnt.result = true;
					}
					callBack(cnt.error, cnt);
				});
			};

			async.waterfall([ fun_init, fun_check_folder, fun_create_folder, fun_deliver_script,
					fun_exe_script ], function(error, cnt) {
				logger.info("check step : " + cnt.step);
				if (error) {
					logger.error(error);
					host.result = false;
					host.message = "check host " + host.address + " error : " + error;
				} else {
					host.result = true;
					host.message = "check host ok host is :" + host.address;
				}
				countDown.down();
			});
		}

	};
	cmd.send();
};

module.exports = HostConfigTask;