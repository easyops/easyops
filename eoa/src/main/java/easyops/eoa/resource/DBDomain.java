package easyops.eoa.resource;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class DBDomain extends BaseResource {

	private static final long serialVersionUID = 1L;
	@Expose
	public String name;
	@Expose
	public boolean isPartition = false;
	@Expose
	public List<DBPartition> partitionList;
	@Expose
	public List<DBServer> serverList;

	public List<DBServer> getSlaveServerList() {
		List<DBServer> serverList = new ArrayList<DBServer>();
		if (isPartition) {
			for (DBPartition p : partitionList) {
				for (DBServer s : p.serverlist) {
					if (s.role == DBRole.SLAVE) {
						serverList.add(s);
					}
				}
			}
		} else {
			for (DBServer s : serverList) {
				if (s.role == DBRole.SLAVE) {
					serverList.add(s);
				}
			}
		}
		return serverList;
	}

	public List<DBServer> getAllServerList() {
		List<DBServer> serverList = new ArrayList<DBServer>();
		if (isPartition) {
			for (DBPartition p : partitionList) {
				for (DBServer s : p.serverlist) {
					serverList.add(s);
				}
			}
		} else {
			for (DBServer s : serverList) {
				serverList.add(s);
			}
		}
		return serverList;
	}


}
