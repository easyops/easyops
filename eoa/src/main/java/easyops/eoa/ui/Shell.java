package easyops.eoa.ui;

import com.beust.jcommander.JCommander;

import easyops.eoa.Agent;
import easyops.eoa.ui.arguments.Argument;

public class Shell {

	private static Argument argument = new Argument();
	private static Agent agent;

	private static class ShellRunTime implements Runnable {

		@Override
		public void run() {
			synchronized (Shell.srt) {
				try {
					Shell.srt.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	private static ShellRunTime srt = new ShellRunTime();

	public static void main(String[] args) {
		new JCommander(argument, args);
		Thread st = new Thread(srt);
		st.start();
		try {
			agent = new Agent(argument);
			agent.check();
			agent.start();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void shutdown() {
		agent.shutdown();
		synchronized (srt) {
			srt.notify();
		}

	}

}
