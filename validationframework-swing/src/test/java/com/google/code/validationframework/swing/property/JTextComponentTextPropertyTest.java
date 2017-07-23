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

import javax.swing.JTextField;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see JTextComponentTextProperty
 */
public class JTextComponentTextPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromPropertyWithoutInitialText() throws BadLocationException {
        JTextComponent component = new JTextField();
        ReadableWritableProperty<String, String> property = new JTextComponentTextProperty(component);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals("", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("", component.getText());
        assertEquals("", property.getValue());
        property.setValue("new text");
        assertEquals("new text", component.getText());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, "", "new text");
        verify(listenerMock).valueChanged(any(JTextComponentTextProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponentWithoutInitialText() throws BadLocationException {
        JTextComponent component = new JTextField();
        ReadableWritableProperty<String, String> property = new JTextComponentTextProperty(component);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals("", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("", component.getText());
        assertEquals("", property.getValue());
        component.setText("new text");
        assertEquals("new text", property.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, "", "new text");
        verify(listenerMock).valueChanged(any(JTextComponentTextProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromPropertyWithInitialText() throws BadLocationException {
        JTextComponent component = new JTextField("initial text");
        ReadableWritableProperty<String, String> property = new JTextComponentTextProperty(component);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals("initial text", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("initial text", component.getText());
        assertEquals("initial text", property.getValue());
        property.setValue("new text");
        assertEquals("new text", component.getText());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, "initial text", "new text");
        verify(listenerMock).valueChanged(any(JTextComponentTextProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponentWithInitialText() throws BadLocationException {
        JTextComponent component = new JTextField("initial text");
        ReadableWritableProperty<String, String> property = new JTextComponentTextProperty(component);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals("initial text", component.getDocument().getText(0, component.getDocument().getLength()));
        assertEquals("initial text", component.getText());
        assertEquals("initial text", property.getValue());
        component.setText("new text");
        assertEquals("new text", property.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, "", "new text");
        verify(listenerMock, times(2)).valueChanged(any(JTextComponentTextProperty.class), anyString(), anyString());
    }
}
