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

package com.google.code.validationframework.base.trigger;

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;
import com.google.code.validationframework.base.property.simple.SimpleDoubleProperty;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see PropertyValueChangeTrigger
 */
public class PropertyValueChangeTriggerTest {

    @Test
    public void testOneTrigger() {
        ReadableWritableProperty<Double, Double> property = new SimpleDoubleProperty(0.0);
        Trigger trigger = new PropertyValueChangeTrigger(property);

        TriggerListener listener = mock(TriggerListener.class);
        trigger.addTriggerListener(listener);

        property.setValue(0.0);
        property.setValue(1.0);
        property.setValue(1.0);

        verify(listener, times(1)).triggerValidation(any(TriggerEvent.class));
    }

    @Test
    public void testTwoTriggers() {
        ReadableWritableProperty<Double, Double> property = new SimpleDoubleProperty(0.0);
        Trigger trigger = new PropertyValueChangeTrigger(property);

        TriggerListener listener = mock(TriggerListener.class);
        trigger.addTriggerListener(listener);

        property.setValue(0.0);
        property.setValue(1.0);
        property.setValue(1.0);
        property.setValue(2.0);
        property.setValue(2.0);

        verify(listener, times(2)).triggerValidation(any(TriggerEvent.class));
    }

    @Test
    public void testThreeTriggers() {
        ReadableWritableProperty<Double, Double> property = new SimpleDoubleProperty(0.0);
        Trigger trigger = new PropertyValueChangeTrigger(property);

        TriggerListener listener = mock(TriggerListener.class);
        trigger.addTriggerListener(listener);

        property.setValue(0.0);
        property.setValue(1.0);
        property.setValue(1.0);
        property.setValue(2.0);
        property.setValue(2.0);
        property.setValue(null);
        property.setValue(null);

        verify(listener, times(3)).triggerValidation(any(TriggerEvent.class));
    }
}
