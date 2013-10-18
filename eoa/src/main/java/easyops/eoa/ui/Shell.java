package easyops.eoa.ui;

import java.util.concurrent.CountDownLatch;

import com.beust.jcommander.JCommander;

import easyops.eoa.Agent;
import easyops.eoa.ui.arguments.Argument;

public class Shell {

	private static Argument argument = new Argument();
	private static Agent agent;
	private static CountDownLatch latch = new CountDownLatch(1);
	public static RunMode runMode = RunMode.Product;
	

	public static void main(String[] args) {
		new JCommander(argument, args);
		
		try {
			agent = new Agent(argument);
			agent.check();
			agent.start();
			latch.await();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}finally{
			agent.shutdown();
		}

	}

	public static void shutdown() {
		agent.shutdown();
		latch.countDown();

	}

}
