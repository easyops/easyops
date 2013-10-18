package easyops.eoa.controller;

import easyops.eoa.resource.DBServer;
import easyops.eoa.ui.arguments.Argument;

public class SimuDBController implements IDBController {
	
	private boolean valid = true;;

	@Override
	public void checkDB() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isValid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(DBServer server, Argument arg) {
		// TODO Auto-generated method stub

	}

}
