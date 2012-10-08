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

package com.github.validationframework.base.rule.string;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Rule checking string data against one or several regular expressions and returning a boolean as a result.<br>The
 * result will be valid if the data matches at least one of the patterns (OR operation).<br>Note that the validation is
 * based on the method {@link Matcher#find()} and not {@link Matcher#matches()}. As a result, if you need the matching
 * to be done strictly on the whole input data, you should surround the patterns with the '^' and '$' characters.
 *
 * @see AbstractStringBooleanRule
 * @see Pattern
 * @see Matcher
 */
public class StringRegexRule extends AbstractStringBooleanRule {

	/**
	 * Mapping between regex expression string and compiled patterns.
	 */
	private final Map<String, Pattern> patterns = new HashMap<String, Pattern>();

	/**
	 * Default constructor.
	 */
	public StringRegexRule() {
		super();
	}

	/**
	 * Constructor specifying the pattern(s) to be added.<br>Note that if you need the matching to be done strictly on the
	 * whole input data, you should surround the patterns with the '^' and '$' characters.
	 *
	 * @param patterns Patterns to be added.
	 *
	 * @see #addPattern(String)
	 * @see Matcher#find()
	 */
	public StringRegexRule(final String... patterns) {
		super();
		if (patterns != null) {
			for (final String pattern : patterns) {
				addPattern(pattern);
			}
		}
	}

	/**
	 * Adds the specified regular expression to be matched against the data to be validated.<br>Note that if you need the
	 * matching to be done strictly on the whole input data, you should surround the pattern with the '^' and '$'
	 * characters.
	 *
	 * @param pattern Regular expression to be added.
	 *
	 * @see #addPattern(String, int)
	 * @see Matcher#find()
	 */
	public void addPattern(final String pattern) {
		addPattern(pattern, 0);
	}

	/**
	 * Adds the specified regular expression to be matched against the data to be validated, with the specified pattern
	 * flags.<br>Note that if you need the matching to be done strictly on the whole input data, you should surround the
	 * patterns with the '^' and '$' characters.
	 *
	 * @param pattern Regular expression to be added.
	 * @param flags Regular expression pattern flags.<br>Refer to {@link Pattern#}.
	 *
	 * @see #addPattern(String)
	 * @see Matcher#find()
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
	 * @see AbstractStringBooleanRule#validate(Object)
	 * @see Matcher#find()
	 */
	@Override
	public Boolean validate(final String data) {
		Boolean result = false;

		if (data != null) {
			final String dataToBeValidated = trimIfNeeded(data);
			for (final Pattern pattern : patterns.values()) {
				final Matcher matcher = pattern.matcher(dataToBeValidated);
				if (matcher.find()) {
					result = true;
					break;
				}
			}
		}

		return result;
	}
}
