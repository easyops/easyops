package easyops.eoa.test.controller;

import easyops.eoa.controller.BaseDBController;
import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBServer;
import easyops.eoa.ui.arguments.Argument;

public class SimuDBController extends BaseDBController implements IDBController {

	
	public boolean activeResult = true;
	
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
	@Override
	public void shutDownDB() {
		setValid(false);
	}

	@Override
	public void startDB() {
		setValid(true);
		
	}

	@Override
	public boolean activeDB() {

		return activeResult;
	}

}
