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
import com.github.validationframework.resulthandler.ResultHandler;
import com.github.validationframework.rule.TypedDataRule;
import com.github.validationframework.trigger.Trigger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple implementation of a homogeneous validator.<br>When any of its triggers is initiated, the simple homogeneous
 * validator will read all the data from all of its data providers, check them all against all of its rules, and handles
 * all the results using all of its result handlers.
 *

 */

/**
 * Homogeneous validator allowing to map triggers to data providers, data providers to rules, and results to result
 * handlers.<br>Whenever a trigger is initiated, all the data providers mapped to it will be used to retrieve data. The
 * data read from the data providers will be check only with the rules that are individually mapped to the data
 * providers. The result will be processed only with the result handlers that are individually mapped the result.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * type of the component itself.
 * @param <R> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 * @see AbstractHomogeneousValidator
 */
public class MappableHomogeneousValidator<D, R> extends AbstractHomogeneousValidator<D, R> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(MappableHomogeneousValidator.class);

	/**
	 * Mapping between triggers and data providers.
	 */
	private final Map<Trigger, List<TypedDataProvider<D>>> triggersToDataProviders =
			new HashMap<Trigger, List<TypedDataProvider<D>>>();

	/**
	 * Mapping between data providers and rules.
	 */
	private final Map<TypedDataProvider<?>, List<TypedDataRule<D, R>>> dataProvidersToRules =
			new HashMap<TypedDataProvider<?>, List<TypedDataRule<D, R>>>();

	/**
	 * Mapping between results and result handlers.
	 */
	private final Map<R, List<ResultHandler<R>>> resultsToResultHandlers = new HashMap<R, List<ResultHandler<R>>>();

	/**
	 * @see AbstractHomogeneousValidator#addTrigger(Object)
	 */
	@Override
	public void addTrigger(final Trigger trigger) {
		super.addTrigger(trigger);
		// TODO
	}

	/**
	 * @see AbstractHomogeneousValidator#removeTrigger(Object)
	 */
	@Override
	public void removeTrigger(final Trigger trigger) {
		super.removeTrigger(trigger);
		unmapTriggerFromAllDataProviders(trigger);
	}

	/**
	 * @see AbstractHomogeneousValidator#addDataProvider(Object)
	 */
	@Override
	public void addDataProvider(final TypedDataProvider<D> dataProvider) {
		super.addDataProvider(dataProvider);
		// TODO
	}

	/**
	 * @see AbstractHomogeneousValidator#removeDataProvider(Object)
	 */
	@Override
	public void removeDataProvider(final TypedDataProvider<D> dataProvider) {
		super.removeDataProvider(dataProvider);
		unmapDataProviderFromAllTriggers(dataProvider);
		unmapDataProviderFromAllRules(dataProvider);
	}

	/**
	 * @see AbstractHomogeneousValidator#addRule(Object)
	 */
	@Override
	public void addRule(final TypedDataRule<D, R> rule) {
		super.addRule(rule);
		// TODO
	}

	/**
	 * @see AbstractHomogeneousValidator#removeRule(Object)
	 */
	@Override
	public void removeRule(final TypedDataRule<D, R> rule) {
		super.removeRule(rule);
		unmapRuleFromAllDataProviders(rule);
	}

	/**
	 * @see AbstractHomogeneousValidator#addResultHandler(Object)
	 */
	@Override
	public void addResultHandler(final ResultHandler<R> resultHandler) {
		super.addResultHandler(resultHandler);
		// TODO
	}

	/**
	 * @see AbstractHomogeneousValidator#removeResultHandler(Object)
	 */
	@Override
	public void removeResultHandler(final ResultHandler<R> resultHandler) {
		super.removeResultHandler(resultHandler);
		unmapResultHandlerFromAllResults(resultHandler);
	}

	/**
	 * Maps the specified trigger to the specified data provider.<br>This means that whenever the specified trigger is
	 * initiated, the specified data provider will be use to read the data, which will be passed to the rules that are
	 * mapped to the this data provider.<br>Specifying null for the trigger will unmap the specified data provider from all
	 * triggers. This means that the specified data provider will no longer be used upon any trigger.<br>Specifying null
	 * for the data provider will unmap the specified trigger from all data providers. This means that the trigger will no
	 * longer have effect on the validation.
	 *
	 * @param trigger Trigger to be mapped to the data provider.
	 * @param dataProvider Data provider to be mapped to the trigger.
	 */
	public void map(final Trigger trigger, final TypedDataProvider<D> dataProvider) {
		if ((trigger == null) && (dataProvider == null)) {
			LOGGER.warn("Call to method will have no effect since both parameters are null");
		} else if (trigger == null) {
			unmapDataProviderFromAllTriggers(dataProvider);
		} else if (dataProvider == null) {
			unmapTriggerFromAllDataProviders(trigger);
		} else {
			List<TypedDataProvider<D>> mappedDataProviders = triggersToDataProviders.get(trigger);
			if (mappedDataProviders == null) {
				mappedDataProviders = new ArrayList<TypedDataProvider<D>>();
				triggersToDataProviders.put(trigger, mappedDataProviders);
			}
			mappedDataProviders.add(dataProvider);
		}
	}

	/**
	 * Disconnects the specified trigger from all data providers.
	 *
	 * @param trigger Trigger to be unmapped.
	 */
	private void unmapTriggerFromAllDataProviders(final Trigger trigger) {
		if (trigger != null) {
			triggersToDataProviders.remove(trigger);
		}
	}

	/**
	 * Disconnects the specified data providers from all triggers.
	 *
	 * @param dataProvider Data provider to be unmapped.
	 */
	private void unmapDataProviderFromAllTriggers(final TypedDataProvider<D> dataProvider) {
		if (dataProvider != null) {
			for (final List<TypedDataProvider<D>> mappedDataProviders : triggersToDataProviders.values()) {
				mappedDataProviders.remove(dataProvider);
			}
		}
	}

	/**
	 * Maps the specified data provider to the specified rule.<br>This means that whenever the specified data provider is
	 * used, the specified rule will be used to validate the data, and the validation result will be passed to the result
	 * handlers that are mapped to the this result.<br>Specifying null for the data provider will unmap the specified rule
	 * from all data providers. This means that the rule will no longer be part of the validation.<br>Specifying null for
	 * the rule will unmap the specified data provider from all rules. This means that the data from this data provider
	 * will no longer be validated.
	 *
	 * @param dataProvider Data provider to be mapped to the rule.
	 * @param rule Rule to be mapped to the data provider.
	 */
	public void map(final TypedDataProvider<D> dataProvider, final TypedDataRule<D, R> rule) {
		if ((dataProvider == null) && (rule == null)) {
			LOGGER.warn("Call to method will have no effect since both parameters are null");
		} else if (dataProvider == null) {
			unmapRuleFromAllDataProviders(rule);
		} else if (rule == null) {
			unmapDataProviderFromAllRules(dataProvider);
		} else {
			List<TypedDataRule<D, R>> mappedRules = dataProvidersToRules.get(dataProvider);
			if (mappedRules == null) {
				mappedRules = new ArrayList<TypedDataRule<D, R>>();
				dataProvidersToRules.put(dataProvider, mappedRules);
			}
			mappedRules.add(rule);
		}
	}

	/**
	 * Disconnects the specified data provider from all rules.
	 *
	 * @param dataProvider Data provider to be unmapped.
	 */
	private void unmapDataProviderFromAllRules(final TypedDataProvider<D> dataProvider) {
		if (dataProvider != null) {
			dataProvidersToRules.remove(dataProvider);
		}
	}

	/**
	 * Disconnects the specified rule from all data providers.
	 *
	 * @param rule Rule to be unmapped.
	 */
	private void unmapRuleFromAllDataProviders(final TypedDataRule<D, R> rule) {
		if (rule != null) {
			for (final List<TypedDataRule<D, R>> mappedRules : dataProvidersToRules.values()) {
				mappedRules.remove(rule);
			}
		}
	}

	/**
	 * Maps the specified result to the specified result handler.<br>This means that whenever the specified result is
	 * issued, the specified result handler will be used to process it.<br>Specifying null for the result will unmap the
	 * specified result handlers from all results. This means that the result handler will no longer be used to process any
	 * result.<br>Specifying null for the result handler will unmap the specified rule from all result handlers. This means
	 * that the result will no longer be processed.
	 *
	 * @param result Result to be mapped to the result handler.
	 * @param resultHandler Result handler to be mapped to the result.
	 */
	public void map(final R result, final ResultHandler<R> resultHandler) {
		if ((result == null) && (resultHandler == null)) {
			LOGGER.warn("Call to method will have no effect since both parameters are null");
		} else if (result == null) {
			unmapResultHandlerFromAllResults(resultHandler);
		} else if (resultHandler == null) {
			unmapResultFromAllResultHandlers(result);
		} else {
			List<ResultHandler<R>> mappedResultHandlers = resultsToResultHandlers.get(result);
			if (mappedResultHandlers == null) {
				mappedResultHandlers = new ArrayList<ResultHandler<R>>();
				resultsToResultHandlers.put(result, mappedResultHandlers);
			}
			mappedResultHandlers.add(resultHandler);
		}
	}

	/**
	 * Disconnects the specified result from all result handlers.
	 *
	 * @param result Result to be unmapped.
	 */
	private void unmapResultFromAllResultHandlers(final R result) {
		if (result != null) {
			resultsToResultHandlers.remove(result);
		}
	}

	/**
	 * Disconnects the specified result handler from all results.
	 *
	 * @param resultHandler Result handler to be unmapped.
	 */
	private void unmapResultHandlerFromAllResults(final ResultHandler<R> resultHandler) {
		if (resultHandler != null) {
			for (final List<ResultHandler<R>> mappedResultHandlers : resultsToResultHandlers.values()) {
				mappedResultHandlers.remove(resultHandler);
			}
		}
	}

	/**
	 * Processes the specified trigger by finding all the mapped data providers, and so on.
	 *
	 * @see AbstractHomogeneousValidator#processTrigger(Trigger)
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
