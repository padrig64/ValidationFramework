/*
 * Copyright (c) 2015, ValidationFramework Authors
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

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see ComponentSizeProperty
 */
public class ComponentSizePropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ReadableWritableProperty<Dimension, Dimension> property = new ComponentSizeProperty(component);
        ValueChangeListener<Dimension> listenerMock = (ValueChangeListener<Dimension>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals(new Dimension(0, 0), property.getValue());
        assertEquals(new Dimension(0, 0), component.getSize());
        property.setValue(new Dimension(11, 12));
        // Wait a little bit
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(new Dimension(11, 12), component.getSize());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, new Dimension(0, 0), new Dimension(11, 12));
        verify(listenerMock).valueChanged(any(ComponentSizeProperty.class), any(Dimension.class), any(Dimension.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ReadableWritableProperty<Dimension, Dimension> property = new ComponentSizeProperty(component);
        ValueChangeListener<Dimension> listenerMock = (ValueChangeListener<Dimension>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals(new Dimension(0, 0), property.getValue());
        assertEquals(new Dimension(0, 0), component.getSize());
        setSize(component, new Dimension(13, 14));
        assertEquals(new Dimension(13, 14), property.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, new Dimension(0, 0), new Dimension(13, 14));
        verify(listenerMock).valueChanged(any(ComponentSizeProperty.class), any(Dimension.class), any(Dimension.class));
    }

    @Test
    public void testDispose() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ComponentSizeProperty property = new ComponentSizeProperty(component);
        ValueChangeListener<Dimension> listener = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener);

        setSize(component, new Dimension(13, 14));
        setSize(component, new Dimension(15, 16));

        property.dispose();

        setSize(component, new Dimension(17, 18));
        setSize(component, new Dimension(19, 20));

        property.dispose();
        property.dispose();

        verify(listener).valueChanged(property, new Dimension(0, 0), new Dimension(13, 14));
        verify(listener).valueChanged(property, new Dimension(13, 14), new Dimension(15, 16));
        verifyNoMoreInteractions(listener);
    }

    private void setSize(Component component, Dimension size) {
        component.setSize(size);

        // Wait a little bit
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
