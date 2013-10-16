package easyops.eoa.ui.arguments;

import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import easyops.eoa.ui.arguments.validation.CommandValidation;

@Parameters(separators = "=")
public class Argument {
	@Parameter(names = "-command", validateWith = CommandValidation.class)
	public String command;
	@Parameter(names = "-id")
	public String id;
	@Parameter(names = "-zkserver")
	public String zkserver;
	@Parameter(names = "-db")
	public String db;
	@Parameter(names = "-zkSessionTimeout")
	public int zkSessionTimeout;
	@Parameter(names = "-dbcheckInterval")
	public int dbCheckInteral;

	public List<DBDomain> dbList;

	public void rebuild() {
		Gson g = new Gson();
		dbList = g.fromJson(db, new TypeToken<List<DBDomain>>() {
		}.getType());
	}
}
