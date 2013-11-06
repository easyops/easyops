
exports.Domain = function(){
	this.id = "";
	this.title = "";
	this.hosts = [];
};

exports.Host  = function(){
	this.address = "";	
	this.user="";
	this.password="";
	this.domains= [];
};

