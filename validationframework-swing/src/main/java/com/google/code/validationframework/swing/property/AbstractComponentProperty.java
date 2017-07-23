/*
 * Copyright (c) 2017, ValidationFramework Authors
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
 * Abstract implementation of a readable/writable property representing a bean property of a {@link Component} that can
 * be tracked using a {@link PropertyChangeListener}.
 * <p>
 * It is possible to control the bean property of the component by setting the value of this property or by calling the
 * bean property setter method of that component.
 *
 * @param <C> Type of component.
 * @param <P> Type of bean property.
 */
public abstract class AbstractComponentProperty<C extends Component, P> extends AbstractReadableWritableProperty<P,
        P> implements Disposable {

    /**
     * Bean property tracker.
     */
    private class EventAdapter implements PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            updatingFromComponent = true;
            setValue(getPropertyValueFromComponent());
            updatingFromComponent = false;
        }
    }

    /**
     * Component to track the bean property of.
     */
    protected final C component;

    /**
     * Name of the property to be tracked.
     */
    private final String propertyName;

    /**
     * Bean property tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private P value = null;

    /**
     * Flag indicating whether the {@link #setValue(Object)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component    Component whose property is to be tracked.
     * @param propertyName Name of the property to be tracked.
     */
    public AbstractComponentProperty(C component, String propertyName) {
        super();

        // Hook to component
        this.component = component;
        this.propertyName = propertyName;

        // Hook to component
        component.addPropertyChangeListener(propertyName, eventAdapter);

        // Set initial value
        value = getPropertyValueFromComponent();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        component.removePropertyChangeListener(propertyName, eventAdapter);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public P getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(P value) {
        if (!isNotifyingListeners()) {
            if (updatingFromComponent) {
                P oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else {
                setPropertyValueToComponent(value);
            }
        }
    }

    protected abstract P getPropertyValueFromComponent();

    protected abstract void setPropertyValueToComponent(P value);
}
