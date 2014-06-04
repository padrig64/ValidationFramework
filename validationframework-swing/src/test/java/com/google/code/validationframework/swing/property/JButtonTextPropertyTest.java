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

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import javax.swing.JButton;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JButtonTextProperty
 */
public class JButtonTextPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromProperty() {
        JButton button = new JButton("Text");
        ReadableWritableProperty<String, String> textProperty = new JButtonTextProperty(button);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        textProperty.addValueChangeListener(listenerMock);

        assertEquals("Text", textProperty.getValue());
        textProperty.setValue(null);
        assertEquals(null, button.getText());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(textProperty, "Text", null);
        verify(listenerMock).valueChanged(any(JButtonTextProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JButton button = new JButton("Text");
        ReadableWritableProperty<String, String> textProperty = new JButtonTextProperty(button);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        textProperty.addValueChangeListener(listenerMock);

        assertEquals("Text", textProperty.getValue());
        textProperty.setValue("Another text");
        assertEquals("Another text", button.getText());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(textProperty, "Text", "Another text");
        verify(listenerMock).valueChanged(any(JButtonTextProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromComponent() {
        JButton button = new JButton("Text");
        ReadableWritableProperty<String, String> textProperty = new JButtonTextProperty(button);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        textProperty.addValueChangeListener(listenerMock);

        assertEquals("Text", textProperty.getValue());
        button.setText(null);
        assertEquals(null, textProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(textProperty, "Text", null);
        verify(listenerMock).valueChanged(any(JButtonTextProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JButton button = new JButton("Text");
        ReadableWritableProperty<String, String> textProperty = new JButtonTextProperty(button);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        textProperty.addValueChangeListener(listenerMock);

        assertEquals("Text", textProperty.getValue());
        button.setText("Another text");
        assertEquals("Another text", textProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(textProperty, "Text", "Another text");
        verify(listenerMock).valueChanged(any(JButtonTextProperty.class), anyString(), anyString());
    }
}
