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

import javax.swing.JComboBox;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * Readable/writable property representing the selected index of a {@link JComboBox}.
 * <p>
 * It is possible to control the selected index of the component by setting the value of this property or by calling the
 * {@link JComboBox#setSelectedIndex(int)} or {@link JComboBox#setSelectedItem(Object)} methods of that component.
 * <p>
 * If the value of this property is set to null, the component selected index will not be changed.
 *
 * @see JComboBox#getSelectedIndex()
 * @see JComboBox#setSelectedIndex(int)
 * @see JComboBoxSelectedValueProperty
 */
public class JComboBoxSelectedIndexProperty extends AbstractReadableWritableProperty<Integer,
        Integer> implements Disposable {

    /**
     * Selected index tracker.
     */
    private class EventAdapter implements ItemListener {

        /**
         * @see ItemListener#itemStateChanged(ItemEvent)
         */
        @Override
        public void itemStateChanged(ItemEvent e) {
            updatingFromComponent = true;
            setValue(component.getSelectedIndex());
            updatingFromComponent = false;
        }
    }

    /**
     * Component to track the selected index for.
     */
    private final JComboBox component;

    /**
     * Selected index tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private Integer value = null;

    /**
     * Flag indicating whether the {@link #setValue(Integer)} call is due to a selection change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the component for which the property applies.
     *
     * @param component Component whose selected property is to be tracked.
     */
    public JComboBoxSelectedIndexProperty(JComboBox component) {
        super();

        // Hook to component
        this.component = component;
        this.component.addItemListener(eventAdapter);

        // Set initial value
        value = component.getSelectedIndex();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        component.removeItemListener(eventAdapter);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Integer value) {
        if (!isNotifyingListeners()) {
            if (updatingFromComponent) {
                Integer oldValue = this.value;
                this.value = value;
                maybeNotifyListeners(oldValue, this.value);
            } else {
                component.setSelectedIndex(value);
            }
        }
    }
}
