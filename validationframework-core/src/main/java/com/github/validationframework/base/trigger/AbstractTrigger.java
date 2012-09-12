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

package com.github.validationframework.base.trigger;

import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.api.trigger.TriggerListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a typical trigger.<br>It merely implements the methods to add and remove trigger
 * listeners, and provides the method {@link #fireTriggerEvent(TriggerEvent)} to fire a trigger event to these
 * listeners.<br>However, the call of this method is left to the sub-classes.
 *
 * @see Trigger
 * @see TriggerListener
 * @see TriggerEvent
 */
public abstract class AbstractTrigger implements Trigger {

	/**
	 * Trigger listeners.
	 */
	protected final List<TriggerListener> listeners = new ArrayList<TriggerListener>();

	/**
	 * @see Trigger#addTriggerListener(TriggerListener)
	 */
	@Override
	public void addTriggerListener(final TriggerListener listener) {
		listeners.add(listener);
	}

	/**
	 * @see Trigger#removeTriggerListener(TriggerListener)
	 */
	@Override
	public void removeTriggerListener(final TriggerListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Fires the specified trigger event.<br>Calling this method is left to the sub-classes.
	 *
	 * @param event Trigger event to be fired.
	 */
	protected void fireTriggerEvent(final TriggerEvent event) {
		for (final TriggerListener listener : listeners) {
			listener.triggerValidation(event);
		}
	}
}
