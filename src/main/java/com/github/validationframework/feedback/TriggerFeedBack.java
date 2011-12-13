package com.github.validationframework.feedback;

import com.github.validationframework.trigger.AbstractTrigger;

public class TriggerFeedBack<R> extends AbstractTrigger<R> implements FeedBack<R> {

	private R result = null;

	@Override
	public R getData() {
		return result;
	}

	@Override
	public void feedback(R result) {
		this.result = result;
		triggerValidation();
	}
}
