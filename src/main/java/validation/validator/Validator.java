package validation.validator;

import validation.feedback.FeedBack;
import validation.rule.Rule;
import validation.trigger.Trigger;

public interface Validator<D, R> {

	public void addTrigger(Trigger<D> trigger);

	public void removeTrigger(Trigger<D> trigger);

	public void addRule(Rule<D, R> rule);

	public void removeRule(Rule<D, R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
