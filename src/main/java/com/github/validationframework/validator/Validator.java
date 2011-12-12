package com.github.validationframework.validator;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;

public interface Validator<D, R> {

	public void addTrigger(Trigger<D> trigger);

	public void removeTrigger(Trigger<D> trigger);

	public void addRule(Rule<D, R> rule);

	public void removeRule(Rule<D, R> rule);

	public void addFeedBack(FeedBack<R> feedBack);

	public void removeFeedBack(FeedBack<R> feedBack);
}
