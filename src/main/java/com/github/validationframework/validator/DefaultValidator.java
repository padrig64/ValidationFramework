package com.github.validationframework.validator;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;

/**
 * Simple default validator validating any data from the validation triggers against all validation rules and providing
 * all results to all validation feedbacks.
 *
 * @param <D> Type of the data to be validated.
 * @param <R> Type of the validation results produced by the validation rules.
 */
public class DefaultValidator<D, R> extends AbstractValidator<D, R> {

	/**
	 * @see AbstractValidator#doValidation(Object)
	 */
	@Override
	protected void doValidation(final D data) {
		for (final Rule<D, R> rule : rules) {
			final R result = rule.validate(data);
			for (final FeedBack<R> feedBack : feedBacks) {
				feedBack.feedback(result);
			}
		}
	}
}
