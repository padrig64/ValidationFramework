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

import com.google.code.validationframework.base.property.AbstractReadableListProperty;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see ReadOnlyListPropertyWrapper
 */
public class ReadOnlyListPropertyWrapperTest {

    @Test
    public void defaultDeepDispose() {
        AbstractReadableListProperty<Integer> wrapped = mock(AbstractReadableListProperty.class);
        ReadOnlyListPropertyWrapper<Integer> wrapper = new ReadOnlyListPropertyWrapper<Integer>(wrapped);

        assertTrue(wrapper.getDeepDispose());

        wrapper.dispose();
        wrapper.dispose();
        wrapper.dispose();

        verify(wrapped).dispose();
    }

    @Test
    public void setDeepDispose() {
        AbstractReadableListProperty<Integer> wrapped = mock(AbstractReadableListProperty.class);
        ReadOnlyListPropertyWrapper<Integer> wrapper = new ReadOnlyListPropertyWrapper<Integer>(wrapped);
        wrapper.setDeepDispose(true);

        assertTrue(wrapper.getDeepDispose());

        wrapper.dispose();
        wrapper.dispose();
        wrapper.dispose();

        verify(wrapped).dispose();
    }

    @Test
    public void setShallowDispose() {
        AbstractReadableListProperty<Integer> wrapped = mock(AbstractReadableListProperty.class);
        ReadOnlyListPropertyWrapper<Integer> wrapper = new ReadOnlyListPropertyWrapper<Integer>(wrapped);
        wrapper.setDeepDispose(false);

        assertFalse(wrapper.getDeepDispose());

        wrapper.dispose();
        wrapper.dispose();
        wrapper.dispose();

        verify(wrapped, times(0)).dispose();
    }
}
