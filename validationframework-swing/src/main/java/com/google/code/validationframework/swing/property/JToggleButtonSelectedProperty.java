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

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Readable/writable property representing the selected state of a {@link JToggleButton}.
 * <p/>
 * This includes toggle buttons, radio buttons and checkboxes.
 * <p/>
 * It is possible to control the selected state of the component by setting the value of this property or by calling the
 * {@link JToggleButton#setSelected(boolean)} method of that component.
 * <p/>
 * Finally note that null values are not supported by this property.
 *
 * @see JToggleButton#isSelected()
 * @see JToggleButton#setSelected(boolean)
 */
public class JToggleButtonSelectedProperty extends AbstractReadableWritableProperty<Boolean> {

    /**
     * Selected index tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Component to track the selected state for.
     */
    private JToggleButton component;

    /**
     * Current property value.
     */
    private Boolean value = null;

    /**
     * Flag indicating whether the {@link #setValue(Boolean)} call is due to a selection change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component Component whose selected state is to be tracked.
     */
    public JToggleButtonSelectedProperty(JToggleButton component) {
        super();

        // Hook to component
        this.component = component;
        this.component.addItemListener(eventAdapter);

        // Set initial value
        value = component.isSelected();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (component != null) {
            component.removeItemListener(eventAdapter);
            component = null;
        }
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
        if (!isNotifyingListeners()) {
            if (updatingFromComponent) {
                Boolean oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else if (component != null) {
                component.setSelected(value);
            }
        }
    }

    /**
     * Selected index tracker.
     */
    private class EventAdapter implements ItemListener {

        /**
         * @see ItemListener#itemStateChanged(ItemEvent)
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (component != null) {
                updatingFromComponent = true;
                setValue(component.isSelected());
                updatingFromComponent = false;
            }
        }
    }
}
