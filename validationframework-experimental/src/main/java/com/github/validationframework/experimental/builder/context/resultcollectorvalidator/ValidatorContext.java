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

package com.github.validationframework.experimental.builder.context.resultcollectorvalidator;

import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.base.resulthandler.ResultCollector;
import com.github.validationframework.base.validator.ResultCollectorValidator;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Context to add more result handlers and to create the validator.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * type of the component itself.
 * @param <O> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 */
public class ValidatorContext<D, O> {

	final List<ResultCollector<?, D>> registeredResultCollectors;
	final List<Rule<Collection<D>, O>> registeredRules;
	final List<ResultHandler<O>> registeredResultHandlers;

	public ValidatorContext(final List<ResultCollector<?, D>> registeredResultCollectors,
							final List<Rule<Collection<D>, O>> registeredRules,
							final List<ResultHandler<O>> registeredResultHandlers) {
		this.registeredResultCollectors = registeredResultCollectors;
		this.registeredRules = registeredRules;
		this.registeredResultHandlers = registeredResultHandlers;
	}

	public ValidatorContext<D, O> handleResultWith(final ResultHandler<O> resultHandler) {
		if (resultHandler != null) {
			registeredResultHandlers.add(resultHandler);
		}
		return this;
	}

	/**
	 * Adds more result handlers to the validator.
	 *
	 * @param resultHandlers Result handlers to be added.
	 *
	 * @return Same validator context.
	 */
	public ValidatorContext<D, O> handleResultWith(final ResultHandler<O>... resultHandlers) {
		if (resultHandlers != null) {
			Collections.addAll(registeredResultHandlers, resultHandlers);
		}
		return this;
	}

	public ValidatorContext<D, O> handleResultWith(final Collection<ResultHandler<O>> resultHandlers) {
		if (resultHandlers != null) {
			registeredResultHandlers.addAll(resultHandlers);
		}
		return this;
	}

	/**
	 * Creates the validator and makes all the connections.
	 *
	 * @return Newly created and configured validator.
	 */
	public ResultCollectorValidator<D, O> build() {
		final ResultCollectorValidator<D, O> validator = new ResultCollectorValidator<D, O>();

		for (final ResultCollector<?, D> resultCollector : registeredResultCollectors) {
			validator.addResultCollector(resultCollector);
		}
		for (final Rule<Collection<D>, O> rule : registeredRules) {
			validator.addRule(rule);
		}
		for (final ResultHandler<O> resultHandler : registeredResultHandlers) {
			validator.addResultHandler(resultHandler);
		}

		return validator;
	}
}
