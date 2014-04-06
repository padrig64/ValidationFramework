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

package com.google.code.validationframework.base.property.simple;

import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see SimpleProperty
 */
public class SimplePropertytTest {

    @Test
    public void testInitialValue() {
        SimpleProperty<Integer> property = new SimpleProperty<Integer>();
        assertEquals(null, property.getValue());

        property = new SimpleProperty<Integer>(5);
        assertEquals(Integer.valueOf(5), property.getValue());
    }

    @Test
    public void testReadWrite() {
        SimpleProperty<Double> property = new SimpleProperty<Double>();

        property.setValue(8.2);
        assertEquals(Double.valueOf(8.2), property.getValue());

        property.setValue(234.3245);
        assertEquals(Double.valueOf(234.3245), property.getValue());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testValueChangeEvent() {
        SimpleProperty<Integer> property = new SimpleProperty<Integer>();
        ValueChangeListener<Integer> listenerMock = (ValueChangeListener<Integer>) mock(ValueChangeListener.class);

        property.addValueChangeListener(listenerMock);
        property.setValue(3);
        property.setValue(4);

        // Check exactly two events fired
        verify(listenerMock).valueChanged(property, null, 3);
        verify(listenerMock).valueChanged(property, 3, 4);
        verify(listenerMock, times(2)).valueChanged(any(SimpleProperty.class), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInhibitAndValueChangeEvent() {
        SimpleProperty<Integer> property = new SimpleProperty<Integer>(null);
        ValueChangeListener<Integer> listenerMock = (ValueChangeListener<Integer>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        property.setInhibited(true);
        property.setValue(3);
        property.setValue(4);
        property.setInhibited(false);

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, null, 4);
        verify(listenerMock).valueChanged(any(SimpleProperty.class), anyInt(), anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testInhibitAndNoValueChangeEvent() {
        SimpleProperty<Integer> property = new SimpleProperty<Integer>(null);
        ValueChangeListener<Integer> listenerMock = (ValueChangeListener<Integer>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        property.setInhibited(true);
        property.setValue(3);
        property.setValue(4);
        property.setValue(null);
        property.setInhibited(false);

        // Check no event fired
        verify(listenerMock, never()).valueChanged(any(SimpleProperty.class), anyInt(), anyInt());
    }
}
