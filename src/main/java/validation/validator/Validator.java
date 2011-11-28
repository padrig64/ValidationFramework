package validation.validator;

import validation.feedback.FeedBack;
import validation.rule.Rule;

public interface Validator<R> {

	public void addRule(Rule<R> rule);

	public void removeRule(Rule<R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
