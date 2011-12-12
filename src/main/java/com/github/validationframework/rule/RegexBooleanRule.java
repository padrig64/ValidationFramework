package com.github.validationframework.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBooleanRule implements Rule<String, Boolean> {

	private Map<String, Integer> patterns = new HashMap<String, Integer>();

	public void addPattern(String pattern) {
		addPattern(pattern, 0);
	}

	public void addPattern(String pattern, int flags) {
		patterns.put(pattern, flags);
	}

	public void removeRegex(String pattern) {
		patterns.remove(pattern);
	}

	@Override
	public Boolean validate(String data) {
		Boolean result = false;

		for (Map.Entry<String, Integer> patternEntry : patterns.entrySet()) {
			Pattern pattern = Pattern.compile(patternEntry.getKey(), patternEntry.getValue());
			Matcher matcher = pattern.matcher(data);
			if (matcher.find()) {
				result = true;
			}
		}

		return result;
	}
}
