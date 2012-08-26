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

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.api.trigger.TriggerListener;
import com.github.validationframework.api.validator.MappableValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a simple validator.<br>It merely implements the methods to map triggers to data providers,
 * data providers to rules, and results to result handlers. However, the use triggers, data providers, rules and result
 * handlers, as well as all the validation logic is left to the sub-classes.
 *
 * @param <T> Type of trigger initiating the validation.
 * @param <P> Type of data provider providing the input data to be validated.
 * @param <U> Type of validation rules to be used on the input data.
 * @param <R> Type of validation results produced by validation rules.
 * @param <H> Type of result handlers to be used on validation output.
 *
 * @see MappableValidator
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 * @see Disposable
 */
public abstract class AbstractMappableValidator<T extends Trigger, P extends DataProvider, U extends Rule, R, H extends ResultHandler<R>>
		implements MappableValidator<T, P, U, R, H>, Disposable {

	/**
	 * Listener to all registered triggers, initiating the validation logic.
	 */
	private class TriggerAdapter implements TriggerListener {

		/**
		 * Trigger that is listened to.
		 */
		private final T trigger;

		/**
		 * Constructor specifying the trigger that is listened to.
		 *
		 * @param trigger Trigger that is listened to.
		 */
		public TriggerAdapter(final T trigger) {
			this.trigger = trigger;
		}

		/**
		 * @see TriggerListener#triggerValidation(TriggerEvent)
		 */
		@Override
		public void triggerValidation(final TriggerEvent event) {
			// Start validation logic
			processTrigger(trigger);
		}
	}

	/**
	 * Listeners to all registered validation triggers.
	 */
	protected final Map<T, TriggerListener> triggersToTriggerAdapters = new HashMap<T, TriggerListener>();

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractMappableValidator.class);

	/**
	 * Mapping between triggers and data providers.
	 */
	protected final Map<T, List<P>> triggersToDataProviders = new HashMap<T, List<P>>();

	/**
	 * Mapping between data providers and rules.
	 */
	protected final Map<P, List<U>> dataProvidersToRules = new HashMap<P, List<U>>();

	/**
	 * Mapping between results and result handlers.
	 */
	protected final Map<R, List<H>> resultsToResultHandlers = new HashMap<R, List<H>>();

	/**
	 * Registers a trigger listener to start the validation flow.<br>If a trigger listener was already previously
	 * registered, calling this method will have no effect.
	 *
	 * @param trigger Trigger to hook to.
	 */
	private void hookToTrigger(final T trigger) {
		// Hook to trigger only if not already done (the same trigger adatper will be used if it was already hooked)
		if (!triggersToTriggerAdapters.containsKey(trigger)) {
			final TriggerListener triggerAdapter = new TriggerAdapter(trigger);
			triggersToTriggerAdapters.put(trigger, triggerAdapter);
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	/**
	 * De-registers the trigger listener.
	 *
	 * @param trigger Trigger to unhook from.
	 */
	private void unhookFromTrigger(final T trigger) {
		// Unhook from trigger
		final TriggerListener triggerAdapter = triggersToTriggerAdapters.get(trigger);
		trigger.removeTriggerListener(triggerAdapter);

		// Check if trigger was added several times
		if (!triggersToTriggerAdapters.containsKey(trigger)) {
			// All occurrences of the same trigger have been removed
			triggersToTriggerAdapters.remove(trigger);
		}
	}

	/**
	 * @see MappableValidator#mapTriggerToDataProvider(Trigger, DataProvider)
	 */
	@Override
	public void mapTriggerToDataProvider(final T trigger, final P dataProvider) {
		if ((trigger == null) && (dataProvider == null)) {
			LOGGER.warn("Call to method will have no effect since both parameters are null");
		} else if (trigger == null) {
			unmapDataProviderFromAllTriggers(dataProvider);
		} else if (dataProvider == null) {
			unmapTriggerFromAllDataProviders(trigger);
		} else {
			// Hook trigger
			hookToTrigger(trigger);

			// Do the mapping
			List<P> mappedDataProviders = triggersToDataProviders.get(trigger);
			if (mappedDataProviders == null) {
				mappedDataProviders = new ArrayList<P>();
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
	private void unmapTriggerFromAllDataProviders(final T trigger) {
		if (trigger != null) {
			unhookFromTrigger(trigger);
			triggersToDataProviders.remove(trigger);
		}
	}

	/**
	 * Disconnects the specified data providers from all triggers.
	 *
	 * @param dataProvider Data provider to be unmapped.
	 */
	private void unmapDataProviderFromAllTriggers(final P dataProvider) {
		if (dataProvider != null) {
			for (final List<P> mappedDataProviders : triggersToDataProviders.values()) {
				mappedDataProviders.remove(dataProvider);
			}
		}
	}

	/**
	 * @see MappableValidator#mapDataProviderToRule(DataProvider, Rule)
	 */
	@Override
	public void mapDataProviderToRule(final P dataProvider, final U rule) {
		if ((dataProvider == null) && (rule == null)) {
			LOGGER.warn("Call to method will have no effect since both parameters are null");
		} else if (dataProvider == null) {
			unmapRuleFromAllDataProviders(rule);
		} else if (rule == null) {
			unmapDataProviderFromAllRules(dataProvider);
		} else {
			List<U> mappedRules = dataProvidersToRules.get(dataProvider);
			if (mappedRules == null) {
				mappedRules = new ArrayList<U>();
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
	private void unmapDataProviderFromAllRules(final P dataProvider) {
		if (dataProvider != null) {
			dataProvidersToRules.remove(dataProvider);
		}
	}

	/**
	 * Disconnects the specified rule from all data providers.
	 *
	 * @param rule Rule to be unmapped.
	 */
	private void unmapRuleFromAllDataProviders(final U rule) {
		if (rule != null) {
			for (final List<U> mappedRules : dataProvidersToRules.values()) {
				mappedRules.remove(rule);
			}
		}
	}

	/**
	 * @see MappableValidator#mapResultToResultHandler(Object, ResultHandler)
	 */
	@Override
	public void mapResultToResultHandler(final R result, final H resultHandler) {
		if ((result == null) && (resultHandler == null)) {
			LOGGER.warn("Call to method will have no effect since both parameters are null");
		} else if (result == null) {
			unmapResultHandlerFromAllResults(resultHandler);
		} else if (resultHandler == null) {
			unmapResultFromAllResultHandlers(result);
		} else {
			List<H> mappedResultHandlers = resultsToResultHandlers.get(result);
			if (mappedResultHandlers == null) {
				mappedResultHandlers = new ArrayList<H>();
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
	private void unmapResultHandlerFromAllResults(final H resultHandler) {
		if (resultHandler != null) {
			for (final List<H> mappedResultHandlers : resultsToResultHandlers.values()) {
				mappedResultHandlers.remove(resultHandler);
			}
		}
	}

	/**
	 * @see Disposable#dispose()
	 */
	@Override
	public void dispose() {
		disposeTriggersAndDataProviders();
		disposeDataProvidersAndRules();
		disposeResultsAndResultHandlers();
	}

	/**
	 * Disposes all triggers and data providers that are mapped to each other.
	 */
	private void disposeTriggersAndDataProviders() {
		for (final Map.Entry<T, List<P>> entry : triggersToDataProviders.entrySet()) {
			// Disconnect from trigger
			unhookFromTrigger(entry.getKey());

			// Dispose trigger itself
			final T trigger = entry.getKey();
			if (trigger instanceof Disposable) {
				((Disposable) trigger).dispose();
			}

			// Dispose data providers
			final List<P> dataProviders = entry.getValue();
			if (dataProviders != null) {
				for (final P dataProvider : dataProviders) {
					if (dataProvider instanceof Disposable) {
						((Disposable) dataProvider).dispose();
					}
				}
			}
		}

		// Clears all triggers
		triggersToDataProviders.clear();
	}

	/**
	 * Disposes all data providers and rules that are mapped to each other.<br>Note that some data providers may have been
	 * disposed already in the other disposal methods.
	 */
	private void disposeDataProvidersAndRules() {
		for (final Map.Entry<P, List<U>> entry : dataProvidersToRules.entrySet()) {
			// Dispose data provider
			final P dataProvider = entry.getKey();
			if (dataProvider instanceof Disposable) {
				((Disposable) dataProvider).dispose();
			}

			// Dispose rules
			final List<U> rules = entry.getValue();
			if (rules != null) {
				for (final U rule : rules) {
					if (rule instanceof Disposable) {
						((Disposable) rule).dispose();
					}
				}
			}
		}

		// Clears all triggers
		dataProvidersToRules.clear();
	}

	/**
	 * Disposes all results and result handlers that are mapped to each other.
	 */
	private void disposeResultsAndResultHandlers() {
		for (final Map.Entry<R, List<H>> entry : resultsToResultHandlers.entrySet()) {
			// Dispose result
			final R result = entry.getKey();
			if (result instanceof Disposable) {
				((Disposable) result).dispose();
			}

			// Dispose result handlers
			final List<H> resultHandlers = entry.getValue();
			if (resultHandlers != null) {
				for (final H resultHandler : resultHandlers) {
					if (resultHandler instanceof Disposable) {
						((Disposable) resultHandler).dispose();
					}
				}
			}
		}

		// Clears all triggers
		resultsToResultHandlers.clear();
	}

	/**
	 * Performs the whole validation logic for the specified trigger.<br>Typically, data will be read from the data
	 * providers and passed to the rules, and the rule results will be processed by the result handlers.
	 *
	 * @param trigger Trigger actually initiated.
	 */
	protected abstract void processTrigger(final T trigger);
}
