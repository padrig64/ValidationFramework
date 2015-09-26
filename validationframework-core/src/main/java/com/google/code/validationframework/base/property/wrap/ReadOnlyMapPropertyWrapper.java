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
import com.google.code.validationframework.api.property.MapValueChangeListener;
import com.google.code.validationframework.api.property.ReadableMapProperty;
import com.google.code.validationframework.base.property.AbstractReadableMapProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for map properties (typically both readable/writable) to make them appear as read-only.
 * <p/>
 * This can be useful, for example, to return a read-only map property in a getter method that is actually a
 * readable/writable map property internally. The wrapper then forbids the programmer to cast the returned map property
 * to a writable map property in order to change its contents.
 *
 * @param <K> Type of keys maintained by this map property.
 * @param <R> Type of mapped values in this map property.
 */
public class ReadOnlyMapPropertyWrapper<K, R> extends AbstractReadableMapProperty<K, R> implements DeepDisposable {

    /**
     * Listener to changes on the wrapped property.
     */
    private final MapValueChangeListener<K, R> changeAdapter = new MapValueChangeForwarder();

    private boolean deepDispose;

    /**
     * Wrapped map property.
     */
    private ReadableMapProperty<K, R> wrappedMapProperty;

    /**
     * Constructor specifying the map property to be wrapped, typically a map property that is both readable and
     * writable.
     * <p/>
     * The wrapped map property will be disposed whenever this map property is disposed.
     *
     * @param wrappedMapProperty Map property to be wrapped.
     */
    public ReadOnlyMapPropertyWrapper(ReadableMapProperty<K, R> wrappedMapProperty) {
        this(wrappedMapProperty, true);
    }

    /**
     * Constructor specifying the map property to be wrapped, typically a map property that is both readable and
     * writable.
     *
     * @param wrappedMapProperty Map property to be wrapped.
     * @param deepDispose        True to dispose the wrapped map property whenever this map property is disposed, false
     *                           otherwise.
     */
    public ReadOnlyMapPropertyWrapper(ReadableMapProperty<K, R> wrappedMapProperty, boolean deepDispose) {
        super();
        this.wrappedMapProperty = wrappedMapProperty;
        this.wrappedMapProperty.addValueChangeListener(changeAdapter);
        this.deepDispose = deepDispose;
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
        super.dispose();
        if (wrappedMapProperty != null) {
            wrappedMapProperty.removeValueChangeListener(changeAdapter);
            if (deepDispose && (wrappedMapProperty instanceof Disposable)) {
                ((Disposable) wrappedMapProperty).dispose();
            }
            wrappedMapProperty = null;
        }
    }

    /**
     * @see AbstractReadableMapProperty#size()
     */
    @Override
    public int size() {
        int size;
        if (wrappedMapProperty == null) {
            size = 0;
        } else {
            size = wrappedMapProperty.size();
        }
        return size;
    }

    /**
     * @see AbstractReadableMapProperty#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return (wrappedMapProperty == null) || wrappedMapProperty.isEmpty();
    }

    /**
     * @see AbstractReadableMapProperty#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return (wrappedMapProperty != null) && wrappedMapProperty.containsKey(key);
    }

    /**
     * @see AbstractReadableMapProperty#containsValue(Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return (wrappedMapProperty != null) && wrappedMapProperty.containsValue(value);
    }

    /**
     * @see AbstractReadableMapProperty#get(Object)
     */
    @Override
    public R get(Object key) {
        R value;
        if (wrappedMapProperty == null) {
            value = null;
        } else {
            value = wrappedMapProperty.get(key);
        }
        return value;
    }

    /**
     * @see AbstractReadableMapProperty#keySet()
     */
    @Override
    public Set<K> keySet() {
        return asUnmodifiableMap().keySet();
    }

    /**
     * @see AbstractReadableMapProperty#values()
     */
    @Override
    public Collection<R> values() {
        return asUnmodifiableMap().values();
    }

    /**
     * @see AbstractReadableMapProperty#entrySet()
     */
    @Override
    public Set<Map.Entry<K, R>> entrySet() {
        return asUnmodifiableMap().entrySet();
    }

    /**
     * @see AbstractReadableMapProperty#asUnmodifiableMap()
     */
    @Override
    public Map<K, R> asUnmodifiableMap() {
        Map<K, R> unmodifiable;
        if (wrappedMapProperty == null) {
            unmodifiable = Collections.emptyMap();
        } else {
            unmodifiable = wrappedMapProperty.asUnmodifiableMap();
        }
        return unmodifiable;
    }

    /**
     * Entity responsible for forwarding the change events from the wrapped map property to the listeners of the
     * read-only wrapper.
     */
    private class MapValueChangeForwarder implements MapValueChangeListener<K, R> {

        @Override
        public void valuesAdded(ReadableMapProperty<K, R> mapProperty, Map<K, R> newValues) {
            doNotifyListenersOfAddedValues(newValues);
        }

        @Override
        public void valuesChanged(ReadableMapProperty<K, R> mapProperty, Map<K, R> oldValues, Map<K, R> newValues) {
            doNotifyListenersOfChangedValues(oldValues, newValues);
        }

        @Override
        public void valuesRemoved(ReadableMapProperty<K, R> mapProperty, Map<K, R> oldValues) {
            doNotifyListenersOfRemovedValues(oldValues);
        }
    }
}
