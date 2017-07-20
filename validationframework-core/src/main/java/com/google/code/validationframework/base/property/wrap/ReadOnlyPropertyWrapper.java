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
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableProperty;

/**
 * Wrapper for properties (typically both readable/writable) to make them appear as read-only.
 * <p>
 * This can be useful, for example, to return a read-only property in a getter method that is actually a
 * readable/writable property internally. The wrapper then forbids the programmer to cast the returned property to a
 * writable property in order to change its value.
 *
 * @param <R> Type of data that can be read from the wrapped property.
 */
public class ReadOnlyPropertyWrapper<R> extends AbstractReadableProperty<R> implements Disposable {

    /**
     * Entity responsible for forwarding the change events from the wrapped property to the listeners of the read-only
     * wrapper.
     */
    private class ValueChangeForwarder implements ValueChangeListener<R> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<R> property, R oldValue, R newValue) {
            maybeNotifyListeners(oldValue, newValue);
        }
    }

    /**
     * Wrapped property.
     */
    private final ReadableProperty<R> wrappedProperty;

    /**
     * Listener to changes on the wrapped property.
     */
    private final ValueChangeListener<R> changeAdapter = new ValueChangeForwarder();

    /**
     * Constructor specifying the property to be wrapped, typically a property that is both readable and writable.
     *
     * @param wrappedProperty Property to be wrapped.
     */
    public ReadOnlyPropertyWrapper(ReadableProperty<R> wrappedProperty) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addValueChangeListener(changeAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public R getValue() {
        return wrappedProperty.getValue();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        wrappedProperty.removeValueChangeListener(changeAdapter);
    }
}
