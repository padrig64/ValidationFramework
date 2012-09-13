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

package com.github.moawad.validationframework.base.rule;

import java.util.ArrayList;
import java.util.List;

import com.github.moawad.validationframework.api.rule.Rule;

/**
 * Abstraction of a composite rule composed of sub-rules.
 */
public abstract class AbstractCompositeRule<D, R> implements Rule<D, R> {

	/**
	 * Sub-rules to be checked.
	 */
	protected final List<Rule<D, R>> rules = new ArrayList<Rule<D, R>>();

	/**
	 * Default constructor.
	 */
	public AbstractCompositeRule() {
		// Nothing to be done
	}

	/**
	 * Constructor specifying the sub-rule(s) to be added.
	 *
	 * @param rules Sub-rule(s) to be added.
	 *
	 * @see #addRule(Rule)
	 */
	public AbstractCompositeRule(final Rule<D, R>... rules) {
		if (rules != null) {
			for (final Rule<D, R> rule : rules) {
				addRule(rule);
			}
		}
	}

	/**
	 * Adds the specified sub-rule to be checked.
	 *
	 * @param rule Sub-rule to be added.
	 */
	public void addRule(final Rule<D, R> rule) {
		rules.add(rule);
	}

	/**
	 * Removes the specified sub-rule to be checked.
	 *
	 * @param rule Sub-rule tobe removed
	 */
	public void removeRule(final Rule<D, R> rule) {
		rules.remove(rule);
	}
}
