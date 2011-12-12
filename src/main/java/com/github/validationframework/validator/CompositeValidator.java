package com.github.validationframework.validator;

import java.util.ArrayList;
import java.util.List;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;

public abstract class CompositeValidator<D, R> extends DefaultValidator<D, R> {

	private List<Validator<D, R>> validators = new ArrayList<Validator<D, R>>();

	public CompositeValidator(Validator<D, R>... validators) {
		super();
		for (Validator<D, R> validator : validators) {
			addValidator(validator);
		}
	}

	public void addValidator(Validator<D, R> validator) {
		if (validator != null) {
			validators.add(validator);
		}
	}

	public void removeValidator(Validator<D, R> validator) {
		if (validator != null) {
			validators.remove(validator);
		}
	}

	protected void doValidation(D data) {
		for (Rule<D, R> rule : rules) {
			R result = rule.validate(data);
			for (FeedBack<R> feedBack : feedBacks) {
				feedBack.feedback(result);
			}
		}
	}
}
