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
import com.github.validationframework.trigger.TriggerEvent;
import com.github.validationframework.trigger.TriggerListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of a homogeneous validator.<br>A homogeneous validator is a validator whose data providers
 * and rules are bound to a known specific type of data, and whose result handlers are bound to a known specific type of
 * result. It provides the connection to the registered triggers, but the processing of the initiated triggers is left
 * to the sub-classes.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * type of the component itself.
 * @param <R> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 * @see AbstractValidator
 * @see Trigger
 * @see TypedDataProvider
 * @see TypedDataRule
 * @see TypedResultHandler
 */
public abstract class AbstractHomogeneousValidator<D, R>
		extends AbstractValidator<Trigger, TypedDataProvider<D>, TypedDataRule<D, R>, TypedResultHandler<R>> {

	/**
	 * Listener to all registered triggers, initiating the validation logic.
	 */
	private class TriggerAdapter implements TriggerListener {

		/**
		 * Trigger that is listened to.
		 */
		private final Trigger trigger;

		/**
		 * Constructor specifying the trigger that is listened to.
		 *
		 * @param trigger Trigger that is listened to.
		 */
		public TriggerAdapter(final Trigger trigger) {
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
	 * Listener to all registered triggers.
	 */
	private final Map<Trigger, TriggerListener> triggersToTriggerAdapters = new HashMap<Trigger, TriggerListener>();

	/**
	 * @see AbstractValidator#addTrigger(Trigger)
	 */
	@Override
	public void addTrigger(final Trigger trigger) {
		super.addTrigger(trigger);

		// Hook to trigger only if not already done (the same trigger listener will be used if it was already hooked)
		if (!triggersToTriggerAdapters.containsKey(trigger)) {
			final TriggerListener triggerAdapter = new TriggerAdapter(trigger);
			triggersToTriggerAdapters.put(trigger, triggerAdapter);
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	/**
	 * @see AbstractValidator#removeTrigger(Trigger)
	 */
	@Override
	public void removeTrigger(final Trigger trigger) {
		super.removeTrigger(trigger);

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
	 * Performs the whole validation logic for the specified trigger.<br>Typically, data will be read from the data
	 * providers and passed to the rules, and the rule results will be processed by the result handlers.
	 *
	 * @param trigger Trigger actually initiated.
	 */
	protected abstract void processTrigger(final Trigger trigger);
}
