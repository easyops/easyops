var cm = require("../../../lib/res/cmd");
describe("excute local command", function() {
	it("send local command <ls>", function(done) {
		var c = new cm.Cmd("dir");
		c.send(function(error, stdout, stderr) {
			done();
		});

	});

});