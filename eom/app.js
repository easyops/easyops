/**
 * Module dependencies.
 */

var routesPath = "routes";
if (process.env.routes) {
	routesPath = process.env.routes;
}

var express = require('express');
var root = require('./lib/' + routesPath);
var filter = require("./lib/filter");
var http = require('http');
var path = require('path');
var zookeeper = require('node-zookeeper-client');
var logger = require('./lib/common/util').logger;

var app = express();
// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.static(path.join(__dirname, 'public')));
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.cookieParser('%Qwert13579#'));
app.use(express.session());
// app.use(filter.authFilter);
app.use(app.router);

// development only
if ('development' == app.get('env')) {
	app.use(express.errorHandler());
}

app.get('/', root.index);
app.get('/login', root.login);
app.get('/doLogin', root.doLogin);
app.post('/doLogin', root.doLogin);
app.get("/console", root.console);
app.post("/sendMessage", root.sendMessage);

var server = http.createServer(app).listen(app.get('port'), function() {
	console.log('Express server listening on port ' + app.get('port'));
});
io = require('socket.io').listen(server);
io.sockets.on('connection', function(socket) {
	logger.outer = socket;
	global.logger = logger;
	socket.emit('hello', {
		hello : 'world'
	});
});

var client = zookeeper.createClient('localhost:2181');
client.once('connected', function() {
	console.log('Connected to the zookeeper server');
	global.zkCli = client;
});
client.connect();