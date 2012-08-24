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
import com.github.validationframework.api.rule.TypedDataRule;
import com.github.validationframework.api.trigger.Trigger;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete implementation of a typed data mappable validator.<br>A typed data validator is a validator whose data
 * providers and rules are bound to a known specific type of data, and whose result handlers are bound to a known
 * specific type of result.<br>When any of its triggers is initiated, the typed data mappable validator will read all
 * the data from the data providers mapped to that trigger, check the data against the rules mapped to those data
 * providers, and handles the rule results using the result handlers that are mapped to them.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * type of the component itself.
 * @param <R> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 *
 * @see AbstractMappableValidator
 * @see Trigger
 * @see TypedDataProvider
 * @see TypedDataRule
 * @see ResultHandler
 */
public class TypedDataMappableValidator<D, R>
		extends AbstractMappableValidator<Trigger, TypedDataProvider<D>, TypedDataRule<D, R>, R, ResultHandler<R>> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TypedDataMappableValidator.class);

	/**
	 * Processes the specified trigger by finding all the mapped data providers, and so on.
	 *
	 * @see AbstractMappableValidator#processTrigger(Trigger)
	 */
	@Override
	protected void processTrigger(final Trigger trigger) {
		// Get data providers matching the trigger
		final List<TypedDataProvider<D>> mappedDataProviders = triggersToDataProviders.get(trigger);
		if ((mappedDataProviders == null) || mappedDataProviders.isEmpty()) {
			LOGGER.warn("No matching data provider in mappable validator for trigger: " + trigger);
		} else {
			// Process all matching data providers
			for (final TypedDataProvider<D> dataProvider : mappedDataProviders) {
				processDataProvider(dataProvider);
			}
		}
	}

	/**
	 * Process the specified data provider by finding all the mapped rules, and so on.
	 *
	 * @param dataProvider Data provider to be processed.
	 */
	private void processDataProvider(final TypedDataProvider<D> dataProvider) {
		// Get rules matching the data provider
		final List<TypedDataRule<D, R>> mappedRules = dataProvidersToRules.get(dataProvider);
		if ((mappedRules == null) || mappedRules.isEmpty()) {
			LOGGER.warn("No matching rule in mappable validator for data provider: " + dataProvider);
		} else {
			// Get data to be validated
			final D data = dataProvider.getData();

			// Process all matching data providers
			for (final TypedDataRule<D, R> rule : mappedRules) {
				// Check rule and process result
				final R result = rule.validate(data);
				processResult(result);
			}
		}
	}

	/**
	 * Processes the specified result by finding all the mapped results handlers.
	 *
	 * @param result Result to be processed.
	 */
	private void processResult(final R result) {
		// Get result handlers matching the result
		final List<ResultHandler<R>> mappedResultHandlers = resultsToResultHandlers.get(result);
		if ((mappedResultHandlers == null) || mappedResultHandlers.isEmpty()) {
			LOGGER.warn("No matching result handler in mappable validator for result: " + result);
		} else {
			// Process all matching result handlers
			for (final ResultHandler<R> resultHandler : mappedResultHandlers) {
				resultHandler.handleResult(result);
			}
		}
	}
}
