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

import com.google.code.validationframework.api.property.WritableSetProperty;

import java.util.Collection;

/**
 * Wrapper for set properties (typically both readable/writable) to make them appear as write-only.
 * <p>
 * This can be useful, for example, to return a write-only set property in a getter method that is actually a
 * readable/writable set property internally. The wrapper then forbids the programmer to cast the returned set property
 * to a readable set property in order to change read its values.
 *
 * @param <W> Type of data that can be written to the wrapped set property.
 */
public class WriteOnlySetPropertyWrapper<W> implements WritableSetProperty<W> {

    /**
     * Wrapped set property.
     */
    private final WritableSetProperty<W> wrappedSetProperty;

    /**
     * Constructor specifying the set property to be wrapped, typically a set property that is both readable and
     * writable.
     *
     * @param wrappedSetProperty Set property to be wrapped.
     */
    public WriteOnlySetPropertyWrapper(WritableSetProperty<W> wrappedSetProperty) {
        this.wrappedSetProperty = wrappedSetProperty;
    }

    /**
     * @see WritableSetProperty#add(Object)
     */
    @Override
    public boolean add(W item) {
        return wrappedSetProperty.add(item);
    }

    /**
     * @see WritableSetProperty#addAll(Collection)
     */
    @Override
    public boolean addAll(Collection<? extends W> items) {
        return wrappedSetProperty.addAll(items);
    }

    /**
     * @see WritableSetProperty#remove(Object)
     */
    @Override
    public boolean remove(Object item) {
        return wrappedSetProperty.remove(item);
    }

    /**
     * @see WritableSetProperty#removeAll(Collection)
     */
    @Override
    public boolean removeAll(Collection<?> items) {
        return wrappedSetProperty.removeAll(items);
    }

    /**
     * @see WritableSetProperty#retainAll(Collection)
     */
    @Override
    public boolean retainAll(Collection<?> items) {
        return wrappedSetProperty.retainAll(items);
    }

    /**
     * @see WritableSetProperty#clear()
     */
    @Override
    public void clear() {
        wrappedSetProperty.clear();
    }
}
