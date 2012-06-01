package com.github.validationframework.trigger;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a trigger that is also an input provider.<br>The input retrieval ({@link #getInput()} is
 * left for the concrete sub-classes.
 *
 * @param <I> Type of input to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * component itself.
 * @see Trigger
 * @see InputProvider
 * @see TriggerListener
 */
public abstract class AbstractTrigger<I> implements Trigger<I>, InputProvider<I> {

	/**
	 * Listeners to validation triggers.
	 */
	private final List<TriggerListener<I>> triggerListeners = new ArrayList<TriggerListener<I>>();

	/**
	 * @see Trigger#addTriggerListener(TriggerListener)
	 */
	@Override
	public void addTriggerListener(final TriggerListener<I> listener) {
		if (listener != null) {
			triggerListeners.add(listener);
		}
	}

	/**
	 * @see Trigger#removeTriggerListener(TriggerListener)
	 */
	@Override
	public void removeTriggerListener(final TriggerListener<I> listener) {
		if (listener != null) {
			triggerListeners.remove(listener);
		}
	}

	/**
	 * Triggers all validation trigger listeners with the input retrieved from {@link #getInput()}.
	 */
	protected void triggerValidation() {
		// Retrieve input to be validated
		final I input = getInput();

		// Trigger all listeners
		for (final TriggerListener<I> listener : triggerListeners) {
			listener.triggerValidation(input);
		}
	}
}
