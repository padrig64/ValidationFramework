/*
 * Copyright (c) 2012, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
