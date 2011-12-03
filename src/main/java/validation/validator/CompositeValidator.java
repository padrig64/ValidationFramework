package validation.validator;

import java.util.ArrayList;
import java.util.List;

import validation.feedback.FeedBack;
import validation.rule.Rule;

public abstract class CompositeValidator<D, R> extends AbstractValidator<D, R> {

	private List<Validator<D, R>> validators = new ArrayList<Validator<D, R>>();

	public CompositeValidator(Validator<D, R>... validators) {
		super();
		for (Validator<D, R> validator : validators) {
			addValidator(validator);
		}
	}

	public void addValidator(Validator<D, R> validator) {
		validators.add(validator);
	}

	public void removeValidator(Validator<D, R> validator) {
		validators.remove(validator);
	}

	protected void triggerValidation() {
		D data = getData();

		for (Rule<D, R> rule : rules) {
			R result = rule.validate(data);
			for (FeedBack<R> feedBack : feedBacks) {
				feedBack.feedback(result);
			}
		}
	}
}
