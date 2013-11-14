package easyops.eoa.ui.arguments;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.beust.jcommander.IStringConverter;

public class IntArrayConerter implements IStringConverter<int[]> {
	Logger log = Logger.getLogger(IntArrayConerter.class);

	@Override
	public int[] convert(String str) {
		List<Integer> list = new ArrayList<Integer>();
		if (str == null) {
			return new int[0];
		} else {
			String[] ss = str.split(",");
			String[] xx;
			int left, right;
			for (String s : ss) {
				if (s.indexOf('-') > 0) {
					xx = s.split("-");
					try {
						left = Integer.parseInt(xx[0]);
						right = Integer.parseInt(xx[1]);
						for (int i = left; i <= right; i++) {
							list.add(i);
						}
					} catch (Exception e) {
						log.error("parse error:" + s, e);
						throw new RuntimeException(e);
					}
				} else {
					list.add(Integer.parseInt(s));
				}
			}
			int[] ret = new int[list.size()];
			int i = 0;
			for (Integer li : list) {
				ret[i++] = li.intValue();
			}
			return ret;
		}
	}

}
