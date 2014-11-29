/*
 * Copyright (c) 2014, Patrick Moawad
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

import com.google.code.validationframework.api.property.MapValueChangeListener;
import com.google.code.validationframework.api.property.ReadableMapProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of a {@link ReadableMapProperty}.
 *
 * @param <K> Type of keys maintained by this map property.
 * @param <R> Type of mapped values in this map property.
 */
public abstract class AbstractReadableMapProperty<K, R> implements ReadableMapProperty<K, R> {

    /**
     * Listeners to changes in the list property.
     */
    private final List<MapValueChangeListener<K, R>> listeners = new ArrayList<MapValueChangeListener<K, R>>();

    /**
     * @see ReadableMapProperty#addValueChangeListener(MapValueChangeListener)
     */
    @Override
    public void addValueChangeListener(MapValueChangeListener<K, R> listener) {
        listeners.add(listener);
    }

    /**
     * @see ReadableMapProperty#removeValueChangeListener(MapValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(MapValueChangeListener<K, R> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the change listeners that values have been added.
     * <p/>
     * Note that the specified map of values will be wrapped in an unmodifiable map before being passed to the
     * listeners.
     *
     * @param newValues Newly added values.
     */
    protected void doNotifyListenersOfAddedValues(Map<K, R> newValues) {
        Map<K, R> unmodifiable = Collections.unmodifiableMap(newValues);
        for (MapValueChangeListener<K, R> listener : listeners) {
            listener.valuesAdded(this, unmodifiable);
        }
    }

    /**
     * Notifies the change listeners that values have been replaced.
     * <p/>
     * Note that the specified maps of values will be wrapped in unmodifiable maps before being passed to the listeners.
     *
     * @param oldValues Previous values.
     * @param newValues New values.
     */
    protected void doNotifyListenersOfChangedValues(Map<K, R> oldValues, Map<K, R> newValues) {
        Map<K, R> oldUnmodifiable = Collections.unmodifiableMap(oldValues);
        Map<K, R> newUnmodifiable = Collections.unmodifiableMap(newValues);
        for (MapValueChangeListener<K, R> listener : listeners) {
            listener.valuesChanged(this, oldUnmodifiable, newUnmodifiable);
        }
    }

    /**
     * Notifies the change listeners that values have been removed.
     * <p/>
     * Note that the specified map of values will be wrapped in an unmodifiable map before being passed to the
     * listeners.
     *
     * @param oldValues Removed values.
     */
    protected void doNotifyListenersOfRemovedValues(Map<K, R> oldValues) {
        Map<K, R> unmodifiable = Collections.unmodifiableMap(oldValues);
        for (MapValueChangeListener<K, R> listener : listeners) {
            listener.valuesRemoved(this, unmodifiable);
        }
    }
}
