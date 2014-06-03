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

import javax.swing.Icon;
import javax.swing.JLabel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Readable/writable property representing the icon bean property of a {@link JLabel}.
 * <p/>
 * It is possible to control the icon of the label by setting the value of this property or by calling the {@link
 * JLabel#setIcon(Icon)} method of that label.
 * <p/>
 * If the value of this property is set to null, the label will have no icon.
 */
public class JLabelIconProperty extends AbstractReadableWritableProperty<Icon, Icon> implements Disposable {

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
            setValue(label.getIcon());
            updatingFromComponent = false;
        }
    }

    /**
     * Label to track the icon of.
     */
    private final JLabel label;

    /**
     * Bean property tracker.
     */
    private final EventAdapter eventAdapter = new EventAdapter();

    /**
     * Current property value.
     */
    private Icon value = null;

    /**
     * Flag indicating whether the {@link #setValue(Icon)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the label for which the property applies.
     *
     * @param label Label whose icon property is to be tracked.
     */
    public JLabelIconProperty(JLabel label) {
        super();

        // Hook to component
        this.label = label;
        this.label.addPropertyChangeListener("icon", eventAdapter);

        // Set initial value
        value = label.getIcon();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        // Unhook from component
        label.removePropertyChangeListener("icon", eventAdapter);
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public Icon getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Icon value) {
        if (updatingFromComponent) {
            Icon oldValue = this.value;
            this.value = value;
            maybeNotifyListeners(oldValue, this.value);
        } else {
            label.setIcon(value);
        }
    }
}
