var ZNode = function(client, pnode, path, data) {
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
	this.data = data;
	this.children = [];
};
ZNode.prototype.create = function(callBack) {
	var node = this;
	var data;
	if (!this.data) {
		data = null;
	} else {
		data = new Buffer(JSON.stringify(this.data));
	}
	this.client.create(this.path, data, function(error, path) {
		if(error){
			console.error("Node %s create error %s", node.path, error);
		}else{
			console.log("Node %s has been created ", path);
		}
		callBack(error, node, path);
	});
};

ZNode.prototype.createAll = function(cr, callBack) {
	var node = this;
	if (!cr.count) {
		cr.count = 0;
	} else {
		cr.count++;
	}
	node.create(function(error, n, path) {
		if (error) {
			callBack(error, n, path);
			return;
		} else {
			cr.count--;
			if (cr.count === 0) {
				callBack(error, cr, path);
			}
		}
	});
	for (var i = 0; i < node.children.length; i++) {
		node.children[i].createAll(cr, callBack);
	}
};

ZNode.prototype.save = function(callBack) {
	var node = this;
	var data;
	if (!this.data) {
		data = null;
	} else {
		data = new Buffer(JSON.stringify(this.data));
	}
	this.client.setData(this.path, data, function(error, stat) {
		callBack(error, node, stat);
	});
};
ZNode.prototype.fetchData = function(callBack) {
	var node = this;
	this.client.getData(this.path, function(error, data, stat) {
		if (!error) {
			node.data = JSON.parse(data.toString("utf-8"));
		}
		callBack(error, node, stat);
	});
};

ZNode.prototype.listChildren = function(callBack) {
	var node = this;
	this.client.getChildren(this.path, function(error, children, stat) {
		if (children) {
			node.children=[];
			for (var i = 0; i < children.length; i++) {
				new ZNode(node.client, node, children[i]);
			}
		}
		callBack(error, node, stat);
	});
};

ZNode.prototype.remove = function(callBack) {
	var node = this;
	this.client.remove(this.path, function(error) {
		if (node.pnode) {
			var nc = [];
			for (var i = 0; i < node.pnode.children.length; i++) {
				if (node.pnode.children[i] != node) {
					nc.push(node.pnode.children[i]);
				}
			}
		}
		callBack(error, node);
	});
};

ZNode.prototype.deleteme = function(callBack) {
	var node = this;
	this.client.remove(this.path, -1, function(error) {
		if (error) {
			console.error("delete me<%s> error", node.path);
			console.error(error.stack);
			callBack();
			return;
		}
		console.log('Node %s delete itself.', node.path);
		if (node.pnode === null) {
			callBack();
			return;
		}
		console.log("pnode:" + node.pnode.path);
		node.pnode.count--;
		if (node.pnode.count === 0) {
			node.pnode.deleteme(callBack);
		}
	});

};

ZNode.prototype.deleteNodes = function(callBack) {
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
						var n = new ZNode(node.client,node,
								node.path + "/" + children[i]);
						n.deleteNodes(callBack);
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
								node.pnode.deleteme(callBack);
							}
						}

					});
				}

			});
};

ZNode.prototype.fetchChildren = function(callBack) {
	this.listChildren(function(error, node, stat) {
		if (error) {
			console.error("Node %s fetch children error:", node.path, error);
			callBack(error);
			return;
		}
		node.count = node.children.length;
		if(node.count===0){
			callBack(error, node, stat);
		}
		var cb = function(err, n, s) {
			if (err) {
				callBack(err, node, stat);
				return;
			}
			node.count--;
			if (node.count === 0) {
				callBack(err, node, stat);
			}
		};
		for (var i = 0; i < node.children.length; i++) {
			node.children[i].fetchData(cb);
		}
	});
};

module.exports = ZNode;