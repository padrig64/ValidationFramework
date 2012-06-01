package com.github.validationframework.trigger;

/**
 * Interface to be implemented by validation trigger listeners.<br>The trigger listener are meant to start the
 * validation process.
 *
 * @param <I> Type of input to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * component itself.
 * @see Trigger
 */
public interface TriggerListener<I> {

	/**
	 * Starts the validation process with the specified input.
	 *
	 * @param input Input to be validated.
	 */
	public void triggerValidation(I input);
}
