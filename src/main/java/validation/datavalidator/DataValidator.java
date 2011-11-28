package validation.datavalidator;

import validation.datarule.DataRule;
import validation.feedback.FeedBack;

public interface DataValidator<D, R> {

	public void addRule(DataRule<D, R> rule);

	public void removeRule(DataRule<D, R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
