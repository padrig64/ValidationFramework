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

package com.google.code.validationframework.swing.property.wrap;

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import com.google.code.validationframework.base.transform.OrBooleanAggregator;
import org.junit.Ignore;
import org.junit.Test;

import static com.google.code.validationframework.base.binding.Binder.read;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @see InvokeLaterPropertyWrapper
 */
public class InvokeLaterPropertyWrapperTest {

    @Ignore
    @Test
    public void testNoEventFired() throws InterruptedException {
        ReadableWritableProperty<Boolean, Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
        ReadableWritableProperty<Boolean, Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);
        ReadableWritableProperty<Boolean, Boolean> orRolloverProperty = new SimpleBooleanProperty(false);
        read(rolloverProperty1, rolloverProperty2).transform(new OrBooleanAggregator()).write(orRolloverProperty);

        ReadableWritableProperty<Boolean, Boolean> globalRolloverProperty = new SimpleBooleanProperty(false);
        read(new InvokeLaterPropertyWrapper<Boolean>(orRolloverProperty)).write(globalRolloverProperty);

        ValueChangeListener<Boolean> rolloverListener = mock(ValueChangeListener.class);
        globalRolloverProperty.addValueChangeListener(rolloverListener);

        rolloverProperty1.setValue(true);
        assertTrue(orRolloverProperty.getValue());
        rolloverProperty1.setValue(false);
        assertFalse(orRolloverProperty.getValue());
        rolloverProperty2.setValue(true);
        assertTrue(orRolloverProperty.getValue());
        rolloverProperty2.setValue(false);
        assertFalse(orRolloverProperty.getValue());

        Thread.sleep(2000);

        assertFalse(globalRolloverProperty.getValue());
        verifyZeroInteractions(rolloverListener);
    }

    @Ignore
    @Test
    public void testEventsFired() throws InterruptedException {
        ReadableWritableProperty<Boolean, Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
        ReadableWritableProperty<Boolean, Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);
        ReadableWritableProperty<Boolean, Boolean> orRolloverProperty = new SimpleBooleanProperty(false);
        read(rolloverProperty1, rolloverProperty2).transform(new OrBooleanAggregator()).write(orRolloverProperty);

        ReadableWritableProperty<Boolean, Boolean> globalRolloverProperty = new SimpleBooleanProperty(false);
        read(new InvokeLaterPropertyWrapper<Boolean>(orRolloverProperty)).write(globalRolloverProperty);

        ValueChangeListener<Boolean> rolloverListener = mock(ValueChangeListener.class);
        globalRolloverProperty.addValueChangeListener(rolloverListener);

        rolloverProperty1.setValue(true);
        assertTrue(orRolloverProperty.getValue());
        rolloverProperty1.setValue(false);
        assertFalse(orRolloverProperty.getValue());
        rolloverProperty2.setValue(true);
        assertTrue(orRolloverProperty.getValue());

        Thread.sleep(2000);
        assertTrue(globalRolloverProperty.getValue());

        rolloverProperty2.setValue(false);
        assertFalse(orRolloverProperty.getValue());
        rolloverProperty1.setValue(true);
        assertTrue(orRolloverProperty.getValue());
        assertTrue(globalRolloverProperty.getValue());

        Thread.sleep(2000);
        assertTrue(globalRolloverProperty.getValue());

        rolloverProperty1.setValue(false);
        assertFalse(orRolloverProperty.getValue());

        Thread.sleep(2000);
        assertFalse(globalRolloverProperty.getValue());

        verify(rolloverListener, times(1)).valueChanged(globalRolloverProperty, false, true);
        verify(rolloverListener, times(0)).valueChanged(globalRolloverProperty, true, false);
        verifyNoMoreInteractions(rolloverListener);
    }
}
