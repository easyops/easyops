package easyops.eoa.resource;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BaseResource implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String CharCode = "UTF-8";
	
	public ZNode znode;
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
	

}
