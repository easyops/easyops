var DataBase = function(id, address, port, user, password) {
	this.id = id;
	if (address) {
		this.address = address;
	} else {
		this.address = "localhost";
	}

	if (port) {
		this.port = port;
	} else {
		this.port = 3306;
	}
	this.user = user;
	this.password = password;
};

module.exports = DataBase;
