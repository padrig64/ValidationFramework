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

package com.github.validationframework.base.validator;

import com.github.validationframework.api.dataprovider.TypedDataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.base.resulthandler.ResultCollector;
import java.util.ArrayList;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO
 *
 * @param <D> Type of data handled by this validator, that is to say, the transformed collected results.
 * @param <O> Typed of output of this validator.
 */
public class ResultCollectorValidator<D, O> extends
		AbstractSimpleValidator<Trigger, TypedDataProvider<D>, Collection<D>, O, Rule<Collection<D>, O>, ResultHandler<O>> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(ResultCollectorValidator.class);

	public void addResultCollector(final ResultCollector<?, D> resultCollector) {
		addTrigger(resultCollector);
		addDataProvider(resultCollector);
	}

	public void removeResultCollector(final ResultCollector<?, D> resultCollector) {
		removeTrigger(resultCollector);
		removeDataProvider(resultCollector);
	}

	/**
	 * @see AbstractSimpleValidator#processTrigger(Trigger)
	 */
	@Override
	protected void processTrigger(final Trigger trigger) {
		if (dataProviders.isEmpty()) {
			LOGGER.warn("No data providers in validator: " + this);
		} else {
			// Collect results
			final Collection<D> collectedResults = new ArrayList<D>();
			for (final TypedDataProvider<D> dataProvider : dataProviders) {
				collectedResults.add(dataProvider.getData());
			}

			// Process results
			processData(collectedResults);
		}
	}

	/**
	 * Validates the specified data all rules.
	 *
	 * @param data Data to be validated against all rules.
	 */
	protected void processData(final Collection<D> data) {
		// Check data against all rules
		for (final Rule<Collection<D>, O> rule : rules) {
			processResult(rule.validate(data));
		}
	}

	/**
	 * Handles the specified result using all result handlers.
	 *
	 * @param result Result to be processed by all result handlers.
	 */
	protected void processResult(final O result) {
		for (final ResultHandler<O> resultHandler : resultHandlers) {
			resultHandler.handleResult(result);
		}
	}
}
