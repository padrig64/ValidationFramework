/*
 * Copyright (c) 2013, Patrick Moawad
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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.base.binding.ReadableProperty;
import com.google.code.validationframework.base.binding.ReadablePropertyChangeListener;

/**
 * Trigger initiating the validation whenever the value of a specified property changes.
 *
 * @param <T> Type of property value.
 */
public class PropertyTrigger<T> extends AbstractTrigger {

    /**
     * Entity tracking changes of the property and triggering the validation.
     */
    private class ChangeAdapter implements ReadablePropertyChangeListener<T> {

        /**
         * @see ReadablePropertyChangeListener#propertyChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void propertyChanged(ReadableProperty<T> property, T oldValue, T newValue) {
            fireTriggerEvent(new TriggerEvent(property));
        }
    }

    /**
     * Property whose changes should trigger the validation.
     */
    private final ReadableProperty<T> property;

    /**
     * Property change listener to trigger the validation.
     */
    private final ReadablePropertyChangeListener<T> propertyChangeAdapter = new ChangeAdapter();

    /**
     * Constructor specifying the property whose changes should trigger the validation.
     *
     * @param property Property whose changes should trigger the validation.
     */
    public PropertyTrigger(ReadableProperty<T> property) {
        super();

        this.property = property;
        this.property.addChangeListener(propertyChangeAdapter);
    }

    /**
     * @see AbstractTrigger#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();

        property.removeChangeListener(propertyChangeAdapter);
        if (property instanceof Disposable) {
            ((Disposable) property).dispose();
        }
    }
}
