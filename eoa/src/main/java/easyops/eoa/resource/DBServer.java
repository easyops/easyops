package easyops.eoa.resource;

import org.apache.zookeeper.Watcher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class DBServer extends BaseResource {

	private static final long serialVersionUID = 1L;
	@Expose
	public String address;
	@Expose
	public String user;
	@Expose
	public String password;
	@Expose
	public int port;
	@Expose
	public DBRole role = DBRole.SLAVE;
	@Expose
	public DBStatus status = DBStatus.Down;
	@Expose
	public long freezeStamp = 0;
	@Expose
	public long checkInStamp = 0;

	public Watcher lockWatch;

	public String getMark() {
		return address + ":" + port;
	}

	public String getPassword() {
		return password;
	}

	public boolean activeServer() {
		return true;
	}

	public Watcher getLockWatcher() {
		return this.lockWatch;
	}

	public void setLockWatcher(Watcher watcher) {
		this.lockWatch = watcher;
	}

	public static DBServer fromJson(String json) {
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		DBServer server = g.fromJson(json, new TypeToken<DBServer>() {
		}.getType());
		return server;
	}

}
