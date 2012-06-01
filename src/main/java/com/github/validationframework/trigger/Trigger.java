package com.github.validationframework.trigger;

/**
 * Interface to be implemented by entities triggering the validation of input.
 *
 * @param <I> Type of input to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * component itself.
 * @see TriggerListener
 */
public interface Trigger<I> {

	/**
	 * Adds the specified validation trigger listener.
	 *
	 * @param listener Listener to validation trigger to be added.
	 */
	public void addTriggerListener(TriggerListener<I> listener);

	/**
	 * Removes the specified validation trigger listener.
	 *
	 * @param listener Listener to validation trigger to be removed.
	 */
	public void removeTriggerListener(TriggerListener<I> listener);
}
