package com.github.validationframework.validator;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;

/**
 * Interface to be implemented by the validators.<br>The validator is the central point of the validation framework. It
 * has validation triggers (for example, whenever the user enters some data in a field, presses an Apply button, etc.),
 * which will read the data to be validated (for example, from the input field, a database, etc.), and will pass the
 * data to the various validation rules (for example, expecting data to be entered in a specific format), which will
 * produce validation results (for example, invalid input, valid input, input too long, misspell, etc.), which will be
 * used to give appropriate feedback to the user (for example, a popup dialog, an error icon, etc.).
 *
 * @param <I> Type of the input to be validated.
 * @param <R> Type of the validation results from the validation rules.
 * @see Trigger
 * @see Rule
 * @see FeedBack
 */
public interface Validator<I, R> {

	/**
	 * Adds the specified validation trigger.
	 *
	 * @param trigger Validation trigger.
	 */
	public void addTrigger(Trigger<I> trigger);

	/**
	 * Removes the specified validation trigger.
	 *
	 * @param trigger Validation trigger to be removed.
	 */
	public void removeTrigger(Trigger<I> trigger);

	/**
	 * Adds the specified validation rule.
	 *
	 * @param rule Validation rule.
	 */
	public void addRule(Rule<I, R> rule);

	/**
	 * Removes the specified validation rule.
	 *
	 * @param rule Validation rule.
	 */
	public void removeRule(Rule<I, R> rule);

	/**
	 * Adds the specified feedback on validation result.
	 *
	 * @param feedBack Validation result feedback.
	 */
	public void addFeedBack(FeedBack<R> feedBack);

	/**
	 * Removes the specified feedback on validation result.
	 *
	 * @param feedBack Validation result feedback.
	 */
	public void removeFeedBack(FeedBack<R> feedBack);
}
