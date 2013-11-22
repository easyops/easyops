
var Domain = function(){
	this.id = "";
	this.title = "";
	this.hosts = [];
	this.subDomains = [];
};

var Host  = function(){
	this.id = "";
	this.address = "";	
	this.user="p0bm2";
	this.port=22;
	this.password="p0bm2";
	this.domains= [];
};

module.exports = Host;
module.exports.Domain = Domain;