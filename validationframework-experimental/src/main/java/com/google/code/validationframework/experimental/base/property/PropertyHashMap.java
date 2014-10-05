package com.google.code.validationframework.experimental.base.property;

import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class PropertyHashMap<K, V> extends AbstractMap<K, V> {

    private class PropertyEntry extends SimpleEntry<K, V> {

        private static final long serialVersionUID = 8499399127046899523L;

        public PropertyEntry(K key, V value) {
            super(key, value);
        }

        public PropertyEntry(Entry<? extends K, ? extends V> entry) {
            super(entry);
        }

        @Override
        public V setValue(V value) {
            V oldValue = super.setValue(value);
            if (!ValueUtils.areEqual(oldValue, value)) {
                System.out.println("FIRE ENTRY CHANGED: k=" + getKey() + " ov=" + oldValue + " nv=" + value);
            }
            return oldValue;
        }

        @Override
        public int hashCode() {
            int i = super.hashCode();
            System.out.println("PropertyEntry.hashCode: k=" + getKey() + " v="+getValue() + " h=" +i);
            return i;
        }

        @Override
        public boolean equals(Object o) {
            boolean equals = super.equals(o);
            System.out.println("PropertyEntry.equals: k=" + getKey() + " v="+getValue());System.out.println("PropertyEntry.hashCode: k=" + getKey() + " v="+getValue());
            System.out.println("                      o=" + o);
            return equals;
        }
    }

    private class PropertyEntrySet extends HashSet<Entry<K, V>> {

        private static final long serialVersionUID = -7970911675588579583L;

        public PropertyEntrySet() {
            super();
        }

        @Override
        public boolean remove(Object o) {
            boolean ret = super.remove(o);

            if (ret && (o instanceof Entry<?, ?>)) {
                System.out.println("FIRE ENTRY REMOVED: k=" + ((Entry) o).getKey() + " ov=" + ((Entry) o).getValue());
            }

            return ret;
        }
    }

    private final Set<Entry<K, V>> entries = new HashSet<Entry<K, V>>();

    @Override
    public Set<Entry<K, V>> entrySet() {
        return entries;
    }

    @Override
    public V put(K key, V value) {
        V oldValue = null;

        boolean found = false;
        for (Entry<K, V> entry : entries) {
            if (ValueUtils.areEqual(entry.getKey(), key)) {
                oldValue = entry.setValue(value);
                found = true;
                break;
            }
        }

        if (!found) {
            entries.add(new PropertyEntry(key, value));
            System.out.println("FIRE ENTRY ADDED: k=" + key + " nv=" + value);
        }

        return oldValue;
    }

    @Override
    public V remove(Object key) {
        V oldValue = null;

        Entry<K, V> foundEntry = null;
        for (Entry<K, V> entry : entries) {
            if (ValueUtils.areEqual(entry.getKey(), key)) {
                foundEntry = entry;
                break;
            }
        }

        if (foundEntry != null) {
            oldValue = foundEntry.getValue();
            boolean removed = entries.remove(foundEntry);
            if(removed) {
                System.out.println("FIRE ENTRY REMOVED (from map): k=" + key + " ov=" + oldValue);
            }
        }

        return oldValue;
    }

    public static void main(String[] args) {
        Map<String, Integer> map = new PropertyHashMap<String, Integer>();
        System.out.println("SIZE: " + map.size() + " ===================");

        // Add one by one using map
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Add several at once using map
        Map<String, Integer> added = new HashMap<String, Integer>();
        added.put("Four", 4);
        added.put("Five", 5);
        added.put("Six", 6);
        map.putAll(added);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Replace one by one using map
        map.put("One", 11);
        map.put("Three", 33);
        map.put("Five", 55);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Replace several at once using map
        Map<String, Integer> replaced = new HashMap<String, Integer>();
        replaced.put("Two", 22);
        replaced.put("Four", 44);
        replaced.put("Six", 66);
        map.putAll(replaced);
        System.out.println("SIZE: " + map.size() + " ===================");

        // TODO Remove one by one using map
        map.remove("Four");
        map.remove("Five");
        map.remove("Six");
        System.out.println("SIZE: " + map.size() + " ===================");

        // TODO Remove several at once using map

        // TODO Entry set (*)
        // TODO Entry set iterator (*)
//        map.entrySet().iterator().next().setValue(0);
        // TODO Key set (*)
        // TODO Key set iterator (*)
        // TODO Value collection (*)
        // TODO Value collection iterator (*)
    }
}
