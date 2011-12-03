package validation.validator;

import org.jdesktop.core.animation.timing.Trigger;
import validation.feedback.FeedBack;
import validation.rule.Rule;

public interface Validator<D, R> {

	public void addTrigger(Trigger trigger);

	public void removeTrigger(Trigger trigger);

	public void addRule(Rule<D, R> rule);

	public void removeRule(Rule<D, R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
