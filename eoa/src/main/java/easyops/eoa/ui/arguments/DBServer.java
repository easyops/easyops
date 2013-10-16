package easyops.eoa.ui.arguments;

public class DBServer {
	public String address;
	public String user;
	public String password;
	public int port;
	public String getMark(){
		return address + ":" + port;
	}
}
