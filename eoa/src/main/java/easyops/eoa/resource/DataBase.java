package easyops.eoa.resource;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;

public class DataBase extends BaseResource {

	private static final long serialVersionUID = 1L;

	@Expose
	public List<DBDomain> dbList;

	public static DataBase buildDB(String dbParameter) {
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
				.create();
		DataBase db = g.fromJson(dbParameter, new TypeToken<DataBase>() {
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
