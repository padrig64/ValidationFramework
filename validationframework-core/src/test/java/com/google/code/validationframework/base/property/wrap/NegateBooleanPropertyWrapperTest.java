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

package com.google.code.validationframework.base.property.wrap;

import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see NegateBooleanPropertyWrapper
 */
public class NegateBooleanPropertyWrapperTest {

    @Test
    public void wrap() {
        SimpleBooleanProperty wrapped = new SimpleBooleanProperty(false);
        NegateBooleanPropertyWrapper wrapper = new NegateBooleanPropertyWrapper(wrapped);
        ValueChangeListener<Boolean> listener = mock(ValueChangeListener.class);
        wrapper.addValueChangeListener(listener);

        assertTrue(wrapper.getValue());

        wrapped.setValue(true);
        assertFalse(wrapper.getValue());

        wrapped.setValue(null);
        assertNull(wrapper.getValue());

        wrapped.setValue(false);
        assertTrue(wrapper.getValue());

        verify(listener).valueChanged(wrapper, true, false);
        verify(listener).valueChanged(wrapper, false, null);
        verify(listener).valueChanged(wrapper, null, true);
    }

    @Test
    public void shallowDispose() {
        AbstractReadablePropertyWrapper<Boolean> wrapped = mock(AbstractReadablePropertyWrapper.class);
        NegateBooleanPropertyWrapper wrapper = new NegateBooleanPropertyWrapper(wrapped);

        wrapper.setDeepDispose(false);
        wrapper.dispose();
        wrapper.dispose();
        wrapper.dispose();

        verify(wrapped).addValueChangeListener(any(ValueChangeListener.class));
        verify(wrapped).removeValueChangeListener(any(ValueChangeListener.class));
        verifyNoMoreInteractions(wrapped);
    }

    @Test
    public void deepDispose() {
        AbstractReadablePropertyWrapper<Boolean> wrapped = mock(AbstractReadablePropertyWrapper.class);
        NegateBooleanPropertyWrapper wrapper = new NegateBooleanPropertyWrapper(wrapped);

        wrapper.setDeepDispose(true);
        wrapper.dispose();
        wrapper.dispose();
        wrapper.dispose();

        verify(wrapped).addValueChangeListener(any(ValueChangeListener.class));
        verify(wrapped).removeValueChangeListener(any(ValueChangeListener.class));
        verify(wrapped).dispose();
        verifyNoMoreInteractions(wrapped);
    }
}
