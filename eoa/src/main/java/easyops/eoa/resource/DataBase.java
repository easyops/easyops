package easyops.eoa.resource;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class DataBase extends BaseResource{

	private static final long serialVersionUID = 1L;

	public DataBase(String dbParameter) {
		buildDB(dbParameter);
	}
	
	
	public List<DBDomain> dbList;

	public void buildDB(String dbParameter) {
		Gson g = new Gson();
		dbList = g.fromJson(dbParameter, new TypeToken<List<DBDomain>>() {
		}.getType());
	}
}
