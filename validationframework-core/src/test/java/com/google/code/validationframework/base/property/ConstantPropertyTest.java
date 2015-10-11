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

package com.google.code.validationframework.base.property;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @see ConstantProperty
 */
public class ConstantPropertyTest {

    @Test
    public void testConstant() {
        ReadableProperty<Integer> property = new ConstantProperty<Integer>(5);
        ValueChangeListener<Integer> listener1 = mock(ValueChangeListener.class);
        ValueChangeListener<Integer> listener2 = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener1);
        property.addValueChangeListener(listener2);
        property.removeValueChangeListener(listener2);

        assertEquals(Integer.valueOf(5), property.getValue());
        assertNotEquals(Integer.valueOf(6), property.getValue());
        verifyZeroInteractions(listener1);
        verifyZeroInteractions(listener2);
    }

    @Test
    public void testAwkward() {
        Set<String> value = new HashSet<String>();
        value.add("One");
        value.add("Two");
        value.add("Three");

        ReadableProperty<Set<?>> property = new ConstantProperty<Set<?>>(value);
        ValueChangeListener<Set<?>> listener1 = mock(ValueChangeListener.class);
        ValueChangeListener<Set<?>> listener2 = mock(ValueChangeListener.class);
        property.addValueChangeListener(listener1);
        property.addValueChangeListener(listener2);
        property.removeValueChangeListener(listener2);

        value.add("Four");
        value.add("Five");
        value.add("Six");

        assertEquals(value, property.getValue());
        assertNotEquals(new HashSet<String>(), property.getValue());
        verifyZeroInteractions(listener1);
        verifyZeroInteractions(listener2);
    }
}
