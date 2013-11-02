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

package com.google.code.validationframework.swing.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.binding.AbstractReadableProperty;
import com.google.code.validationframework.base.binding.WritableProperty;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class ComponentEnabledProperty extends AbstractReadableProperty<Boolean> implements WritableProperty<Boolean>,
        Disposable {

    private class PropertyChangeAdapter implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            setValue(component.isEnabled());
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -313416439956652682L;

    private final Component component;

    private final PropertyChangeAdapter propertyChangeAdapter = new PropertyChangeAdapter();

    private boolean settingValue = false;

    private boolean value = false;

    public ComponentEnabledProperty(Component component) {
        this.component = component;
        this.component.addPropertyChangeListener("enabled", propertyChangeAdapter);
        setValue(component.isEnabled());
    }

    /**
     * @see com.google.code.validationframework.api.common.Disposable#dispose()
     */
    @Override
    public void dispose() {
        component.removePropertyChangeListener("enabled", propertyChangeAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return value;
    }

    /**
     * @see WritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Boolean value) {
        if (!settingValue) {
            settingValue = true;

            boolean normalizedValue = false;
            if (value != null) {
                normalizedValue = value;
            }
            if (this.value != normalizedValue) {
                this.value = normalizedValue;
                component.setEnabled(normalizedValue);
                updateSlaves();
            }

            settingValue = false;
        }
    }
}
