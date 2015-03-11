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

package com.google.code.validationframework.base.resulthandler;

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.base.property.simple.SimpleIntegerProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @see PropertyResultHandler
 */
public class PropertyResultHandlerTest {

    @Test
    public void testSettingNoValue() {
        ReadableWritableProperty<Integer, Integer> property = new SimpleIntegerProperty(1);
        ResultHandler<Integer> resultHandler = new PropertyResultHandler<Integer>(property); // Keep reference
        ValueChangeListener<Integer> listener = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertEquals(Integer.valueOf(1), property.getValue());
        verifyZeroInteractions(listener);
    }

    @Test
    public void testSettingOneValue() {
        ReadableWritableProperty<Integer, Integer> property = new SimpleIntegerProperty(1);
        ResultHandler<Integer> resultHandler = new PropertyResultHandler<Integer>(property);
        ValueChangeListener<Integer> listener = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertEquals(Integer.valueOf(1), property.getValue());

        resultHandler.handleResult(2);
        resultHandler.handleResult(2);
        assertEquals(Integer.valueOf(2), property.getValue());
        verify(listener, times(1)).valueChanged(property, 1, 2);
    }

    @Test
    public void testSettingTwoValues() {
        ReadableWritableProperty<Integer, Integer> property = new SimpleIntegerProperty(1);
        ResultHandler<Integer> resultHandler = new PropertyResultHandler<Integer>(property);
        ValueChangeListener<Integer> listener = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertEquals(Integer.valueOf(1), property.getValue());

        resultHandler.handleResult(2);
        resultHandler.handleResult(2);
        resultHandler.handleResult(3);
        resultHandler.handleResult(3);
        assertEquals(Integer.valueOf(3), property.getValue());
        verify(listener, times(1)).valueChanged(property, 1, 2);
        verify(listener, times(1)).valueChanged(property, 2, 3);
    }

    @Test
    public void testSettingThreeValues() {
        ReadableWritableProperty<Integer, Integer> property = new SimpleIntegerProperty(1);
        ResultHandler<Integer> resultHandler = new PropertyResultHandler<Integer>(property);
        ValueChangeListener<Integer> listener = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertEquals(Integer.valueOf(1), property.getValue());

        resultHandler.handleResult(2);
        resultHandler.handleResult(2);
        resultHandler.handleResult(3);
        resultHandler.handleResult(3);
        resultHandler.handleResult(null);
        resultHandler.handleResult(null);
        assertEquals(null, property.getValue());
        verify(listener, times(1)).valueChanged(property, 1, 2);
        verify(listener, times(1)).valueChanged(property, 2, 3);
        verify(listener, times(1)).valueChanged(property, 3, null);
    }
}
