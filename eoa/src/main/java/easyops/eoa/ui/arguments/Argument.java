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
	@Parameter(names = "-db")
	public String db;
	@Parameter(names = "-zkSessionTimeout")
	public int zkSessionTimeout;
	@Parameter(names = "-dbcheckInterval")
	public int dbCheckInteral;
	@Parameter(names = "-dbCheckMaxTry")
	public int dbCheckMaxTry;
	@Parameter(names="failCodes")
	public int[] failCodes;
	@Parameter(names="masterAutoActive")
	public boolean masterAutoActive;
	@Parameter(names="freezeTime")
	public int freezeTime;
}
