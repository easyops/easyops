package easyops.eoa.resource;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;

public class BaseResource implements Serializable {

	private static final long serialVersionUID = 1L;
	public static String CharCode = "UTF-8";
	@Expose
	public ZNode znode;
	public byte[] toJsonBytes() {
		Gson g = new Gson();
		try {
			return g.toJson(this).getBytes(CharCode);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}
	

}
