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

package com.google.code.validationframework.base.property.simple;

import com.google.code.validationframework.api.property.MapValueChangeListener;
import com.google.code.validationframework.base.property.AbstractReadableWritableMapProperty;
import com.google.code.validationframework.base.utils.ValueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Readable/writable map property backed by a {@link Map}.
 *
 * @param <K> Type of keys maintained by this map property and the proxied map.
 * @param <V> Type mapped values.
 */
public class SimpleMapProperty<K, V> extends AbstractReadableWritableMapProperty<K, V, V> implements Map<K, V> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleMapProperty.class);

    /**
     * Proxied map.
     */
    private final Map<K, V> map = new HashMap<K, V>();

    /**
     * Read-only version of the proxied map.
     */
    private final Map<K, V> readOnlyMap = Collections.unmodifiableMap(map);

    /**
     * Default constructor.
     */
    public SimpleMapProperty() {
        super();
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    public SimpleMapProperty(MapValueChangeListener<K, V>... listeners) {
        super(listeners);
    }

    /**
     * Constructor specifying the initial entries.
     *
     * @param entries Initial entries.
     */
    public SimpleMapProperty(Map<K, V> entries) {
        super();
        map.putAll(entries);
    }

    /**
     * Constructor specifying the initial entries and adding the specified listeners.
     * <p/>
     * Note that the specified listeners will not be notified for the addition of the specified initial entries.
     *
     * @param entries   Initial entries.
     * @param listeners Listeners to be added.
     */
    public SimpleMapProperty(Map<K, V> entries, MapValueChangeListener<K, V>... listeners) {
        super(); // Without listeners

        map.putAll(entries);

        for (MapValueChangeListener<K, V> listener : listeners) {
            addValueChangeListener(listener);
        }
    }

    /**
     * @see Map#size()
     */
    @Override
    public int size() {
        return map.size();
    }

    /**
     * @see Map#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * @see Map#containsKey(Object)
     */
    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /**
     * @see Map#containsValue(Object)
     */
    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /**
     * @see Map#get(Object)
     */
    @Override
    public V get(Object key) {
        return map.get(key);
    }

    /**
     * @see Map#put(Object, Object)
     */
    @Override
    public V put(K key, V value) {
        boolean alreadyExists = map.containsKey(key);
        V oldValue = map.put(key, value);

        if (alreadyExists) {
            // Changed existing entry
            if (!ValueUtils.areEqual(oldValue, value)) {
                Map<K, V> oldValues = new HashMap<K, V>();
                oldValues.put(key, oldValue);
                Map<K, V> newValues = new HashMap<K, V>();
                newValues.put(key, value);
                doNotifyListenersOfChangedValues(oldValues, newValues);
            }
        } else {
            // Added new entry
            Map<K, V> added = new HashMap<K, V>();
            added.put(key, value);
            doNotifyListenersOfAddedValues(added);
        }

        return oldValue;
    }

    /**
     * @see Map#putAll(Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> entries) {
        Map<K, V> newAddedValues = new HashMap<K, V>();
        Map<K, V> changedOldValues = new HashMap<K, V>();
        Map<K, V> changedNewValues = new HashMap<K, V>();

        for (Entry<? extends K, ? extends V> entry : entries.entrySet()) {
            boolean alreadyExists = map.containsKey(entry.getKey());
            V oldValue = map.put(entry.getKey(), entry.getValue());

            if (alreadyExists) {
                // Changed existing entry
                if (!ValueUtils.areEqual(oldValue, entry.getValue())) {
                    changedOldValues.put(entry.getKey(), oldValue);
                    changedNewValues.put(entry.getKey(), entry.getValue());
                }
            } else {
                // Added new entry
                newAddedValues.put(entry.getKey(), entry.getValue());
            }
        }

        // Notify the listeners
        if (!newAddedValues.isEmpty()) {
            doNotifyListenersOfAddedValues(newAddedValues);
        }
        if (!changedNewValues.isEmpty()) {
            doNotifyListenersOfChangedValues(changedOldValues, changedNewValues);
        }
    }

    /**
     * @see Map#remove(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public V remove(Object key) {
        boolean exists = map.containsKey(key);
        V previousValue = null;

        if (exists) {
            previousValue = map.remove(key);

            try {
                Map<K, V> removed = new HashMap<K, V>();
                removed.put((K) key, previousValue);
                doNotifyListenersOfRemovedValues(removed);
            } catch (ClassCastException e) {
                // Just in case
                LOGGER.error("Inconsistent type of key: " + key);
            }
        }

        return previousValue;
    }

    /**
     * @see Map#clear()
     */
    @Override
    public void clear() {
        if (!map.isEmpty()) {
            Map<K, V> removed = new HashMap<K, V>(map);
            map.clear();
            doNotifyListenersOfRemovedValues(removed);
        }
    }

    /**
     * Gets a set containing all keys in the read-only version of the proxied map.
     *
     * @return Key set that cannot be used to modify the proxied map.
     *
     * @see Map#keySet()
     */
    @Override
    public Set<K> keySet() {
        return readOnlyMap.keySet();
    }

    /**
     * Gets a collection containing all values in the read-only version of the proxied map.
     *
     * @return Value collection that cannot be used to modified the proxied map.
     *
     * @see Map#values()
     */
    @Override
    public Collection<V> values() {
        return readOnlyMap.values();
    }

    /**
     * Gets a set containing all entries in the read-only version of the proxied map.
     *
     * @return Entry set that cannot be use to modified the proxied map.
     *
     * @see Map#entrySet()
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return readOnlyMap.entrySet();
    }

    /**
     * @see AbstractReadableWritableMapProperty#asUnmodifiableMap()
     */
    @Override
    public Map<K, V> asUnmodifiableMap() {
        return readOnlyMap;
    }
}
