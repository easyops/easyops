package easyops.eoa.controller;


public class DBControllerFactory {
	
	public static Class<?> clazz;
	
	public static IDBController getController(){
		try {
			return  (IDBController)clazz.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		return null;
	}
}
