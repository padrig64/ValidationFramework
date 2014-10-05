package com.google.code.validationframework.experimental.base.property;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapProxy<K, V> {

    private final Map<K, V> map;

    private final List<MapListener<K, V>> listeners = new ArrayList<MapListener<K, V>>();

    public MapProxy() {
        this(new HashMap<K, V>());
    }

    public MapProxy(HashMap<K, V> map) {
        this.map = map;
    }

    public void addListener(MapListener<K, V> listener) {
        listeners.add(listener);
    }

    public void removeListener(MapListener<K, V> listener) {
        listeners.remove(listener);
    }

    private void fireAdded(Map<K, V> added) {
        for (MapListener<K, V> listener : listeners) {
            listener.entriesAdded(added);
        }
    }

    private void fireChanged(Map<K, V> added) {
        for (MapListener<K, V> listener : listeners) {
            listener.entriesChanged(added);
        }
    }

    private void fireRemoved(Map<K, V> removed) {
        for (MapListener<K, V> listener : listeners) {
            listener.entriesRemoved(removed);
        }
    }

    public V put(K key, V value) {
        V previousValue = map.put(key, value);

        Map<K, V> added = new HashMap<K, V>();
        added.put(key, value);
        fireAdded(added);

        return previousValue;
    }

    public void putAll(Map<? extends K, ? extends V> entries) {
        Map<K, V> added = new HashMap<K, V>(entries);

        map.putAll(entries);
        fireAdded(added);
    }

    public V remove(K key) {
        V previousValue = map.remove(key);

        Map<K, V> removed = new HashMap<K, V>();
        removed.put(key, previousValue);
        fireRemoved(removed);

        return previousValue;
    }

    public void clear() {
        Map<K, V> removed = new HashMap<K, V>(map);
        removed.clear();
        fireRemoved(removed);
    }

    public int getSize() {
        return map.size();
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    public V get(Object key) {
        return map.get(key);
    }
}
