package easyops.eoa.controller;

import easyops.eoa.resource.DBServer;
import easyops.eoa.ui.arguments.Argument;

public interface IDBController {
	public void checkDB();
	public boolean isValid();
	public String getMessage();
	public void init(DBServer server, Argument arg);
}
