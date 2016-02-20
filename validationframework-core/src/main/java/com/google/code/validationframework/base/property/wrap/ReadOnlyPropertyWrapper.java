/*
 * Copyright (c) 2016, ValidationFramework Authors
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

import com.google.code.validationframework.api.property.ReadableProperty;

/**
 * Wrapper for properties (typically both readable/writable) to make them appear as read-only.
 * <p/>
 * This can be useful, for example, to return a read-only property in a getter method that is actually a
 * readable/writable property internally. The wrapper then forbids the programmer to cast the returned property to a
 * writable property in order to change its value.
 *
 * @param <R> Type of data that can be read from the wrapped property.
 */
public class ReadOnlyPropertyWrapper<R> extends AbstractReadablePropertyWrapper<R> {

    /**
     * @see AbstractReadablePropertyWrapper#AbstractReadablePropertyWrapper(ReadableProperty)
     */
    public ReadOnlyPropertyWrapper(ReadableProperty<? extends R> wrappedProperty) {
        super(wrappedProperty);
    }

    /**
     * @see AbstractReadablePropertyWrapper#AbstractReadablePropertyWrapper(ReadableProperty, boolean)
     */
    public ReadOnlyPropertyWrapper(ReadableProperty<? extends R> wrappedProperty, boolean deepDispose) {
        super(wrappedProperty, deepDispose);
    }

    /**
     * @see AbstractReadablePropertyWrapper#wrappedPropertyValueChanged(ReadableProperty, Object, Object)
     */
    @Override
    protected void wrappedPropertyValueChanged(ReadableProperty<? extends R> property, R oldValue, R newValue) {
        // Just forward the property change
        maybeNotifyListeners(oldValue, newValue);
    }

    /**
     * @see AbstractReadablePropertyWrapper#getValue()
     */
    @Override
    public R getValue() {
        R value;
        if (wrappedProperty == null) {
            value = null;
        } else {
            value = wrappedProperty.getValue();
        }
        return value;
    }
}
