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

import java.util.ArrayList;
import java.util.List;

/**
 * Composite rule performing validation using sub-rules, and returning a boolean as an aggregation of the boolean
 * results from its sub-rules.
 *
 * @see UntypedDataRule
 * @see CompositeTypedDataBooleanRule
 */
public class CompositeUntypedDataBooleanRule implements UntypedDataRule<Boolean> {

	/**
	 * Untyped data sub-rules to be checked.
	 */
	private final List<UntypedDataRule<Boolean>> rules = new ArrayList<UntypedDataRule<Boolean>>();

	/**
	 * Default constructor.
	 */
	public CompositeUntypedDataBooleanRule() {
		// Nothing to be done
	}

	/**
	 * Constructor specifying the first two rules to be added to the composite rule.<br>It is provided for convenience.
	 *
	 * @param firstRule First rule to be added.
	 * @param secondRule Second rule to be added.
	 * @see #addRule(UntypedDataRule)
	 */
	public CompositeUntypedDataBooleanRule(final UntypedDataRule<Boolean> firstRule,
										   final UntypedDataRule<Boolean> secondRule) {
		addRule(firstRule);
		addRule(secondRule);
	}

	/**
	 * Adds the specified sub-rule to be checked.<br>Note that the sub-rules will be checked in the same order as they are
	 * added.
	 *
	 * @param rule Sub-rule to be added.
	 */
	public void addRule(final UntypedDataRule<Boolean> rule) {
		rules.add(rule);
	}

	/**
	 * Removes the specified sub-rule to be checked.
	 *
	 * @param rule Sub-rule tobe removed
	 */
	public void removeRule(final UntypedDataRule<Boolean> rule) {
		rules.remove(rule);
	}

	/**
	 * @see UntypedDataRule#validate()
	 */
	@Override
	public Boolean validate() {
		Boolean result = true;

		for (final UntypedDataRule<Boolean> rule : rules) {
			result &= rule.validate();
		}

		return result;
	}
}
