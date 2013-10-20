package easyops.eoa.test.controller;

import easyops.eoa.controller.IDBController;
import easyops.eoa.resource.DBServer;
import easyops.eoa.ui.arguments.Argument;

public class SimuDBController implements IDBController {

	private boolean valid = true;;

	@Override
	public void checkDB() {
		

	}

	@Override
	public boolean isValid() {
		return valid;
	}

	@Override
	public String getMessage() {
		return "Simulate a valid Connection";
	}

	@Override
	public void init(DBServer server, Argument arg) {
		

	}

	public void dbDown() {
		this.valid = false;
	}

}
