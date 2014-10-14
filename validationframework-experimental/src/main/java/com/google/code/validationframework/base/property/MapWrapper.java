package com.google.code.validationframework.base.property;

import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapWrapper<K, V> implements Map<K, V> {

    private final Map<K, V> wrapped;

    public MapWrapper() {
        this(new HashMap<K, V>());
    }

    public MapWrapper(Map<K, V> wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public int size() {
        return wrapped.size();
    }

    @Override
    public boolean isEmpty() {
        return wrapped.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return wrapped.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return wrapped.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return wrapped.get(key);
    }

    @Override
    public V put(K key, V value) {
        boolean willBeModified = wrapped.containsKey(key);

        V oldValue = wrapped.put(key, value);

        if (willBeModified) {
            System.out.println("UPDATED: k=" + key + " ov=" + oldValue + " nv=" + value);
        } else {
            System.out.println("ADDED: k=" + key + " nv=" + value);
        }

        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        boolean willBeModified = wrapped.containsKey(key);
        V oldValue = wrapped.remove(key);

        if (willBeModified) {
            System.out.println("REMOVED: k=" + key + " ov=" + oldValue);
        }

        return oldValue;
    }

    @Override
    public void clear() {
        if (!wrapped.isEmpty()) {
            Map<K, V> oldEntries = new HashMap<K, V>(wrapped);
            wrapped.clear();
            System.out.println("CLEARED");
        }
    }

    @Override
    public Set<K> keySet() {
        return new KeySetWrapper(wrapped.keySet());
    }

    @Override
    public Collection<V> values() {
        return new ValuesCollectionWrapper(wrapped.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySetWrapper(wrapped.entrySet());
    }

    private class KeySetWrapper implements Set<K> {

        private final Set<K> wrapped;

        private KeySetWrapper(Set<K> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public int size() {
            return wrapped.size();
        }

        @Override
        public boolean isEmpty() {
            return wrapped.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return wrapped.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return wrapped.containsAll(c);
        }

        @Override
        public Object[] toArray() {
            return wrapped.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return wrapped.toArray(a);
        }

        @Override
        public Iterator<K> iterator() {
            return new KeySetIteratorWrapper(wrapped.iterator());
        }

        @Override
        public boolean add(K key) {
            // Cannot add key because no value can be specified
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends K> c) {
            // Cannot add keys because no value can be specified
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object key) {
            // Call remove() on the map so that event can be fired with all needed info
            boolean willBeModified = MapWrapper.this.containsKey(key);
            MapWrapper.this.remove(key);
            return willBeModified;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean modified = false;
            for (Object key : c) {
                modified |= remove(key);
            }
            return modified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            // TODO
            return wrapped.retainAll(c);
        }

        @Override
        public void clear() {
            MapWrapper.this.clear();
        }
    }

    private class KeySetIteratorWrapper implements Iterator<K> {

        private final Iterator<K> wrapped;

        private KeySetIteratorWrapper(Iterator<K> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public boolean hasNext() {
            return wrapped.hasNext();
        }

        @Override
        public K next() {
            return wrapped.next();
        }

        @Override
        public void remove() {
            // TODO Never called?
            System.out.println("KeySetIteratorWrapper.remove");
            wrapped.remove();
        }
    }

    private class ValuesCollectionWrapper implements Collection<V> {

        private final Collection<V> wrapped;

        private ValuesCollectionWrapper(Collection<V> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public int size() {
            return wrapped.size();
        }

        @Override
        public boolean isEmpty() {
            return wrapped.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return wrapped.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return wrapped.containsAll(c);
        }

        @Override
        public Object[] toArray() {
            return wrapped.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return wrapped.toArray(a);
        }

        @Override
        public Iterator<V> iterator() {
            // TODO
            return wrapped.iterator();
        }

        @Override
        public boolean add(V v) {
            // Cannot add value because no key can be specified
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends V> c) {
            // Cannot add values because no key can be specified
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object value) {
            boolean modified = false;
            for (Entry<K, V> entry : MapWrapper.this.entrySet()) {
                if (ValueUtils.areEqual(entry.getValue(), value)) {
                    modified = wrapped.remove(value);
                    System.out.println("REMOVED: k=" + entry.getKey() + " ov=" + value);
                    break;
                }
            }
            return modified;
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            boolean modified = false;
            for (Object value : c) {
                modified |= remove(value);
            }
            return modified;
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            // TODO
            return wrapped.retainAll(c);
        }

        @Override
        public void clear() {
            MapWrapper.this.clear();
        }
    }

    private class EntrySetWrapper implements Set<Entry<K, V>> {

        private final Set<Entry<K, V>> wrapped;

        private EntrySetWrapper(Set<Entry<K, V>> wrapped) {
            this.wrapped = wrapped;
        }

        @Override
        public int size() {
            return wrapped.size();
        }

        @Override
        public boolean isEmpty() {
            return wrapped.isEmpty();
        }

        @Override
        public boolean contains(Object o) {
            return wrapped.contains(o);
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return wrapped.containsAll(c);
        }

        @Override
        public Object[] toArray() {
            return wrapped.toArray();
        }

        @Override
        public <T> T[] toArray(T[] a) {
            return wrapped.toArray(a);
        }

        @Override
        public Iterator<Entry<K, V>> iterator() {
            // TODO
            return wrapped.iterator();
        }

        @Override
        public boolean add(Entry<K, V> entry) {
            // TODO
            return wrapped.add(entry);
        }

        @Override
        public boolean addAll(Collection<? extends Entry<K, V>> c) {
            // TODO
            return wrapped.addAll(c);
        }

        @Override
        public boolean remove(Object o) {
            // TODO
            return wrapped.remove(o);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            // TODO
            return wrapped.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            // TODO
            return wrapped.retainAll(c);
        }

        @Override
        public void clear() {
            // TODO
            wrapped.clear();
        }
    }
}
