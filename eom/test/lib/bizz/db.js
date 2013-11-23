var should = require('should');
var zookeeper = require('node-zookeeper-client');
var DB = require("../../../lib/res/db");
var DCT = require("../../../lib/bizz/install_wizard/db_config.js");

describe("test task function", function() {
	before(function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			global.zkCli = client;
			var dct = new DCT();
			client.exists(dct.configPath, function(err, stat) {
				if (err) {
					throw err;
				}
				if (stat) {
					console.log("config nodes is exist : " + dct.configPath);
					done();
				} else {
					client.create(dct.configPath, function(err, path) {
						if (err) {
							console.error(" can't create config node :"
									+ dct.configPath);
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

	it("test for create db configuartion", function(done) {
		var client = global.zkCli;
		var dct = new DCT();
		var dbs = [];
		var db = new DB();
		db.id = "basedb";
		db.address = "localhost";
		db.user = "root";
		db.password = "root123";
		dbs.push(db);
		db = new DB();
		db.id = "acctdb";
		db.address = "10.10.12.83";
		db.user = "fu";
		db.password = "fu";
		dbs.push(db);
		db = new DB();
		db.id = "billingdb";
		db.address = "10.10.12.84";
		db.user = "ob60";
		db.password = "ob60";
		db.port = 3310;
		dbs.push(db);
		dct.saveConfig(dbs, function(err) {
			if (err) {
				throw err;
			}
			console.info(" save db configs success !");
			done();
		});
	});
	it("test for checking db connection", function(done) {
		var checker = new DCT();
		checker.check(null, function(error, chk) {
			if (error) {
				console.error(error);
			} else {
				chk.result.should.equal(false);
				chk.subTask.should.have.length(3);
				for (var i = 0; i < chk.subTask.length; i++) {
					if (chk.subTask[i].id == "basedb") {
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
