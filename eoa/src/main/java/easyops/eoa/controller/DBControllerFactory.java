package easyops.eoa.controller;

import easyops.eoa.ui.RunMode;
import easyops.eoa.ui.Shell;

public class DBControllerFactory {
	public static IDBController getController(){
		if(Shell.runMode == RunMode.Product){
			return new MySQLController();
		}else{
			return new SimuDBController();
		}
	}
}
