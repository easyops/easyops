package easyops.eoa.resource;

import java.util.List;

import com.google.gson.annotations.Expose;

public class DBPartition extends BaseResource{

	private static final long serialVersionUID = 1L;
	public String partitionId;
	@Expose
	public List<DBServer> serverlist;
}
