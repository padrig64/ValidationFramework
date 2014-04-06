/*
 * Copyright (c) 2014, Patrick Moawad
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
import com.google.code.validationframework.base.property.AbstractReadableProperty;
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.base.utils.ValueUtils;

import javax.swing.JFormattedTextField;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ValueProperty extends AbstractReadableProperty<Object> implements
        WritableProperty<Object>, Disposable {

    private class PropertyChangeAdapter implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setValue(formattedTextField.getValue());
        }

    }

    private final JFormattedTextField formattedTextField;

    private final PropertyChangeAdapter propertyChangeAdapter = new PropertyChangeAdapter();

    private boolean settingValue = false;

    private Object value = null;

    public ValueProperty(JFormattedTextField formattedTextField) {
        this.formattedTextField = formattedTextField;
        this.formattedTextField.addPropertyChangeListener("value", propertyChangeAdapter);
        setValue(formattedTextField.getValue());
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        formattedTextField.removePropertyChangeListener("value", propertyChangeAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Object getValue() {
        return value;
    }

    /**
     * @see WritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Object value) {
        if (!settingValue) {
            settingValue = true;

            if (!ValueUtils.areEqual(this.value, value)) {
                Object oldValue = this.value;
                this.value = value;
                formattedTextField.setValue(value);
                maybeNotifyListeners(oldValue, value);
            }

            settingValue = false;
        }
    }
}
