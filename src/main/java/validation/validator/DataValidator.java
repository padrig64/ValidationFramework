package validation.validator;

import validation.feedback.FeedBack;
import validation.rule.DataRule;

public interface DataValidator<D, R> {

	public void addRule(DataRule<D, R> rule);

	public void removeRule(DataRule<D, R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
