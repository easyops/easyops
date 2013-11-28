var should = require('should');
var zookeeper = require('node-zookeeper-client');
var MW = require("../../../lib/res/mw");
var MWT = require("../../../lib/bizz/install_wizard/mw_config.js");

describe("test middlewar function", function() {
	before(function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			global.zkCli = client;
			var task = new MWT();
			client.exists(task.configPath, function(err, stat) {
				if (err) {
					throw err;
				}
				if (stat) {
					console.log("config nodes is exist : " + task.configPath);
					done();
				} else {
					client.create(task.configPath, function(err, path) {
						if (err) {
							console.error(" can't create config node :"
									+ task.configPath);
							throw err;
						}
						console.log(" created config node:" + path);
						done();
					});
				}
			});
		});
		client.connect();
	});

	it("test for create middleware configuartion", function(done) {
		var client = global.zkCli;
		var task = new MWT();
		var configs = [];
		var cfg = new MW();
		cfg.id = "so_domain";
		cfg.address = "10.10.12.155";
		cfg.user = "eoa";
		cfg.password = "eoa123";
		cfg.mport = 4990;
		configs.push(cfg);

		task.saveConfig(configs, function(err) {
			if (err) {
				throw err;
			}
			console.info(" save middleware configs success !");
			done();
		});
	});
	it("test for checking middelware connection", function(done) {
		var checker = new MWT();
		checker.check(null, function(error, chk) {
			if (error) {
				console.error(error);
			} else {
				chk.result.should.equal(true);
				chk.subTask.should.have.length(1);
				for (var i = 0; i < chk.subTask.length; i++) {
					if (chk.subTask[i].id == "so_domain") {
						chk.subTask[i].result.should.equal(true);
					} else {
						chk.subTask[i].result.should.equal(false);
					}
				}
			}
			done();
		});
	});
});
