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

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;
import com.github.validationframework.trigger.TriggerListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Common validator abstraction.<br>It provides the connection to the validation triggers, but the validation algorithm
 * is left to the sub-classes.
 *
 * @param <I> Type of the input to be validated.
 * @param <R> Type of the validation results produced by the validation rules.
 */
public abstract class AbstractValidator<I, R> implements Validator<I, R> {

	/**
	 * Listener to validation triggers.<br>It provides the entry point to the validation algorithm.
	 *
	 * @see AbstractValidator#doValidation(Object)
	 */
	private class TriggerAdapter implements TriggerListener<I> {

		/**
		 * @see TriggerListener#triggerValidation(Object)
		 */
		@Override
		public void triggerValidation(final I input) {
			doValidation(input);
		}
	}

	/**
	 * Listener to validation triggers.
	 */
	private final TriggerAdapter triggerAdapter = new TriggerAdapter();

	/**
	 * List of registered validation triggers.
	 */
	protected List<Trigger<I>> triggers = new ArrayList<Trigger<I>>();

	/**
	 * List of registered validation rules.
	 */
	protected List<Rule<I, R>> rules = new ArrayList<Rule<I, R>>();

	/**
	 * List of registered validation feedbacks.
	 */
	protected List<FeedBack<R>> feedBacks = new ArrayList<FeedBack<R>>();

	/**
	 * @see Validator#addTrigger(Trigger)
	 */
	@Override
	public void addTrigger(final Trigger<I> trigger) {
		if (trigger != null) {
			triggers.add(trigger);
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	/**
	 * @see Validator#removeTrigger(Trigger)
	 */
	@Override
	public void removeTrigger(final Trigger<I> trigger) {
		if (trigger != null) {
			triggers.remove(trigger);
			trigger.removeTriggerListener(triggerAdapter);
		}
	}

	/**
	 * @see Validator#addRule(Rule)
	 */
	@Override
	public void addRule(final Rule<I, R> rule) {
		rules.add(rule);
	}

	/**
	 * @see Validator#removeRule(Rule)
	 */
	@Override
	public void removeRule(final Rule<I, R> rule) {
		rules.remove(rule);
	}

	/**
	 * @see Validator#addFeedBack(FeedBack)
	 */
	@Override
	public void addFeedBack(final FeedBack<R> feedBack) {
		feedBacks.add(feedBack);
	}

	/**
	 * @see Validator#removeFeedBack(FeedBack)
	 */
	@Override
	public void removeFeedBack(final FeedBack<R> feedBack) {
		feedBacks.remove(feedBack);
	}

	/**
	 * Performs the validation on the given input with the validation rules and giving the feedback on the validation
	 * result.
	 *
	 * @param input Input read by the validation triggers and that is to be validated against the validation rules.
	 */
	protected abstract void doValidation(I input);
}
