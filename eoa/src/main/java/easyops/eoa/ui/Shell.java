package easyops.eoa.ui;

import com.beust.jcommander.JCommander;

import easyops.eoa.ui.arguments.Argument;

public class Shell {
	
	private static Argument argument = new Argument();

	public static void main(String[] args) {
		new JCommander(argument, args);

	}

}
