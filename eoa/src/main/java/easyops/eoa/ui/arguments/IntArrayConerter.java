package easyops.eoa.ui.arguments;

import com.beust.jcommander.IStringConverter;

public class IntArrayConerter implements IStringConverter<int[]> {

	@Override
	public int[] convert(String str) {
		if (str == null) {
			return new int[0];
		} else {
			String[] ss = str.split(",");
			int[] ret = new int[ss.length];
			int i = 0;
			for (String s : ss) {
				ret[i++] = Integer.parseInt(s);
			}
			return ret;
		}
	}

}
