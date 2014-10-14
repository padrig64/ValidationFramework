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

import com.google.code.validationframework.base.utils.ValueUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper for {@link Map}s to make them observable.
 * <p/>
 * Any change to the map using this proxy will notify the registered {@link MapProxyListener}s.
 * <p/>
 * Note that any change to the proxied map directly will not notified the {@link MapProxyListener}s registered in this
 * proxy.
 *
 * @param <K> Type of keys maintained by the proxied map.
 * @param <V> Type mapped values.
 *
 * @see MapProxyListener
 */
public class MapProxy<K, V> implements Map<K, V> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MapProxy.class);

    /**
     * Proxied map.
     */
    private final Map<K, V> map;

    /**
     * Read-only version of the proxied map.
     */
    private final Map<K, V> readOnlyMap;

    /**
     * Listeners to changes on the proxied map.
     */
    private final List<MapProxyListener<K, V>> listeners = new ArrayList<MapProxyListener<K, V>>();

    /**
     * Default constructor using a {@link HashMap}.
     */
    public MapProxy() {
        this(new HashMap<K, V>());
    }

    /**
     * Default constructor using a {@link HashMap} and adding the specified map proxy listeners.
     *
     * @param listeners Map proxy listeners to be added.
     */
    public MapProxy(MapProxyListener<K, V>... listeners) {
        this(new HashMap<K, V>(), listeners);
    }

    /**
     * Constructor specifying the map to be proxied.
     *
     * @param map Proxied map.
     */
    public MapProxy(Map<K, V> map) {
        this.map = map;
        this.readOnlyMap = Collections.unmodifiableMap(map);
    }

    /**
     * Constructor specifying the map to be proxied and adding the specified map proxy listeners.
     *
     * @param map       Proxied map.
     * @param listeners Map proxy listeners to be added.
     */
    public MapProxy(Map<K, V> map, MapProxyListener<K, V>... listeners) {
        this.map = map;
        this.readOnlyMap = Collections.unmodifiableMap(map);
        Collections.addAll(this.listeners, listeners);
    }

    /**
     * Gets a read-only version of the proxied map.
     *
     * @return Read-only version of the proxied map.
     */
    public Map<K, V> getReadOnlyMap() {
        return readOnlyMap;
    }

    /**
     * Adds the specified map proxy listener.
     *
     * @param listener Map proxy listener to be added.
     */
    public void addMapProxyListener(MapProxyListener<K, V> listener) {
        listeners.add(listener);
    }

    /**
     * Removes the specified proxy listener.
     *
     * @param listener Map proxy listener to be removed.
     */
    public void removeMapProxyListener(MapProxyListener<K, V> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the map proxy listeners that entries have been added to the proxied map.
     *
     * @param added Entries added to the proxied map.
     */
    private void fireAdded(Map<K, V> added) {
        for (MapProxyListener<K, V> listener : listeners) {
            listener.entriesAdded(this, added);
        }
    }

    /**
     * Notifies the map proxy listeners that entries have been changed in the proxied map.
     *
     * @param changed Entries changed in the proxied map.
     */
    private void fireChanged(Map<K, V> changed) {
        for (MapProxyListener<K, V> listener : listeners) {
            listener.entriesChanged(this, changed);
        }
    }

    /**
     * Notifies the map proxy listeners that entries have been removed form the proxied map.
     *
     * @param removed Entries removed from the proxied map.
     */
    private void fireRemoved(Map<K, V> removed) {
        for (MapProxyListener<K, V> listener : listeners) {
            listener.entriesRemoved(this, removed);
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
        V previousValue = map.put(key, value);

        if (alreadyExists) {
            // Changed existing entry
            if (!ValueUtils.areEqual(previousValue, value)) {
                Map<K, V> changed = new HashMap<K, V>();
                changed.put(key, value);
                fireChanged(changed);
            }
        } else {
            // Added new entry
            Map<K, V> added = new HashMap<K, V>();
            added.put(key, value);
            fireAdded(added);
        }

        return previousValue;
    }

    /**
     * @see Map#putAll(Map)
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> entries) {
        Map<K, V> added = new HashMap<K, V>();
        Map<K, V> changed = new HashMap<K, V>();

        for (Entry<? extends K, ? extends V> entry : entries.entrySet()) {
            boolean alreadyExists = map.containsKey(entry.getKey());
            V previousValue = map.put(entry.getKey(), entry.getValue());

            if (alreadyExists) {
                // Changed existing entry
                if (!ValueUtils.areEqual(previousValue, entry.getValue())) {
                    changed.put(entry.getKey(), entry.getValue());
                }
            } else {
                // Added new entry
                added.put(entry.getKey(), entry.getValue());
            }
        }

        // Notify the listeners
        if (!added.isEmpty()) {
            fireAdded(added);
        }
        if (!changed.isEmpty()) {
            fireChanged(changed);
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
                fireRemoved(removed);
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
            fireRemoved(removed);
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
}
