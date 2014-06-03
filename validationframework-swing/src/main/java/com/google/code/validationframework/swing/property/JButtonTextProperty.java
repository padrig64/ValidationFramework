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

import javax.swing.JButton;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Readable/writable property representing the text bean property of a {@link JButton}.
 * <p/>
 * It is possible to control the text of the button by setting the value of this property or by calling the {@link
 * JButton#setText(String)} method of that button.
 * <p/>
 * If the value of this property is set to null, the button will be emptied.
 */
public class JButtonTextProperty extends AbstractReadableWritableProperty<String, String> implements Disposable {

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
            setValue(button.getText());
            updatingFromComponent = false;
        }
    }

    /**
     * Button to track the text of.
     */
    private final JButton button;

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
     * Constructor specifying the button for which the property applies.
     *
     * @param button Button whose text property is to be tracked.
     */
    public JButtonTextProperty(JButton button) {
        super();

        // Hook to component
        this.button = button;
        this.button.addPropertyChangeListener("text", eventAdapter);

        // Set initial value
        value = button.getText();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        button.removePropertyChangeListener("text", eventAdapter);
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
            button.setText(value);
        }
    }
}
