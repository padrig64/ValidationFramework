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


/**
 * Interface to be implemented by validators.<br>The validator is the central point of the validation framework. It
 * implements the whole chain of validation, from the triggers, till the display of the feedback.<br>Upon validation
 * trigger (for example, whenever the user enters some data in a field, presses an Apply button, etc.), data is
 * retrieved (for example, from the input field, a database, etc.) and passed to the validation algorithm (for example,
 * expecting data to be entered in a specific format), which produces validation results (for example, invalid input,
 * valid input, input too long, misspelled, etc.), which are then used to give appropriate feedback to the user (for
 * example, a popup dialog, an error icon, etc.).
 */

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
 * @see AbstractSimpleValidator
 * @see Trigger
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
 * @see SimpleValidator
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 */
public abstract class AbstractSimpleValidator<T extends Trigger, P extends DataProvider, U extends Rule, H extends ResultHandler>
		implements SimpleValidator<T, P, U, H> {

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
		 * @see TriggerListener#triggerValidation(com.github.validationframework.trigger.TriggerEvent)
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
	protected List<U> rules = new ArrayList<U>();

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
	public void addRule(final U rule) {
		rules.add(rule);
	}

	/**
	 * @see SimpleValidator#removeRule(Object)
	 */
	@Override
	public void removeRule(final U rule) {
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
	 * Performs the whole validation logic for the specified trigger.<br>Typically, data will be read from the data
	 * providers and passed to the rules, and the rule results will be processed by the result handlers.
	 *
	 * @param trigger Trigger actually initiated.
	 */
	protected abstract void processTrigger(final T trigger);
}
