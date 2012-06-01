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
