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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.SwingUtilities;

/**
 * Wrapper for {@link ReadableProperty} that postpones the notifications of the {@link ValueChangeListener}s later on
 * the EDT.
 *
 * @param <R> Type of data that can be read from this property and the wrapped property.
 */
public class InvokeLaterPropertyWrapper<R> extends AbstractReadableProperty<R> implements Disposable {

    /**
     * Listener to value changes of the wrapped property and notifying the listeners of this property later on the EDT.
     */
    private class ValueChangeAdapter implements ValueChangeListener<R>, Runnable {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         * @see SwingUtilities#invokeLater(Runnable)
         */
        @Override
        public void valueChanged(ReadableProperty<R> property, R oldValue, R newValue) {
            SwingUtilities.invokeLater(this);
        }

        /**
         * @see Runnable#run()
         */
        @Override
        public void run() {
            R oldValue = value;
            value = getValue();
            maybeNotifyListeners(oldValue, value);
        }
    }

    /**
     * Wrapped property.
     */
    private ReadableProperty<R> wrappedProperty = null;

    /**
     * Entity postponing value change events to later on the EDT.
     */
    private final ValueChangeListener<R> valueChangeAdapter = new ValueChangeAdapter();

    /**
     * Last value notified.
     */
    private R value = null;

    /**
     * Constructor specifying the property to be wrapped.
     *
     * @param wrappedProperty {@link ReadableProperty} to be wrapped.
     */
    public InvokeLaterPropertyWrapper(ReadableProperty<R> wrappedProperty) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addValueChangeListener(valueChangeAdapter);
        this.value = wrappedProperty.getValue();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (wrappedProperty != null) {
            wrappedProperty.removeValueChangeListener(valueChangeAdapter);
            wrappedProperty = null;
        }
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public R getValue() {
        return wrappedProperty.getValue();
    }
}
