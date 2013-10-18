package easyops.eoa.ui.arguments;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import easyops.eoa.ui.arguments.validation.CommandValidation;

@Parameters(separators = "=")
public class Argument {
	@Parameter(names = "-command", validateWith = CommandValidation.class)
	public String command;
	@Parameter(names = "-id")
	public String id;
	@Parameter(names = "-zkserver")
	public String zkserver;
	@Parameter(names = "-db", description = "{\"dbList\":[{\"name\":\"basedb\",\"isPartition\":false,\"serverList\":[{\"address\":\"10.10.10.10\",\"user\":\"dbmonitor\",\"password\":\"dbmonitor123\",\"port\":5000,\"role\":\"MASTER\",\"status\":\"Down\",\"freezeStamp\":0}]}]}")
	public String db;
	@Parameter(names = "-zkSessionTimeout")
	public int zkSessionTimeout;
	@Parameter(names = "-dbcheckInterval")
	public int dbCheckInteral;
	@Parameter(names = "-dbCheckMaxTry")
	public int dbCheckMaxTry;
	@Parameter(names = "-failCodes", converter = IntArrayConerter.class)
	public int[] failCodes;
	@Parameter(names = "-masterAutoActive")
	public boolean masterAutoActive = false;
	@Parameter(names = "-freezeTime")
	public int freezeTime;
}
