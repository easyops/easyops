var CountDown = function(count, callBack, context) {
	this.count = count;
	this.callBack = callBack;
	this.context = context;
};

CountDown.prototype.down = function(n) {
	if (n) {
		this.count = this.count - n;
	} else {
		this.count--;
	}
	if (this.count <= 0) {
		this.callBack(this);
	}
};

var logger = function(outer) {
	this.outer = outer;
};

logger.prototype.info = function(str) {
	console.log(str);
	if (this.outer) {
		this.outer.emit("info", {
			message : str
		});
	}
};
logger.prototype.error = function(str, e) {
	console.error(str);
	console.error(e);
	if (this.outer) {
		this.outer.emit("info", {
			message : str
		});
	}
};
logger.prototype.debug = function(str) {
	console.log(str);
	if (this.outer) {
		this.outer.emit("info", {
			message : str
		});
	}
};

module.exports.CountDown = CountDown;
if (global.logger) {
	module.exports.logger = global.logger;
} else {
	module.exports.logger = new logger();
}
