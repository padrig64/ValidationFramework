package com.github.validationframework.validator;

import java.util.ArrayList;
import java.util.List;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;
import com.github.validationframework.trigger.TriggerListener;

public abstract class AbstractValidator<D, R> implements Validator<D, R> {

	private class TriggerAdapter implements TriggerListener<D> {

		@Override
		public void triggerValidation(D data) {
			doValidation(data);
		}
	}

	private final TriggerAdapter triggerAdapter = new TriggerAdapter();

	protected List<Trigger<D>> triggers = new ArrayList<Trigger<D>>();

	protected List<Rule<D, R>> rules = new ArrayList<Rule<D, R>>();

	protected List<FeedBack<R>> feedBacks = new ArrayList<FeedBack<R>>();

	public void addTrigger(Trigger<D> trigger) {
		if (trigger != null) {
			triggers.add(trigger);
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	public void removeTrigger(Trigger<D> trigger) {
		if (trigger != null) {
			triggers.remove(trigger);
			trigger.removeTriggerListener(triggerAdapter);
		}
	}

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

	protected abstract void doValidation(D data);
}
