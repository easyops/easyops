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
	public static String MASTER = "master";
	public static String SERVER_NAME = "serverName";
	public static String HOST = "host";
	public static String PORT = "port";

	@Expose
	public List<DBDomain> dbList;

	public static DataBase buildDB(String dbParameter) {
		if (dbParameter == null || dbParameter.length() == 0) {
			return new DataBase();
		}
		DataBase db = DataBase.fromJson(dbParameter);
		return db;
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
		for (DBDomain domain : dbList) {
			list.addAll(domain.getAllServerList());
		}
		return list;
	}
}
