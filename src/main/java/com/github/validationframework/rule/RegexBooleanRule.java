package com.github.validationframework.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexBooleanRule implements Rule<String, Boolean> {

	private final Map<String, Integer> patterns = new HashMap<String, Integer>();

	public void addPattern(final String pattern) {
		addPattern(pattern, 0);
	}

	public void addPattern(final String pattern, final int flags) {
		patterns.put(pattern, flags);
	}

	public void removeRegex(final String pattern) {
		patterns.remove(pattern);
	}

	@Override
	public Boolean validate(final String data) {
		Boolean result = false;

		for (final Map.Entry<String, Integer> patternEntry : patterns.entrySet()) {
			final Pattern pattern = Pattern.compile(patternEntry.getKey(), patternEntry.getValue());
			final Matcher matcher = pattern.matcher(data);
			if (matcher.find()) {
				result = true;
			}
		}

		return result;
	}
}
