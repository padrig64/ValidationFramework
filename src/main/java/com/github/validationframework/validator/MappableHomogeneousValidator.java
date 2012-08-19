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

package com.github.validationframework.validator;

import com.github.validationframework.dataprovider.TypedDataProvider;
import com.github.validationframework.resulthandler.TypedResultHandler;
import com.github.validationframework.rule.TypedDataRule;
import com.github.validationframework.trigger.Trigger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MappableHomogeneousValidator<D, R> extends AbstractHomogeneousValidator<D, R> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MappableHomogeneousValidator.class);

	private final Map<Trigger, List<TypedDataProvider<D>>> triggersToDataProviders =
			new HashMap<Trigger, List<TypedDataProvider<D>>>();

	private final Map<TypedDataProvider<?>, List<TypedDataRule<D, R>>> dataProvidersToRules =
			new HashMap<TypedDataProvider<?>, List<TypedDataRule<D, R>>>();

	private final Map<R, List<TypedResultHandler<R>>> resultsToResultHandlers =
			new HashMap<R, List<TypedResultHandler<R>>>();

	public void map(final Trigger trigger, final TypedDataProvider<D> dataProvider) {
		List<TypedDataProvider<D>> mappedDataProviders = triggersToDataProviders.get(trigger);
		if (mappedDataProviders == null) {
			mappedDataProviders = new ArrayList<TypedDataProvider<D>>();
			triggersToDataProviders.put(trigger, mappedDataProviders);
		}
		mappedDataProviders.add(dataProvider);
	}

	public void map(final TypedDataProvider<D> dataProvider, final TypedDataRule<D, R> rule) {
		List<TypedDataRule<D, R>> mappedRules = dataProvidersToRules.get(dataProvider);
		if (mappedRules == null) {
			mappedRules = new ArrayList<TypedDataRule<D, R>>();
			dataProvidersToRules.put(dataProvider, mappedRules);
		}
		mappedRules.add(rule);
	}

	public void map(final R result, final TypedResultHandler<R> resultHandler) {
		List<TypedResultHandler<R>> mappedResultHandlers = resultsToResultHandlers.get(result);
		if (mappedResultHandlers == null) {
			mappedResultHandlers = new ArrayList<TypedResultHandler<R>>();
			resultsToResultHandlers.put(result, mappedResultHandlers);
		}
		mappedResultHandlers.add(resultHandler);
	}

	@Override
	public void processTrigger(final Trigger trigger) {
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

	private void processResult(final R result) {
		// Get result handlers matching the result
		final List<TypedResultHandler<R>> mappedResultHandlers = resultsToResultHandlers.get(result);
		if ((mappedResultHandlers == null) || mappedResultHandlers.isEmpty()) {
			LOGGER.warn("No matching result handler in mappable validator for result: " + result);
		} else {
			// Process all matching result handlers
			for (final TypedResultHandler<R> resultHandler : mappedResultHandlers) {
				resultHandler.handleResult(result);
			}
		}
	}
}
