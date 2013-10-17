package easyops.eoa.resource;

public enum DBStatus {
	Running, Down, Active, Active2Down, Running2Down;
	public static String getStatus(DBStatus s) {
		switch (s) {
		case Running:
			return "running";
		case Down:
			return "down";
		case Active:
			return "active";
		case Active2Down:
			return "down";
		case Running2Down:
			return "down";
		default:
			return "";
		}

	}

}
