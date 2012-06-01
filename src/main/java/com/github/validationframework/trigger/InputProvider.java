package com.github.validationframework.trigger;

/**
 * Interface to be implemented by entities providing input for the validation rules.<br>Typical {@link Trigger}
 * implementations may also implement this interface.
 *
 * @param <I> Type of input to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 * component itself.
 * @see Trigger
 */
public interface InputProvider<I> {

	/**
	 * Retrieves the input to be validated by validation rules.
	 *
	 * @return Input for validation rules.
	 */
	public I getInput();
}
