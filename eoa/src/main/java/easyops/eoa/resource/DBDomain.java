package easyops.eoa.resource;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

import easyops.eoa.base.BaseObject;

public class DBDomain extends BaseObject {


	@Expose
	public String name;
	@Expose
	public boolean isPartition = false;
	@Expose
	public List<DBPartition> partitionList;
	@Expose
	public List<DBServer> serverList;

	public List<DBServer> getSlaveServerList() {
		List<DBServer> retList = new ArrayList<DBServer>();
		if (isPartition) {
			for (DBPartition p : partitionList) {
				for (DBServer s : p.serverlist) {
					if (s.role == DBRole.SLAVE) {
						retList.add(s);
					}
				}
			}
		} else {
			for (DBServer s : serverList) {
				if (s.role == DBRole.SLAVE) {
					retList.add(s);
				}
			}
		}
		return retList;
	}

	public List<DBServer> getAllServerList() {
		List<DBServer> retList = new ArrayList<DBServer>();
		if (isPartition) {
			for (DBPartition p : partitionList) {
				for (DBServer s : p.serverlist) {
					retList.add(s);
				}
			}
		} else {
			for (DBServer s : this.serverList) {
				retList.add(s);
			}
		}
		return retList;
	}


}
