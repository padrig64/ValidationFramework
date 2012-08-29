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

package com.github.validationframework.experimental.validator;

import com.github.validationframework.api.dataprovider.TypedDataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.TypedDataRule;
import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.base.validator.AbstractSimpleValidator;
import com.github.validationframework.experimental.transform.Transformer;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @deprecated Hard to use?
 */
@Deprecated
public class TypedDataAggregateResultValidator<D, O, A>
		extends AbstractSimpleValidator<Trigger, TypedDataProvider<D>, TypedDataRule<D, O>, A, ResultHandler<A>> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TypedDataAggregateResultValidator.class);

	private Transformer<Collection<O>, A> aggregator = null;

	public TypedDataAggregateResultValidator() {
		super();
	}

	public TypedDataAggregateResultValidator(final Transformer<Collection<O>, A> aggregator) {
		super();
		this.aggregator = aggregator;
	}

	/**
	 * @see AbstractSimpleValidator#processTrigger(Trigger)
	 */
	@Override
	protected void processTrigger(final Trigger trigger) {
		if (dataProviders.isEmpty()) {
			LOGGER.warn("No data providers in validator: " + this);
		} else {
			// Process data from all providers
			for (final TypedDataProvider<D> dataProvider : dataProviders) {
				processData(dataProvider.getData());
			}
		}
	}

	/**
	 * Validates the specified data all rules.
	 *
	 * @param data Data to be validated against all rules.
	 */
	protected void processData(final D data) {
		if (!rules.isEmpty()) {
			// Check data against all rules and collect results
			final Collection<O> results = new ArrayList<O>();
			for (final TypedDataRule<D, O> rule : rules) {
				results.add(rule.validate(data));
			}

			// Aggregate results and process aggregated result
			final A aggregatedResult = aggregator.transform(results);
			processResult(aggregatedResult);
		}
	}

	/**
	 * Handles the specified result using all result handlers.
	 *
	 * @param result Result to be processed by all result handlers.
	 */
	protected void processResult(final A result) {
		for (final ResultHandler<A> resultHandler : resultHandlers) {
			resultHandler.handleResult(result);
		}
	}
}