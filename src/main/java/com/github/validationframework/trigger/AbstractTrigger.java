package com.github.validationframework.trigger;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractTrigger<D> implements Trigger<D>, DataReader<D> {

	private final List<TriggerListener<D>> triggerListeners = new ArrayList<TriggerListener<D>>();

	@Override
	public void addTriggerListener(TriggerListener<D> listener) {
		if (listener != null) {
			triggerListeners.add(listener);
		}
	}

	@Override
	public void removeTriggerListener(TriggerListener<D> listener) {
		if (listener != null) {
			triggerListeners.remove(listener);
		}
	}

	protected void triggerValidation() {
		D data = getData();

		for (TriggerListener<D> listener : triggerListeners) {
			listener.triggerValidation(data);
		}
	}
}
