
/*
 * GET home page.
 */

var host = require("../bizz/host");

exports.index = function(req, res){
  var hostList = host.getAllDomain();
    var h = hostList[0].getHosts()[0];
    console.log(h);
  res.render('index', { domains: hostList });

};