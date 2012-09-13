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

package com.github.moawad.validationframework.experimental.builder.context.resultcollectorvalidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.github.moawad.validationframework.api.rule.Rule;
import com.github.moawad.validationframework.base.resulthandler.ResultCollector;

public class RuleContext<D> {

	final List<ResultCollector<?, D>> registeredResultCollectors;

	public RuleContext(final List<ResultCollector<?, D>> registeredResultCollectors) {
		this.registeredResultCollectors = registeredResultCollectors;
	}

	public RuleContext<D> collect(final ResultCollector<?, D> resultCollector) {
		if (resultCollector != null) {
			registeredResultCollectors.add(resultCollector);
		}
		return this;
	}

	public RuleContext<D> collect(final ResultCollector<?, D>... resultCollectors) {
		if (resultCollectors != null) {
			Collections.addAll(registeredResultCollectors, resultCollectors);
		}
		return this;
	}

	public RuleContext<D> collect(final Collection<ResultCollector<?, D>> resultCollectors) {
		if (resultCollectors != null) {
			registeredResultCollectors.addAll(resultCollectors);
		}
		return this;
	}

	public <O> ResultHandlerContext<D, O> check(final Rule<Collection<D>, O> rule) {
		final List<Rule<Collection<D>, O>> registeredRules = new ArrayList<Rule<Collection<D>, O>>();
		if (rule != null) {
			registeredRules.add(rule);
		}
		return new ResultHandlerContext<D, O>(registeredResultCollectors, registeredRules);
	}

	public <O> ResultHandlerContext<D, O> check(final Rule<Collection<D>, O>... rules) {
		final List<Rule<Collection<D>, O>> registeredRules = new ArrayList<Rule<Collection<D>, O>>();
		if (rules != null) {
			Collections.addAll(registeredRules, rules);
		}
		return new ResultHandlerContext<D, O>(registeredResultCollectors, registeredRules);
	}

	public <O> ResultHandlerContext<D, O> check(final Collection<Rule<Collection<D>, O>> rules) {
		final List<Rule<Collection<D>, O>> registeredRules = new ArrayList<Rule<Collection<D>, O>>();
		if (rules != null) {
			registeredRules.addAll(rules);
		}
		return new ResultHandlerContext<D, O>(registeredResultCollectors, registeredRules);
	}
}
