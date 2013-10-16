package easyops.eoa.ui.arguments.validation;

import com.beust.jcommander.IParameterValidator;
import com.beust.jcommander.ParameterException;

public class CommandValidation implements IParameterValidator {

	public void validate(String key, String value) throws ParameterException {
		boolean v = false;
		if (value == null) {
			v = false;
		} else if ("start".equals(value) || "version".equals(value)
				|| "check".equals(value)) {
			v = true;
		}
		if (!v) {
			throw new ParameterException("command is'nt validate!, " + value);
		}

	}

}
