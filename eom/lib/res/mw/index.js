var MiddleWare = function(id, address, mport, user, password){
	this.Id = id;
	this.address = address;
	this.mport = mport;
	this.user = user;
	this.password = password;
	this.instances = [];
};

module.exports = MiddleWare;