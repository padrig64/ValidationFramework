package validation.validator;

import validation.feedback.FeedBack;
import validation.rule.Rule;

public interface Validator<D, R> {

	public void addRule(Rule<D, R> rule);

	public void removeRule(Rule<D, R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
