package validation.validator;

import java.util.ArrayList;
import java.util.List;

import validation.feedback.FeedBack;
import validation.rule.Rule;

public abstract class AbstractValidator<D, R> implements Validator<D, R> {

	protected List<Rule<D, R>> rules = new ArrayList<Rule<D, R>>();

	protected List<FeedBack<R>> feedBacks = new ArrayList<FeedBack<R>>();

	public void addRule(Rule<D, R> rule) {
		rules.add(rule);
	}

	public void removeRule(Rule<D, R> rule) {
		rules.remove(rule);
	}

	public void addFeedBack(FeedBack<R> feedBack) {
		feedBacks.add(feedBack);
	}

	public void removeFeedBack(FeedBack<R> feedBack) {
		feedBacks.remove(feedBack);
	}

	protected abstract D getData();

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
