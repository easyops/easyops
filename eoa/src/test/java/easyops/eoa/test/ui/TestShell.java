package easyops.eoa.test.ui;

import static org.junit.Assert.*;

import org.junit.Test;

import com.beust.jcommander.JCommander;

import easyops.eoa.ui.arguments.Argument;

public class TestShell {

	@Test
	public void testVersion() {
		String[] args = {"-command=version"};
		Argument a = new Argument();
		new JCommander(a,args);
		assertTrue("read version parameter success!", a.command.equals("version"));
	}

}
