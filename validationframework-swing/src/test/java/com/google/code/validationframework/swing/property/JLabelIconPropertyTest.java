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
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import org.junit.Test;

import javax.swing.Icon;
import javax.swing.JLabel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JLabelIconProperty
 */
@Deprecated
public class JLabelIconPropertyTest {

    private static final Icon ICON1 = IconUtils.loadImageIcon("/images/info.png", JLabelIconPropertyTest.class);

    private static final Icon ICON2 = IconUtils.loadImageIcon("/images/valid.png", JLabelIconPropertyTest.class);

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromProperty() {
        JLabel label = new JLabel(ICON1);
        ReadableWritableProperty<Icon> iconProperty = new JLabelIconProperty(label);
        ValueChangeListener<Icon> listenerMock = (ValueChangeListener<Icon>) mock(ValueChangeListener.class);
        iconProperty.addValueChangeListener(listenerMock);

        assertEquals(ICON1, iconProperty.getValue());
        iconProperty.setValue(null);
        assertEquals(null, label.getIcon());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(iconProperty, ICON1, null);
        verify(listenerMock).valueChanged(any(JLabelIconProperty.class), any(Icon.class), any(Icon.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JLabel label = new JLabel(ICON1);
        ReadableWritableProperty<Icon> iconProperty = new JLabelIconProperty(label);
        ValueChangeListener<Icon> listenerMock = (ValueChangeListener<Icon>) mock(ValueChangeListener.class);
        iconProperty.addValueChangeListener(listenerMock);

        assertEquals(ICON1, iconProperty.getValue());
        iconProperty.setValue(ICON2);
        assertEquals(ICON2, label.getIcon());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(iconProperty, ICON1, ICON2);
        verify(listenerMock).valueChanged(any(JLabelIconProperty.class), any(Icon.class), any(Icon.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromComponent() {
        JLabel label = new JLabel(ICON1);
        ReadableWritableProperty<Icon> iconProperty = new JLabelIconProperty(label);
        ValueChangeListener<Icon> listenerMock = (ValueChangeListener<Icon>) mock(ValueChangeListener.class);
        iconProperty.addValueChangeListener(listenerMock);

        assertEquals(ICON1, iconProperty.getValue());
        label.setIcon(null);
        assertEquals(null, iconProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(iconProperty, ICON1, null);
        verify(listenerMock).valueChanged(any(JLabelIconProperty.class), any(Icon.class), any(Icon.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JLabel label = new JLabel(ICON1);
        ReadableWritableProperty<Icon> iconProperty = new JLabelIconProperty(label);
        ValueChangeListener<Icon> listenerMock = (ValueChangeListener<Icon>) mock(ValueChangeListener.class);
        iconProperty.addValueChangeListener(listenerMock);

        assertEquals(ICON1, iconProperty.getValue());
        label.setIcon(ICON2);
        assertEquals(ICON2, iconProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(iconProperty, ICON1, ICON2);
        verify(listenerMock).valueChanged(any(JLabelIconProperty.class), any(Icon.class), any(Icon.class));
    }
}
