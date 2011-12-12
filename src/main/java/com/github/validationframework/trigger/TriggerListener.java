package com.github.validationframework.trigger;

public interface TriggerListener<D> {

	public void triggerValidation(D data);
}
