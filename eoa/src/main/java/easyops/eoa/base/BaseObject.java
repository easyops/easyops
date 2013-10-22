package easyops.eoa.base;

import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class BaseObject{
	public static String CharCode = "UTF-8";
	public byte[] toJsonBytes() {
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		try {
			return g.toJson(this).getBytes(CharCode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	public String toJsonString(){
		Gson g = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		return g.toJson(this);
		
	}
	public ZNode znode;

}
