/*
 * Copyright (c) 2015, ValidationFramework Authors
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
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

/**
 * Readable/writable property representing the location of a {@link Component} (possibly a {@link java.awt.Window}),
 * relatively to its parent.
 * <p/>
 * It is possible to control the location of the component by setting the value of this property or by calling the
 * {@link Component#setLocation(Point)} method of that component.
 * <p/>
 * However, note that the layout manager of the parent container may also modify the location of the component.
 * <p/>
 * Note that changing the X or Y coordinate of the {@link Point} object directly will have no effect on this property.
 * It is therefore not advised to do so.
 * <p/>
 * Finally note that null values are not supported by this property.
 *
 * @see Component#getLocation()
 * @see Component#setLocation(Point)
 */
public class ComponentLocationProperty extends AbstractReadableWritableProperty<Point, Point> implements Disposable {

    /**
     * Location tracker.
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
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            updateFromComponent();
        }

        /**
         * Sets the value of the property based on the location of the component.
         */
        private void updateFromComponent() {
            updatingFromComponent = true;
            setValue(component.getLocation());
            updatingFromComponent = false;
        }
    }

    /**
     * Component to track the location of.
     */
    private final Component component;

    /**
     * Location tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private Point value = null;

    /**
     * Flag indicating whether the {@link #setValue(Point)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component Component whose location property is to be tracked.
     */
    public ComponentLocationProperty(Component component) {
        super();

        // Hook to component
        this.component = component;
        this.component.addComponentListener(eventAdapter);

        // Set initial value
        value = component.getLocation();
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
    public Point getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Point value) {
        if (!isNotifyingListeners()) {
            if (updatingFromComponent) {
                Point oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else {
                component.setLocation(value);
            }
        }
    }
}
