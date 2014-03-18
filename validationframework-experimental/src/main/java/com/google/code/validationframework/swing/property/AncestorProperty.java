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
import com.google.code.validationframework.base.utils.ValueUtils;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Readable property representing the ancestor property of a {@link Component}.
 * <p/>
 * Note that change listeners will not be notified if the {@link Component} is not a {@link javax.swing.JComponent}.
 */
public class AncestorProperty extends AbstractReadableProperty<Container> implements Disposable {

    private class PropertyChangeAdapter implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (evt.getNewValue() instanceof Container) {
                setValue((Container) evt.getNewValue());
            } else {
                setValue(null);
            }
        }
    }

    private final Component component;

    private final PropertyChangeAdapter propertyChangeAdapter = new PropertyChangeAdapter();

    private Container value = null;

    public AncestorProperty(Component component) {
        this.component = component;
        this.component.addPropertyChangeListener("ancestor", propertyChangeAdapter);
        setValue(component.getParent());
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        component.removePropertyChangeListener("ancestor", propertyChangeAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Container getValue() {
        return value;
    }

    public void setValue(Container value) {
        if (!ValueUtils.areEqual(this.value, value)) {
            Container oldValue = this.value;
            this.value = value;
            notifyListeners(oldValue, value);
        }
    }
}
