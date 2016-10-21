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

import com.google.code.validationframework.api.common.DeepDisposable;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.WritableMapProperty;

import java.util.Map;

/**
 * Wrapper for map properties (typically both readable/writable) to make them appear as write-only.
 * <p/>
 * This can be useful, for example, to return a write-only map property in a getter method that is actually a
 * readable/writable map property internally. The wrapper then forbids the programmer to cast the returned map property
 * to a readable map property in order to change read its values.
 *
 * @param <K> Type of keys maintained by this map property.
 * @param <W> Type of values that can be written to this map property.
 */
public class WriteOnlyMapPropertyWrapper<K, W> implements WritableMapProperty<K, W>, DeepDisposable {

    /**
     * Wrapped map property.
     */
    private WritableMapProperty<K, W> wrappedMapProperty;

    /**
     * True to dispose the wrapped property upon {@link #dispose()}, false otherwise.
     */
    private boolean deepDispose = true;

    /**
     * Constructor specifying the map property to be wrapped, typically a map property that is both readable and
     * writable.
     *
     * @param wrappedMapProperty Map property to be wrapped.
     */
    public WriteOnlyMapPropertyWrapper(WritableMapProperty<K, W> wrappedMapProperty) {
        this.wrappedMapProperty = wrappedMapProperty;
    }

    /**
     * @see DeepDisposable#getDeepDispose()
     */
    @Override
    public boolean getDeepDispose() {
        return deepDispose;
    }

    /**
     * @see DeepDisposable#setDeepDispose(boolean)
     */
    @Override
    public void setDeepDispose(boolean deepDispose) {
        this.deepDispose = deepDispose;
    }

    /**
     * @see DeepDisposable#dispose()
     */
    @Override
    public void dispose() {
        if (deepDispose && (wrappedMapProperty instanceof Disposable)) {
            ((Disposable) wrappedMapProperty).dispose();
        }
        wrappedMapProperty = null;
    }

    /**
     * @see WritableMapProperty#put(Object, Object)
     */
    @Override
    public W put(K key, W value) {
        return wrappedMapProperty.put(key, value);
    }

    /**
     * @see WritableMapProperty#remove(Object)
     */
    @Override
    public W remove(Object key) {
        return wrappedMapProperty.remove(key);
    }

    /**
     * @see WritableMapProperty#putAll(Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends W> entries) {
        wrappedMapProperty.putAll(entries);
    }

    /**
     * @see WritableMapProperty#clear()
     */
    @Override
    public void clear() {
        wrappedMapProperty.clear();
    }
}
