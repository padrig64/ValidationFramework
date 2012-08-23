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

import com.github.validationframework.common.Disposable;
import com.github.validationframework.dataprovider.DataProvider;
import com.github.validationframework.resulthandler.ResultHandler;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;
import com.github.validationframework.trigger.TriggerEvent;
import com.github.validationframework.trigger.TriggerListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract implementation of a homogeneous validator.<br>A homogeneous validator is a validator whose data providers
 * and rules are bound to a known specific type of data, and whose result handlers are bound to a known specific type of
 * result. It provides the connection to the registered triggers, but the processing of the initiated triggers is left
 * to the sub-classes.<br>Homogeneous validators are typically used to validate one particular component or a group of
 * component holding data of a same type.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * type of the component itself.
 * @param <R> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 * @see com.github.validationframework.validator.AbstractSimpleValidator
 * @see com.github.validationframework.trigger.Trigger
 * @see com.github.validationframework.dataprovider.TypedDataProvider
 * @see com.github.validationframework.rule.TypedDataRule
 * @see com.github.validationframework.resulthandler.ResultHandler
 */
// TODO javadoc

/**
 * Abstract implementation of a validator.<br>It merely implements the methods to add and remove triggers, data
 * providers, rules and result handlers. However, note that the connection between triggers, data providers, rules and
 * result handlers, as well as all the validation logic is left to the sub-classes.
 *
 * @param <T> Type of trigger initiating the validation.
 * @param <P> Type of data provider providing the input data to be validated.
 * @param <U> Type of validation rules to be used on the input data.
 * @param <H> Type of result handlers to be used on validation output.
 *
 * @see com.github.validationframework.trigger.Trigger
 * @see com.github.validationframework.dataprovider.DataProvider
 * @see com.github.validationframework.rule.Rule
 * @see com.github.validationframework.resulthandler.ResultHandler
 */
public abstract class AbstractMappableValidator<T extends Trigger, P extends DataProvider, U extends Rule, R, H extends ResultHandler>
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
		 * @see com.github.validationframework.trigger.TriggerListener#triggerValidation(com.github.validationframework.trigger.TriggerEvent)
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

	private void hookTrigger(final T trigger) {
		// Hook to trigger only if not already done (the same trigger adatper will be used if it was already hooked)
		if (!triggersToTriggerAdapters.containsKey(trigger)) {
			final TriggerListener triggerAdapter = new TriggerAdapter(trigger);
			triggersToTriggerAdapters.put(trigger, triggerAdapter);
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	private void unhookTrigger(final T trigger) {
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
	 * @see MappableValidator#mapTriggerToDataProvider(Object, Object)
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
			hookTrigger(trigger);

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
			unhookTrigger(trigger);
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
	 * @see MappableValidator#mapDataProviderToRule(Object, Object)
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
	 * @see MappableValidator#mapResultToResultHandler(Object, Object)
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

	private void disposeTriggersAndDataProviders() {
		for (final Map.Entry<T, List<P>> entry : triggersToDataProviders.entrySet()) {
			// Disconnect from trigger
			unhookTrigger(entry.getKey());

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
