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
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Readable/writable property representing the visible state of a {@link javax.swing.JComponent}.
 * <p/>
 * It is possible to control the visible state of the component by setting the value of this property or by calling the
 * {@link Component#setVisible(boolean)} method of that component.
 * <p/>
 * If the value of this property is set to null, the component visible state will not be changed.
 */
public class ComponentVisibleProperty extends AbstractReadableWritableProperty<Boolean, Boolean> implements Disposable {

    /**
     * Visible state tracker.
     */
    private class EventAdapter implements ComponentListener {

        /**
         * @see ComponentListener#componentShown(ComponentEvent)
         */
        @Override
        public void componentShown(ComponentEvent e) {
            updateFromComponent();
        }

        /**
         * @see ComponentListener#componentHidden(ComponentEvent)
         */
        @Override
        public void componentHidden(ComponentEvent e) {
            updateFromComponent();
        }

        /**
         * @see ComponentListener#componentResized(ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * Sets the value of the property based on the visible state of the component.
         */
        private void updateFromComponent() {
            updatingFromComponent = true;
            setValue(component.isVisible());
            updatingFromComponent = false;
        }
    }

    /**
     * Component to track the visible state for.
     */
    private final Component component;

    /**
     * Visible state tracker.
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
     * @param component Component whose visible property is to be tracked.
     */
    public ComponentVisibleProperty(Component component) {
        super();

        // Hook to component
        this.component = component;
        this.component.addComponentListener(eventAdapter);

        // Set initial value
        value = component.isVisible();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        component.removeComponentListener(eventAdapter);
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
            component.setVisible(value);
        }
    }
}
