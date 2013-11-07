package easyops.eoa.resource;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

import easyops.eoa.base.BaseObject;

public class DataBase extends BaseObject {

	public static String STATUS = "status";
	public static String CHECK_IN_STAMP = "check_in_stamp";
	public static String DBSERVER_LIST = "servers";
	public static String ACTIVE_LOCK = "lock";
	public static String ACTIVE_NODE = "active_server";

	@Expose
	public List<DBDomain> domainList = new ArrayList<DBDomain>();

	public static DataBase buildDB(String dbParameter) {
		try {
			if (dbParameter == null || dbParameter.length() == 0) {
				return new DataBase();
			}
			String[] ss = dbParameter.split(",");
			DataBase db = new DataBase();
			for (String s : ss) {
				s = s.trim();
				String[] xx = s.split("\\.");				
				DBDomain domain = db.getDomain(xx[0]);
				if(domain==null){
					domain = new DBDomain();
					domain.name = xx[0];
					db.domainList.add(domain);
				}
				DBServer server = domain.getServer(xx[1]);
				if(server == null){
					server = new DBServer();
					server.serverName = xx[1];
					domain.serverList.add(server);
				}
				
			}
			return db;
		} catch (Throwable e) {
			e.printStackTrace();			
			return null;
		}

	}

	private DBDomain getDomain(String name) {
		for(DBDomain domain : domainList){
			if(domain.name.equals(name)){
				return domain;
			}
		}
		return null;
	}

	public static DataBase fromJson(String json) {
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		DataBase db = g.fromJson(json, new TypeToken<DataBase>() {
		}.getType());
		return db;
	}

	public List<DBServer> getAllServerList() {
		List<DBServer> list = new ArrayList<DBServer>();
		for (DBDomain domain : domainList) {
			list.addAll(domain.getAllServerList());
		}
		return list;
	}
}
