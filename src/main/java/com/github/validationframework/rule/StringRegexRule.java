/*
 * Copyright (c) 2012, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.validationframework.rule;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule checking string data against one or several regular expressions and returning a boolean a result.
 *
 * @see TypedDataRule
 * @see Pattern
 */
public class StringRegexRule implements StringBooleanRule {

	/**
	 * Mapping between regex expression string and compiled patterns.
	 */
	private final Map<String, Pattern> patterns = new HashMap<String, Pattern>();

	/**
	 * Default constructor.
	 */
	public StringRegexRule() {
		// Nothing to be done
	}

	/**
	 * Constructor specified a pattern to be added.<br>This constructor is provided for convenience.
	 *
	 * @param pattern Pattern to be added.
	 * @see #addPattern(String)
	 */
	public StringRegexRule(final String pattern) {
		addPattern(pattern);
	}

	/**
	 * Adds the specified regular expression to be matched against the data to be validated.
	 *
	 * @param pattern Regular expression to be added.
	 * @see #addPattern(String, int)
	 */
	public void addPattern(final String pattern) {
		addPattern(pattern, 0);
	}

	/**
	 * Adds the specified regular expression to be matched against the data to be validated, with the specified pattern
	 * flags.
	 *
	 * @param pattern Regular expression to be added.
	 * @param flags Regular expression pattern flags.<br>Refer to {@link Pattern#}
	 * @see #addPattern(String)
	 */
	public void addPattern(final String pattern, final int flags) {
		patterns.put(pattern, Pattern.compile(pattern, flags));
	}

	/**
	 * Removes the specified regular expression.
	 *
	 * @param pattern Regular expression to be removed.
	 */
	public void removePattern(final String pattern) {
		patterns.remove(pattern);
	}

	/**
	 * @see StringBooleanRule#validate(Object)
	 */
	@Override
	public Boolean validate(final String data) {
		Boolean result = false;

		for (final Pattern pattern : patterns.values()) {
			final Matcher matcher = pattern.matcher(data);
			if (matcher.find()) {
				result = true;
			}
		}

		return result;
	}
}
