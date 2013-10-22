package easyops.eoa.resource;

import java.util.List;

import com.google.gson.annotations.Expose;

import easyops.eoa.base.BaseObject;

public class DBPartition extends BaseObject{

	@Expose
	public String partitionId;
	@Expose
	public List<DBServer> serverlist;
}
