var CountDown = function(count, callBack, context) {
	this.count = count;
	this.callBack=callBack;
	this.context = context;
};

CountDown.prototype.down = function(n)
{
	if (n) {
		this.count = this.count - n;
	} else {
		this.count--;
	}
	if(this.count<=0){
		this.callBack(this);
	}
};
module.exports.CountDown = CountDown;
