package com.github.validationframework.validator;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;

public class DefaultValidator<D, R> extends AbstractValidator<D, R> {

	@Override
	protected void doValidation(D data) {
		for (Rule<D, R> rule : rules) {
			R result = rule.validate(data);
			for (FeedBack<R> feedBack : feedBacks) {
				feedBack.feedback(result);
			}
		}
	}
}
