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
import java.awt.Dimension;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Readable/writable property representing the size of a {@link Component} (possibly a {@link java.awt.Window}).
 * <p>
 * It is possible to control the size of the component by setting the value of this property or by calling the {@link
 * Component#setSize(Dimension)} method of that component.
 *
 * <p>
 * However, note that the layout manager of the parent container may also modify the size of the component.
 * <p>
 * Note that changing the width or height attribute of the {@link Dimension} object directly will have no effect on this
 * property. It is therefore not advised to do so.
 * <p>
 * Finally note that null values are not supported by this property.
 *
 * @see Component#getSize()
 * @see Component#setSize(Dimension)
 */
public class ComponentSizeProperty extends AbstractReadableWritableProperty<Dimension,
        Dimension> implements Disposable {

    /**
     * Size tracker.
     */
    private class EventAdapter implements ComponentListener {

        /**
         * @see ComponentListener#componentShown(ComponentEvent)
         */
        @Override
        public void componentShown(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentHidden(ComponentEvent)
         */
        @Override
        public void componentHidden(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentResized(ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            updateFromComponent();
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * Sets the value of the property based on the size of the component.
         */
        private void updateFromComponent() {
            updatingFromComponent = true;
            setValue(component.getSize());
            updatingFromComponent = false;
        }
    }

    /**
     * Component to track the size of.
     */
    private final Component component;

    /**
     * Size tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private Dimension value = null;

    /**
     * Flag indicating whether the {@link #setValue(Dimension)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component Component whose size property is to be tracked.
     */
    public ComponentSizeProperty(Component component) {
        super();

        // Hook to component
        this.component = component;
        this.component.addComponentListener(eventAdapter);

        // Set initial value
        value = component.getSize();
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
    public Dimension getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Dimension value) {
        if (!isNotifyingListeners()) {
            if (updatingFromComponent) {
                Dimension oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else {
                component.setSize(value);
            }
        }
    }
}
