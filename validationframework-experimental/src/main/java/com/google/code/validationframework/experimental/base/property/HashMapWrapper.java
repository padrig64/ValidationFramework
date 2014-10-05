package com.google.code.validationframework.experimental.base.property;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class HashMapWrapper<K, V> implements Map<K, V> {

    private final Map<K, V> internal = new HashMap<K, V>();

    @Override
    public int size() {
        return internal.size();
    }

    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return internal.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return internal.get(key);
    }

    @Override
    public V put(K key, V value) {
        boolean updated = internal.containsKey(key);

        V oldValue = internal.put(key, value);

        if (updated) {
            System.out.println("UPDATED: k=" + key + " ov=" + oldValue + " nv=" + value);
        } else {
            System.out.println("ADDED: k=" + key + " nv=" + value);
        }

        return oldValue;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> map) {
//        internal.putAll(map);
        for (Entry<? extends K, ? extends V> entry : map.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public V remove(Object key) {
        boolean removed = internal.containsKey(key);
        V oldValue = internal.remove(key);

        if (removed) {
            System.out.println("REMOVED: k=" + key + " ov=" + oldValue);
        }

        return oldValue;
    }

    @Override
    public void clear() {
        if (!internal.isEmpty()) {
            Map<K, V> oldEntries = new HashMap<K, V>(internal);
            internal.clear();
            System.out.println("CLEARED");
        }
    }

    @Override
    public Set<K> keySet() {
        return new KeySetWrapper(internal.keySet());
    }

    @Override
    public Collection<V> values() {
        return new ValuesCollectionWrapper(internal.values());
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return new EntrySetWrapper(internal.entrySet());
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
            // TODO
            return wrapped.iterator();
        }

        @Override
        public boolean add(K key) {
            // Cannot add because no value can be specified
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object key) {
            boolean removed = HashMapWrapper.this.containsKey(key);
            HashMapWrapper.this.remove(key);
            return removed;
        }

        @Override
        public boolean addAll(Collection<? extends K> c) {
            // Cannot add because no value can be specified
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            // TODO
            return wrapped.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            // TODO
            return wrapped.removeAll(c);
        }

        @Override
        public void clear() {
            // TODO
            wrapped.clear();
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
            // Will call remove() on the map
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
            // TODO
            return wrapped.add(v);
        }

        @Override
        public boolean remove(Object o) {
            // TODO
            return wrapped.remove(o);
        }

        @Override
        public boolean addAll(Collection<? extends V> c) {
            // TODO
            return wrapped.addAll(c);
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
        public boolean remove(Object o) {
            // TODO
            return wrapped.remove(o);
        }

        @Override
        public boolean addAll(Collection<? extends Entry<K, V>> c) {
            // TODO
            return wrapped.addAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            // TODO
            return wrapped.retainAll(c);
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            // TODO
            return wrapped.removeAll(c);
        }

        @Override
        public void clear() {
            // TODO
            wrapped.clear();
        }
    }
}
