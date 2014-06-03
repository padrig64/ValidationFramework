/*
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

import javax.swing.JLabel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Readable/writable property representing the text bean property of a {@link JLabel}.
 * <p/>
 * It is possible to control the text of the label by setting the value of this property or by calling the {@link
 * JLabel#setText(String)} method of that label.
 * <p/>
 * If the value of this property is set to null, the label will be emptied.
 */
public class JLabelTextProperty extends AbstractReadableWritableProperty<String, String> implements Disposable {

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
            setValue(label.getText());
            updatingFromComponent = false;
        }
    }

    /**
     * Label to track the text of.
     */
    private final JLabel label;

    /**
     * Bean property tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private String value = null;

    /**
     * Flag indicating whether the {@link #setValue(String)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the label for which the property applies.
     *
     * @param label Label whose text property is to be tracked.
     */
    public JLabelTextProperty(JLabel label) {
        super();

        // Hook to component
        this.label = label;
        this.label.addPropertyChangeListener("text", eventAdapter);

        // Set initial value
        value = label.getText();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        label.removePropertyChangeListener("text", eventAdapter);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(String value) {
        if (updatingFromComponent) {
            String oldValue = this.value;
            this.value = value;
            maybeNotifyListeners(oldValue, this.value);
        } else {
            label.setText(value);
        }
    }
}
