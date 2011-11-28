package validation.validator;

import java.util.ArrayList;
import java.util.List;

import validation.datarule.DataRule;
import validation.datavalidator.AbstractDataValidator;
import validation.datavalidator.DataValidator;
import validation.feedback.FeedBack;
import validation.rule.Rule;

public class CompositeValidator<R> extends AbstractValidator<R> {

	private List<Validator<?>> validators = new ArrayList<Validator<?>>();

	public CompositeValidator(Validator<?>... validators) {
		super();
		for (Validator<?> validator : validators) {
			addValidator(validator);
		}
	}

	public void addValidator(Validator<?> validator) {
		validators.add(validator);
	}

	public void removeValidator(Validator<?> validator) {
		validators.remove(validator);
	}

	protected void triggerValidation() {
		for (Rule<R> rule : rules) {
			R result = rule.validate();
			for (FeedBack<R> feedBack : feedBacks) {
				feedBack.feedback(result);
			}
		}
	}
}
