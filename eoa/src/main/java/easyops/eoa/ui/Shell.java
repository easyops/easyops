package easyops.eoa.ui;

import java.util.concurrent.CountDownLatch;

import com.beust.jcommander.JCommander;

import easyops.eoa.Agent;
import easyops.eoa.controller.DBControllerFactory;
import easyops.eoa.controller.MySQLController;
import easyops.eoa.ui.arguments.Argument;

public class Shell {

	private static Argument argument = new Argument();
	private static Agent agent;
	private static CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {

		try {
			init(args);
			agent = new Agent(argument);
			agent.check();
			agent.start();
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		} finally {
			agent.shutdown();
		}

	}

	public static void init(String[] args) {
		DBControllerFactory.clazz = MySQLController.class;
		new JCommander(argument, args);

	}

	public static void shutdown() {
		agent.shutdown();
		latch.countDown();

	}

}
