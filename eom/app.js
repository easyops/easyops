
/**
 * Module dependencies.
 */

var express = require('express');
var root = require('./lib/routes');
var filter = require("./lib/filter");
var http = require('http');
var path = require('path');

var app = express();

// all environments
app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/views');
app.set('view engine', 'ejs');
app.use(express.favicon());
app.use(express.logger('dev'));
app.use(express.bodyParser());
app.use(express.methodOverride());
app.use(express.cookieParser('%Qwert13579#'));
app.use(express.session());
app.use(filter.authFilter);
app.use(app.router);
app.use(express.static(path.join(__dirname, 'public')));


// development only
if ('development' == app.get('env')) {
    app.use(express.errorHandler());
}

app.get('/', root.index);
app.get('/login', root.login);
app.get('/doLogin', root.doLogin);
app.get('/doLogout', root.doLogout);
app.post('/doLogin', root.doLogin);


http.createServer(app).listen(app.get('port'), function(){
    console.log('Express server listening on port ' + app.get('port'));
});
