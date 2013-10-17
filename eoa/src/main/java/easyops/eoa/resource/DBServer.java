package easyops.eoa.resource;

import org.apache.zookeeper.Watcher;

import com.google.gson.annotations.Expose;


public class DBServer  extends BaseResource{

	private static final long serialVersionUID = 1L;
	public String address;
	public String user;
	public String password;
	public int port;
	public DBRole role = DBRole.SLAVE;
	public DBStatus status = DBStatus.Down;
	public long freezeStamp = 0;
	@Expose
	public Watcher lockWatch;
	
	public String getMark(){
		return address + ":" + port;
	}
	public String getPassword(){
		return password;
	}
	public boolean activeServer() {
		return true;
	}
	public Watcher getLockWatcher(){
		return this.lockWatch;
	}
	public void setLockWatcher( Watcher watcher){
		this.lockWatch = watcher;
	}
}
