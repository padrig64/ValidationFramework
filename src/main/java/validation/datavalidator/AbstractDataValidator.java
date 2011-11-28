package validation.datavalidator;

import java.util.ArrayList;
import java.util.List;

import validation.datarule.DataRule;
import validation.feedback.FeedBack;

public abstract class AbstractDataValidator<D, R> implements DataValidator<D, R> {

	protected List<DataRule<D, R>> rules = new ArrayList<DataRule<D, R>>();

	protected List<FeedBack<R>> feedBacks = new ArrayList<FeedBack<R>>();

	public void addRule(DataRule<D, R> rule) {
		rules.add(rule);
	}

	public void removeRule(DataRule<D, R> rule) {
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

		for (DataRule<D, R> rule : rules) {
			R result = rule.validate(data);
			for (FeedBack<R> feedBack : feedBacks) {
				feedBack.feedback(result);
			}
		}
	}
}
