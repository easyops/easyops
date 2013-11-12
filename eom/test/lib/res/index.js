var should = require('should');
var Cmd = require("../../../lib/res/cmd");
describe("excute command", function() {
	it("send local command <ls>", function(done) {
		var c = new Cmd("dir");
		c.callBack = function(error, stdout, stderr){
			console.log("checkout command <dir> result!!");
			checkCommand(error, stdout, stderr);
			done();
		};
		c.send();

	});

	it("send remote command <uptime>", function(done) {
		var c = new Cmd("uptime", "remote");
		c.host.address = "10.10.12.83";
		c.callBack = function(error, stdout, stderr){
			console.log("checkout command <uptime> result!!");
			checkCommand(error, stdout, stderr);
			done();
		};
		c.send();

	});	
	it("get file by sftp:", function(done) {
		this.timeout(5000);
		var c = new Cmd("get", "sftp", ["./sftptestfile_local","/data01/p0grp/p0bm2/sftptestfile"]);
		c.host.address = "10.10.12.83";
		c.callBack = function(error, stdout, stderr){
			console.log("checkout command <get file> result!!");
			checkCommand(error, stdout, stderr);
			done();
		};
		c.send();

	});
	
	it("put file by sftp:", function(done) {
		this.timeout(5000);
		var c = new Cmd("put", "sftp", ["./sftptestfile_local","/data01/p0grp/p0bm2/sftptestfile_remote"]);
		c.host.address = "10.10.12.83";
		c.callBack = function(error, stdout, stderr){
			console.log("checkout command <put file> result!!");
			checkCommand(error, stdout, stderr);
			done();
		};
		c.send();

	});
	
	it.skip("get big file by sftp:", function(done) {
		this.timeout(50000);
		var c = new Cmd("get", "sftp", ["./testsftpforbigfile_local","/data01/p0grp/p0bm2/testsftpforbigfile"]);
		c.host.address = "10.10.12.83";
		c.callBack = function(error, stdout, stderr){
			console.log("checkout command <get big file> result!!");
			checkCommand(error, stdout, stderr);
			done();
		};
		c.send();
	});
	
});

var checkCommand = function(error, stdout, stderr){
	should.strictEqual(null, error);
	stdout.should.not.be.empty;
};