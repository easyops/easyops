package easyops.eoa.controller;

import easyops.eoa.resource.DBServer;

public class BaseDBController {
	public DBServer server;
	boolean valid;
	private String message;

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public DBServer getServer() {
		return server;
	}
}
