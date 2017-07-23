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

package com.google.code.validationframework.base.trigger;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.api.trigger.TriggerEvent;

/**
 * Trigger initiating the validation whenever the value of a {@link ReadableProperty} has changed.
 *
 * @see AbstractTrigger
 * @see ReadableProperty
 */
public class PropertyValueChangeTrigger extends AbstractTrigger {

    /**
     * Listener to property change events and triggering the validation.
     */
    private class ValueChangeAdapter implements ValueChangeListener<Object> {

        @Override
        public void valueChanged(ReadableProperty<Object> property, Object oldValue, Object newValue) {
            fireTriggerEvent(new TriggerEvent(property));
        }
    }

    /**
     * Property whose value changes should trigger the validation.
     */
    private ReadableProperty<?> property = null;

    /**
     * Listener to property change events and triggering the validation.
     */
    private final ValueChangeListener<Object> changeAdapter;

    /**
     * Constructor specifying the property whose value changes should trigger the validation.
     *
     * @param property Property whose value changes should trigger the validation.
     */
    @SuppressWarnings("unchecked")
    public PropertyValueChangeTrigger(ReadableProperty<?> property) {
        super();
        this.property = property;
        this.changeAdapter = new ValueChangeAdapter();
        this.property.addValueChangeListener((ValueChangeListener) changeAdapter);
    }

    /**
     * @see AbstractTrigger#dispose()
     */
    @SuppressWarnings("unchecked")
    @Override
    public void dispose() {
        super.dispose();
        property.removeValueChangeListener((ValueChangeListener) changeAdapter);
        property = null;
    }
}
