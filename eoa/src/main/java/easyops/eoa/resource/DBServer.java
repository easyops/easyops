package easyops.eoa.resource;


public class DBServer  extends BaseResource{

	private static final long serialVersionUID = 1L;
	public String address;
	public String user;
	public String password;
	public int port;
	public DBRole role = DBRole.SLAVE;

	public String getMark(){
		return address + ":" + port;
	}
	public String getPassword(){
		return password;
	}
}
