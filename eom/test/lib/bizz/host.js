var should = require('should');
var zookeeper = require('node-zookeeper-client');
var Host = require("../../../lib/res/host");
var HCT = require("../../../lib/bizz/install_wizard/host_config.js");

describe("test host task function", function() {
	before(function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			global.zkCli = client;
			client.remove("/config/system/deployment/hosts/10.10.12.150",
					function(error) {
						console.error(error);
					})
			done();
		});
		client.connect();
	});

	it("test for add host", function(done) {
		var hct = new HCT();
		var host = new Host();
		host.id = "10.10.12.150";
		host.address = "10.10.12.150";
		host.user = "eoa";
		host.password = "oea1234";
		hct.addConfig(host, function(error, path) {
			console.log("add host path :" + path);
			should.not.exist(error);
			done();
		});
	});
	
	it("test for update host", function(done){
		var hct = new HCT();
		var host = new Host();
		host.id = "10.10.12.150";
		host.address = "10.10.12.150";
		host.user = "eoa";
		host.password = "oea123";
		hct.updateConfig(host, function(error, stat) {
			console.log("host stat :" + stat);
			should.not.exist(error);
			done();
		});
	});
	
	/*it.skip("test for remove host", function(done){
		var hct = new HCT();
		var host = new Host();
		host.id = "10.10.12.150";
		hct.updateConfig(host, function(error) {
			if(error){
				console.error(error);
			}
			should.not.exist(error);
			done();
		});
	});
*/
	it("test for checking host", function(done) {
		var checker = new HCT();
		checker.check(null, function(error, chk) {
			if (error) {
				console.error(error);
			} else {
				chk.result.should.equal(false);			
				for (var i = 0; i < chk.subTask.length; i++) {
					if (chk.subTask[i].id == "10.10.12.83") {
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
