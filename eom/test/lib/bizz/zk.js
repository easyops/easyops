var should = require('should');
var zookeeper = require('node-zookeeper-client');
var Host = require("../../../lib/res/host");
var ZNode = require("../../../lib/res/zk");
var HCT = require("../../../lib/bizz/install_wizard/host_config.js");
var Domain = Host.Domain;

var ZooNode = function(pnode, path, client) {
	this.client = client;
	this.pnode = pnode;
	if (path.indexOf("/") === 0) {
		this.path = path;
	} else {
		this.path = pnode.path + "/" + path;
	}
	if (pnode !== null) {
		pnode.children.push(this);
	}
	this.count = 0;
	this.children = [];
	this.data = "";
};

ZooNode.prototype.deleteme = function() {
	var node = this;
	this.client.remove(this.path, -1, function(error) {
		if (error) {
			console.error("delete me<%s> error", node.path);
			console.error(error.stack);
			node.client.close();
			return;
		}
		console.log('Node %s delete itself.', node.path);
		if (node.pnode === null) {
			node.client.close();
			return;
		}
		console.log("pnode:" + node.pnode.path);
		node.pnode.count--;
		if (node.pnode.count === 0) {
			node.pnode.deleteme();
		}
	});

};

ZooNode.prototype.deleteNodes = function() {
	console.log("node=" + this.path);
	var node = this;
	node.client.getChildren(node.path,
			function(error, children, stats) {
				if (error) {
					console.error("get node %s 's children error :" + error,
							node.path);
					console.log("exit at error");
					node.client.close();
					return;
				}
				if (children && children.length !== 0) {
					node.count = children.length;
					for (var i = 0; i < children.length; i++) {
						var n = new ZooNode(node,
								node.path + "/" + children[i], node.client);
						n.deleteNodes();
					}
				} else {
					node.client.remove(node.path, -1, function(error) {
						if (error) {
							console.log(error.stack);
							node.client.close();
							return;
						}
						console.log('Node %s is deleted.', node.path);
						if (node.pnode !== null) {
							node.pnode.count--;
							if (node.pnode.count === 0) {
								node.pnode.deleteme();
							}
						}

					});
				}

			});
};

ZooNode.prototype.createNode = function(context) {
	var node = this;
	var bf;
	if (this.data === null) {
		bf = null;
	} else {
		bf = new Buffer(JSON.stringify(this.data));
	}
	this.client.create(this.path, bf, function(error, path) {
		if (error) {
			console.error(error);
		} else {
			console.log("create node %s ", path);
			for (var i = 0; i < node.children.length; i++) {
				node.children[i].createNode(context);
			}
		}
		context.count--;
		context.callBack();
	});
};

/*
describe("init configuration in zookeeper", function() {
	
	before(function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			var cnode = new ZooNode(null, "/config", client);
			cnode.done = done;
			cnode.deleteNodes();
		});
		client.on('state', function(state) {
			if (state === zookeeper.State.DISCONNECTED) {
				console.log('Client state is changed to disconnected.');
				done();
			}
		});
		client.connect();
	});
	
	it("create configuration nodes", function(done) {
		var client = zookeeper.createClient('localhost:2181');
		var context = {};
		context.count = 0;
		context.callBack = function() {
			if (this.count === 0) {
				client.close();
			}
		};
		var cnode = new ZooNode(null, "/config", client);
		context.count++;
		var snode = new ZooNode(cnode, "system", client);
		context.count++;
		var dnode = new ZooNode(snode, "deployment", client);
		context.count++;
		var hnode = new ZooNode(dnode, "hosts", client);
		context.count++;
		var host = new Host();
		host.id = "10.10.12.83";
		host.address = "10.10.12.83";
		host.user = "p0bm2";
		host.password = "p0bm2";
		var dbdomain = new Domain();
		dbdomain.title = "dbdomain";
		host.domains.push(dbdomain);
		var hostNode = new ZooNode(hnode, host.address, client);
		context.count++;

		hostNode.data = host;
		var mnode = new ZooNode(dnode, "domains", client);
		context.count++;
		var domain = new Domain();
		domain.id = "dbdomain";
		domain.title = "dbdomain";
		var domainNode = new ZooNode(mnode, domain.id, client);
		context.count++;
		domainNode.data = domain;
		domain = new Domain();
		domain.id = "bizdomain";
		domain.title = "bizdomain";
		domainNode = new ZooNode(mnode, domain.id, client);
		context.count++;
		domainNode.data = domain;
		domain = new Domain();
		domain.id = "configdomain";
		domain.title = "configdomain";
		domainNode = new ZooNode(mnode, domain.id, client);
		context.count++;
		domainNode.data = domain;
		domain = new Domain();
		domain.id = "webdomain";
		domain.title = "webdomain";
		domainNode = new ZooNode(mnode, domain.id, client);
		context.count++;
		domainNode.data = domain;
		client.once('connected', function() {
			cnode.createNode(context);
		});
		client.on('state', function(state) {
			if (state === zookeeper.State.DISCONNECTED) {
				console.log('Client state is changed to disconnected.');
				done();
			}
		});
		client.connect();

	});

});

*/
	
describe("hosts configuration test", function() {
	it("test for query host configuration", function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			global.zkCli = client;
			var hct = new HCT();
			hct.queryConfig(function(error, hosts) {
				hosts.length.should.equal(2);
				//hosts[0].should.have.property("address", "10.10.12.83");
				client.close();
			});
		});
		client.on('state', function(state) {
			if (state === zookeeper.State.DISCONNECTED) {
				console.log('Client state is changed to disconnected.');
				done();
			}
		});
		client.connect();
	});

	it("test for change host configuration", function(done) {
		var client = zookeeper.createClient('localhost:2181');
		client.once('connected', function() {
			global.zkCli = client;
			var hct = new HCT();
			var hosts = [];
			var host = new Host();
			host.id = "10.10.12.83";
			host.address = "10.10.12.83";
			host.user = "p0bm2";
			host.password = "p0bm2";
			var dbdomain = new Domain();
			dbdomain.title = "dbdomain";
			host.domains.push(dbdomain);

			hosts.push(host);

			host = new Host();
			host.id = "10.10.12.155";
			host.address = "10.10.12.155";
			host.user = "eoa";
			host.password = "eoa1234";
			host.domains.push(dbdomain);

			var bizdomain = new Domain();
			bizdomain.title = "bizdomain";
			host.domains.push(bizdomain);
			hosts.push(host);

			hct.saveConfig(hosts, function(error) {
				if (error) {
					console.error(error);
					client.close();
					return;
				}
				hct.queryConfig(function(error, hosts) {
					hosts.length.should.equal(2);
					hosts.should.includeEql(host);
					client.close();
				});
			});
		});
		client.on('state', function(state) {
			if (state === zookeeper.State.DISCONNECTED) {
				console.log('Client state is changed to disconnected.');
				done();
			}
		});
		client.connect();
	});
});
/*
 describe("test ZNode function in zookeeper", function() {
 before(function(done) {
 var client = zookeeper.createClient('localhost:2181');
 client.once('connected', function() {
 var node = new ZNode(client, null, "/test4zk");
 node.deleteNodes(function() {
 client.close();
 });
 });
 client.on('state', function(state) {
 if (state === zookeeper.State.DISCONNECTED) {
 console.log('Client state is changed to disconnected.');
 done();
 }
 });
 client.connect();
 });
 it("add node", function(done) {
 var client = zookeeper.createClient('localhost:2181');
 client.once('connected', function() {
 var node = new ZNode(client, null, "/test4zk");
 node.create(function(error, node, path) {
 if (error) {
 console
 .error("create node %s error : %s", node.path,
 error);
 } else {
 console.log("Node %s has been created.", node.path);
 }
 client.close();
 });
 });
 client.on('state', function(state) {
 if (state === zookeeper.State.DISCONNECTED) {
 console.log('Client state is changed to disconnected.');
 done();
 }
 });
 client.connect();
 });

 it("add all node", function(done) {
 var client = zookeeper.createClient('localhost:2181');
 client.once('connected', function() {
 var node = new ZNode(client, null, "/test4zk/nodes");
 var node_1 = new ZNode(client, node, "node_1");
 var node_2 = new ZNode(client, node, "node_2");
 new ZNode(client, node_1, "node_1_1");
 new ZNode(client, node_1, "node_1_2");
 new ZNode(client, node_1, "node_1_3");
 new ZNode(client, node_1, "node_1_3");

 new ZNode(client, node_2, "node_2_1");

 node.createAll(node, function(error, dr, path) {
 if (error) {
 console.error("create all nodes %s error : %s", node.path,
 error);
 client.close();
 } else {
 if (node == dr) {
 console.log("All nodes %s has been created.", dr.path);
 client.close();
 } else {
 console.log("one %s of nodes has been created.",
 node.path);
 }

 }

 });
 });
 client.on('state', function(state) {
 if (state === zookeeper.State.DISCONNECTED) {
 console.log('Client state is changed to disconnected.');
 done();
 }
 });
 client.connect();
 });
 });
 */