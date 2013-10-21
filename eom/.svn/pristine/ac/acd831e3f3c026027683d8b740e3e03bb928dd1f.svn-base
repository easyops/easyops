/**
 * Created with JetBrains WebStorm.
 * User: tintin
 * Date: 13-8-21
 * Time: 上午10:23
 * To change this template use File | Settings | File Templates.
 */
exports.getAllDomain = function () {
    var retList = new Array();
    var d = new Domain();
    d.id = 1;
    d.name = "计费域";
    var h = new Host();
    h.id = 1;
    h.name = "jfapp1";
    h.ip = "10.10.10.100";
    d.addHost(h);
    d.addHost(h);
    d.addHost(h);
    retList.push(d);
    retList.push(d);
    retList.push(d);
    return retList;
}

var Domain = function () {
    this.id="";
    this.name="";
   this.hostList = new Array();
    this.getHosts = function(){
        return this.hostList;
    }
    this.addHost = function(host){
        this.hostList.push(host);
    }
}

var Host = function () {
    this.ip = "";
    this.name = "";
    this.note = "";
    this.domain = "";
    this.status="";
}
