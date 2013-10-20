package easyops.eoa.controller;


public class DBControllerFactory {
	
	public static Class<?> clazz;
	
	public static IDBController getController(){
		try {
			return  (IDBController)clazz.newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
}
