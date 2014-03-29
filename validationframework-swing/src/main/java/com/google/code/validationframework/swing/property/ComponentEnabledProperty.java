/*
 * %%Ignore-License
 *
 * Copyright (c) 2013, Patrick Moawad
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

package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableWritableProperty;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Readable/writable property representing the enabled state of a {@link Component}.
 * <p/>
 * It is possible to control the enabled state of the component by setting the value of this property or by calling the
 * {@link Component#setEnabled(boolean)} method of that component.
 * <p/>
 * Please note that a plain {@link Component} does not a fire property change event when it gets enabled/disabled. A
 * Swing {@link javax.swing.JComponent} does. However, the type {@link Component} is used by this property instead of
 * {@link javax.swing.JComponent} for convenience.
 * <p/>
 * If the value of this property is set to null, the component enabled state will not be changed.
 */
public class ComponentEnabledProperty extends AbstractReadableWritableProperty<Boolean, Boolean> implements Disposable {

    /**
     * Enabled state tracker.
     */
    private class EventAdapter implements PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updatingFromComponent = true;
            setValue(component.isEnabled());
            updatingFromComponent = false;
        }
    }

    /**
     * Component to track the enabled state for.
     */
    private final Component component;

    /**
     * Enabled state tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private Boolean value = null;

    /**
     * Flag indicating whether the {@link #setValue(Boolean)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component Component whose enabled property is to be tracked.
     */
    public ComponentEnabledProperty(Component component) {
        super();

        // Hook to component
        this.component = component;
        this.component.addPropertyChangeListener("enabled", eventAdapter);

        // Set initial value
        value = component.isEnabled();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        component.removePropertyChangeListener("enabled", eventAdapter);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Boolean value) {
        if (updatingFromComponent) {
            Boolean oldValue = this.value;
            this.value = value;
            notifyListeners(oldValue, this.value);
        } else if (value != null) {
            component.setEnabled(value);
        }
    }
}
