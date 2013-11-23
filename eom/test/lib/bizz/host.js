var should = require('should');
var zookeeper = require('node-zookeeper-client');
var Host = require("../../../lib/res/host");
var HCT = require("../../../lib/bizz/install_wizard/host_config.js");

describe("test host task function", function() {
	before(function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			global.zkCli = client;
			done();
		});
		client.connect();
	});	
	it("test for checking host", function(done) {
		var checker = new HCT();
		checker.check(null, function(error, chk) {
			if (error){
				console.error(error);				
			}else{
				chk.result.should.equal(false);
				chk.subTask.should.have.length(2);
				for(var i=0;i<chk.subTask.length;i++){
					if(chk.subTask[i].id=="10.10.12.83"){
						chk.subTask[i].result.should.equal(true);
					}else{
						chk.subTask[i].result.should.equal(false);
					}
				}
			}
			done();
		});
	});
});
