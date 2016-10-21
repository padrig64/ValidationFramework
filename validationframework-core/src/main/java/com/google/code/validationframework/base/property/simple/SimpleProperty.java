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

package com.google.code.validationframework.base.property.simple;

import com.google.code.validationframework.base.property.AbstractReadableWritableProperty;

/**
 * Simple implementation of a property that is both readable and writable.
 * <p/>
 * Slaved properties will only be updated if the new value set on this property differs from the previous one.
 * <p/>
 * Note that binding can be bi-directional. Infinite recursion will be prevented.
 * <p/>
 * Finally note that this class is not thread-safe.
 *
 * @param <T> Type of data that can be read from and written to this property.
 */
public class SimpleProperty<T> extends AbstractReadableWritableProperty<T> {

    /**
     * Property value.
     */
    private T value = null;

    /**
     * Default constructor using null as the initial property value.
     */
    public SimpleProperty() {
        this(null);
    }

    /**
     * Constructor specifying the initial property value.
     *
     * @param value Initial property value.
     */
    public SimpleProperty(T value) {
        super();
        this.value = value;
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public T getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(T value) {
        if (!isNotifyingListeners()) {
            // Update slaves only if the new value is different than the previous value
            T oldValue = this.value;
            this.value = value;
            maybeNotifyListeners(oldValue, value);
        }
    }
}
