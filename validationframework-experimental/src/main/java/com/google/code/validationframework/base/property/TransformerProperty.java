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

import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.utils.ValueUtils;

/**
 * Implementation of a readable and writable property whose output is the result of the transformation of the input.
 * <p/>
 * Listeners will only be notified if the new output value resulting from a new input value differs from the previous
 * one.
 * <p/>
 * Note that binding can be bi-directional. Infinite recursion will be prevented.
 * <p/>
 * Finally note that this class is not thread-safe.
 *
 * @param <R> Type of data that can be read from this property.
 * @param <W> Type of data that can be written from this property.
 */
public class TransformerProperty<R, W> extends AbstractReadableWritableProperty<R, W> {

    /**
     * Transformer to be used to transform input values.
     */
    private Transformer<W, R> transformer;

    /**
     * Property output value.
     */
    private R value = null;

    /**
     * Flag used to avoid any infinite recursion.
     */
    private boolean settingValue = false;

    /**
     * Constructor specifying the transformer to be used to transform input values, using null as the initial input
     * value.
     *
     * @param transformer Transformer to transform input values.
     */
    public TransformerProperty(Transformer<W, R> transformer) {
        this(transformer, null);
    }

    /**
     * Constructor specifying the transformer to be used to transform input values, and the initial input value.
     *
     * @param transformer Transformer to transform input values.
     * @param value       Initial property value.
     */
    public TransformerProperty(Transformer<W, R> transformer, W value) {
        super();
        this.transformer = transformer;
        this.value = transformer.transform(value);
    }

    /**
     * @see AbstractReadableProperty#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (transformer != null) {
            transformer = null;
        }
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public R getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(W value) {
        if ((transformer != null) && !settingValue) {
            settingValue = true;

            // Update slaves only if the new value is different than the previous value
            R newValue = transformer.transform(value);
            if (!ValueUtils.areEqual(this.value, newValue)) {
                R oldValue = this.value;
                this.value = newValue;
                maybeNotifyListeners(oldValue, newValue);
            }

            settingValue = false;
        }
    }
}
