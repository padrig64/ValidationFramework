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

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see JFrameTitleProperty
 */
@Deprecated
public class JFrameTitlePropertyTest {

    private static final String TITLE1 = "First Title";

    private static final String TITLE2 = "Second Title";

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromProperty() {
        JFrame frame = new JFrame(TITLE1);
        ReadableWritableProperty<String> titleProperty = new JFrameTitleProperty(frame);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        titleProperty.addValueChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        titleProperty.setValue(null);
        assertEquals("", frame.getTitle());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(titleProperty, TITLE1, "");
        verify(listenerMock).valueChanged(any(JFrameTitleProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JFrame frame = new JFrame(TITLE1);
        ReadableWritableProperty<String> titleProperty = new JFrameTitleProperty(frame);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        titleProperty.addValueChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        titleProperty.setValue(TITLE2);
        assertEquals(TITLE2, frame.getTitle());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(titleProperty, TITLE1, TITLE2);
        verify(listenerMock).valueChanged(any(JFrameTitleProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNullFromComponent() {
        JFrame frame = new JFrame(TITLE1);
        ReadableWritableProperty<String> titleProperty = new JFrameTitleProperty(frame);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        titleProperty.addValueChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        frame.setTitle(null);
        assertEquals("", titleProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(titleProperty, TITLE1, "");
        verify(listenerMock).valueChanged(any(JFrameTitleProperty.class), anyString(), anyString());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JFrame frame = new JFrame(TITLE1);
        ReadableWritableProperty<String> titleProperty = new JFrameTitleProperty(frame);
        ValueChangeListener<String> listenerMock = (ValueChangeListener<String>) mock(ValueChangeListener.class);
        titleProperty.addValueChangeListener(listenerMock);

        assertEquals(TITLE1, titleProperty.getValue());
        frame.setTitle(TITLE2);
        assertEquals(TITLE2, titleProperty.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(titleProperty, TITLE1, TITLE2);
        verify(listenerMock).valueChanged(any(JFrameTitleProperty.class), anyString(), anyString());
    }

    @Test
    public void testDispose() {
        JFrame frame = new JFrame(TITLE1);
        JFrameTitleProperty property = new JFrameTitleProperty(frame);
        ValueChangeListener<String> listener = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener);

        frame.setTitle(TITLE2);
        frame.setTitle(TITLE1);

        property.dispose();

        frame.setTitle(TITLE2);
        frame.setTitle(TITLE1);

        property.dispose();
        property.dispose();

        verify(listener).valueChanged(property, TITLE1, TITLE2);
        verify(listener).valueChanged(property, TITLE2, TITLE1);
        verifyNoMoreInteractions(listener);
    }
}
