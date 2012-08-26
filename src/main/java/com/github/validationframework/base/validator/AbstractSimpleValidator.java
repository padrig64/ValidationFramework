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
import com.github.validationframework.api.validator.SimpleValidator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a simple validator.<br>It merely implements the methods to add and remove triggers, data
 * providers, rules and result handlers. However, the use the connection between triggers, data providers, rules and
 * result handlers, as well as all the validation logic is left to the sub-classes.
 *
 * @param <T> Type of trigger initiating the validation.
 * @param <P> Type of data provider providing the input data to be validated.
 * @param <R> Type of validation rules to be used on the input data.
 * @param <H> Type of result handlers to be used on validation output.
 *
 * @see SimpleValidator
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 * @see Disposable
 */
public abstract class AbstractSimpleValidator<T extends Trigger, P extends DataProvider, R extends Rule, H extends ResultHandler>
		implements SimpleValidator<T, P, R, H>, Disposable {

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
	private final Map<T, TriggerListener> triggersToTriggerAdapters = new HashMap<T, TriggerListener>();

	/**
	 * Registered validation triggers.
	 */
	protected final List<T> triggers = new ArrayList<T>();

	/**
	 * Registered validation data providers.
	 */
	protected final List<P> dataProviders = new ArrayList<P>();

	/**
	 * Registered validation rules.
	 */
	protected List<R> rules = new ArrayList<R>();

	/**
	 * Registered validation result handlers.
	 */
	protected final List<H> resultHandlers = new ArrayList<H>();

	/**
	 * @see SimpleValidator#addTrigger(Object)
	 */
	@Override
	public void addTrigger(final T trigger) {
		triggers.add(trigger);

		// Hook to trigger only if not already done (the same trigger listener will be used if it was already hooked)
		if (!triggersToTriggerAdapters.containsKey(trigger)) {
			final TriggerListener triggerAdapter = new TriggerAdapter(trigger);
			triggersToTriggerAdapters.put(trigger, triggerAdapter);
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	/**
	 * @see SimpleValidator#removeTrigger(Object)
	 */
	@Override
	public void removeTrigger(final T trigger) {
		triggers.remove(trigger);

		// Unhook from trigger
		final TriggerListener triggerAdapter = triggersToTriggerAdapters.get(trigger);
		trigger.removeTriggerListener(triggerAdapter);

		// Check if trigger was added several times
		if (!triggers.contains(trigger)) {
			// All occurrences of the same trigger have been removed
			triggersToTriggerAdapters.remove(trigger);
		}
	}

	/**
	 * @see SimpleValidator#addDataProvider(Object)
	 */
	@Override
	public void addDataProvider(final P dataProvider) {
		dataProviders.add(dataProvider);
	}

	/**
	 * @see SimpleValidator#removeDataProvider(Object)
	 */
	@Override
	public void removeDataProvider(final P dataProvider) {
		dataProviders.remove(dataProvider);
	}

	/**
	 * @see SimpleValidator#addRule(Object)
	 */
	@Override
	public void addRule(final R rule) {
		rules.add(rule);
	}

	/**
	 * @see SimpleValidator#removeRule(Object)
	 */
	@Override
	public void removeRule(final R rule) {
		rules.remove(rule);
	}

	/**
	 * @see SimpleValidator#addResultHandler(Object)
	 */
	@Override
	public void addResultHandler(final H resultHandler) {
		resultHandlers.add(resultHandler);
	}

	/**
	 * @see SimpleValidator#removeResultHandler(Object)
	 */
	@Override
	public void removeResultHandler(final H resultHandler) {
		resultHandlers.remove(resultHandler);
	}

	/**
	 * @see Disposable#dispose()
	 */
	@Override
	public void dispose() {
		disposeTriggers();
		disposeDataProviders();
		disposeRules();
		disposeResultHandlers();
	}

	/**
	 * Clears all triggers.
	 */
	private void disposeTriggers() {
		// Browse through all triggers
		for (final T trigger : triggers) {
			// Disconnect installed trigger adapter and forget about the trigger
			final TriggerListener triggerAdapter = triggersToTriggerAdapters.remove(trigger);
			if (triggerAdapter != null) {
				trigger.removeTriggerListener(triggerAdapter);
			}

			// Dispose trigger itself
			if (trigger instanceof Disposable) {
				((Disposable) trigger).dispose();
			}
		}

		// Clears all triggers
		triggers.clear();
	}

	/**
	 * Clears all data providers.
	 */
	private void disposeDataProviders() {
		// Browse through all data providers
		for (final P dataProvider : dataProviders) {
			// Dispose data provider itself
			if (dataProvider instanceof Disposable) {
				((Disposable) dataProvider).dispose();
			}
		}

		// Clear all data providers
		dataProviders.clear();
	}

	/**
	 * Clears all rules.
	 */
	private void disposeRules() {
		// Browse through all rules
		for (final R rule : rules) {
			// Dispose rule itself
			if (rule instanceof Disposable) {
				((Disposable) rule).dispose();
			}
		}

		// Clear all data providers
		rules.clear();
	}

	/**
	 * Clears all result handlers.
	 */
	private void disposeResultHandlers() {
		// Browse through all rules
		for (final H resultHandler : resultHandlers) {
			// Dispose rule itself
			if (resultHandler instanceof Disposable) {
				((Disposable) resultHandler).dispose();
			}
		}

		// Clear all data providers
		resultHandlers.clear();
	}

	/**
	 * Performs the whole validation logic for the specified trigger.<br>Typically, data will be read from the data
	 * providers and passed to the rules, and the rule results will be processed by the result handlers.
	 *
	 * @param trigger Trigger actually initiated.
	 */
	protected abstract void processTrigger(final T trigger);
}
