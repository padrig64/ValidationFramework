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

package com.github.validationframework.swing.trigger;

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.base.trigger.AbstractTrigger;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Trigger initiating the validation when one or several properties of a component change.
 */
public class ComponentPropertyChangeTrigger extends AbstractTrigger implements Disposable {

	/**
	 * Listener to component property changes triggering the validation.
	 */
	private class PropertyChangeAdapter implements PropertyChangeListener {

		/**
		 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			// Trigger validation
			fireTriggerEvent(new TriggerEvent(component));
		}
	}

	/**
	 * Component whose property changes are to be listened to.
	 */
	private final Component component;

	/**
	 * Listener to component property changes.
	 */
	private final PropertyChangeListener propertyChangeAdapter = new PropertyChangeAdapter();

	/**
	 * Constructor specifying the component whose property changes are to be watched.<br>Using this constructor will be the
	 * trigger initiate the validation upon any property change.
	 *
	 * @param component Component whose property changes are to be listened to.
	 */
	public ComponentPropertyChangeTrigger(final Component component) {
		super();
		this.component = component;
		component.addPropertyChangeListener(propertyChangeAdapter);
	}

	/**
	 * Constructor specifying the component for which the specified property changes are to be watched.
	 *
	 * @param component Component whose property changes are to be listened to.
	 * @param propertyNames Optional names of the properties of the component to listen to.<br>If no property name is
	 * specified, changes on any property will initiate the trigger.
	 */
	public ComponentPropertyChangeTrigger(final Component component, final String... propertyNames) {
		super();
		this.component = component;

		if ((propertyNames == null) || (propertyNames.length == 0)) {
			component.addPropertyChangeListener(propertyChangeAdapter);
		} else {
			for (final String propertyName : propertyNames) {
				component.addPropertyChangeListener(propertyName, propertyChangeAdapter);
			}
		}
	}

	/**
	 * @see Disposable#dispose()
	 */
	@Override
	public void dispose() {
		component.removePropertyChangeListener(propertyChangeAdapter);
	}
}
