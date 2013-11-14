package easyops.eoa.ui.arguments;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import easyops.eoa.ui.arguments.validation.CommandValidation;

@Parameters(separators = "=")
public class Argument {
	@Parameter(names = "-command", validateWith = CommandValidation.class)
	public String command;
	@Parameter(names = "-id")
	public String id="agent";
	@Parameter(names = "-zkserver")
	public String zkserver="localhost:2181";
	@Parameter(names = "-db", description = "domain.servername, split by <,>")
	public String db;
	@Parameter(names = "-zkSessionTimeout")
	public int zkSessionTimeout = 1000;
	@Parameter(names = "-dbcheckInterval")
	public int dbCheckInteral=3000;
	@Parameter(names = "-dbCheckMaxTry")
	public int dbCheckMaxTry;
	@Parameter(names = "-failCodes", converter = IntArrayConerter.class, description="1,3,4,5,100-105")
	public int[] failCodes;
	@Parameter(names = "-masterAutoActive")
	public boolean masterAutoActive = false;
	@Parameter(names = "-freezeTime")
	public int freezeTime=10000;
	@Parameter(names = "-dbCheckTimeout")
	public int dbCheckTimeout = 3000;
}
