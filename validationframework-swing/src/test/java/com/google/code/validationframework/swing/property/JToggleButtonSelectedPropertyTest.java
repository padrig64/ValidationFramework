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

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import javax.swing.JToggleButton;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JToggleButtonSelectedProperty
 */
public class JToggleButtonSelectedPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JToggleButton component = new JToggleButton();
        ReadableWritableProperty<Boolean, Boolean> property = new JToggleButtonSelectedProperty(component);
        ValueChangeListener<Boolean> listenerMock = (ValueChangeListener<Boolean>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertFalse(property.getValue());
        assertFalse(component.isSelected());
        property.setValue(true);
        assertTrue(component.isSelected());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, false, true);
        verify(listenerMock).valueChanged(any(JToggleButtonSelectedProperty.class), anyBoolean(), anyBoolean());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JToggleButton component = new JToggleButton();
        component.setSelected(true);
        ReadableWritableProperty<Boolean, Boolean> property = new JToggleButtonSelectedProperty(component);
        ValueChangeListener<Boolean> listenerMock = (ValueChangeListener<Boolean>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertTrue(property.getValue());
        component.setSelected(false);
        assertFalse(property.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, true, false);
        verify(listenerMock).valueChanged(any(JToggleButtonSelectedProperty.class), anyBoolean(), anyBoolean());
    }
}
