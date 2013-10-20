package easyops.eoa.test.controller;

import easyops.eoa.controller.BaseDBController;
import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBServer;
import easyops.eoa.ui.arguments.Argument;

public class SimuDBController extends BaseDBController implements IDBController {

	@Override
	public void checkDB() {

	}

	@Override
	public String getMessage() {
		return "Simulate a valid Connection";
	}

	@Override
	public void init(DBServer server, Argument arg) {
		setValid(true);
		this.server = server;

	}

	public void shutDownDB() {
		setValid(false);
	}

}
