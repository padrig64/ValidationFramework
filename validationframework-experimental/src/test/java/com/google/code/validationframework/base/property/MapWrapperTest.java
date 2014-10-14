package com.google.code.validationframework.base.property;

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @see MapWrapper
 */
public class MapWrapperTest {

    @Test
    public void testUsingMapDirectly() {
        System.out.println();
        System.out.println("MapWrapperTest.testUsingMapProxyDirectly");

        Map<String, Integer> map = new MapWrapper<String, Integer>();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Add one by one
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Add several at once
        Map<String, Integer> added = new HashMap<String, Integer>();
        added.put("Four", 4);
        added.put("Five", 5);
        added.put("Six", 6);
        map.putAll(added);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Replace one by one
        map.put("One", 11);
        map.put("Three", 33);
        map.put("Five", 55);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Replace several at once
        Map<String, Integer> replaced = new HashMap<String, Integer>();
        replaced.put("Two", 22);
        replaced.put("Four", 44);
        replaced.put("Six", 66);
        map.putAll(replaced);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove one by one
        map.remove("Four");
        map.remove("Five");
        map.remove("Six");
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove several at once
        map.clear();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // TODO Entry set (*)
        // TODO Entry set iterator (*)
//        map.entrySet().iterator().next().setValue(0);
        // TODO Key set iterator (*)
        // TODO Value collection iterator (*)
    }

    @Test
    public void testUsingEntrySet() {
        System.out.println();
        System.out.println("MapWrapperTest.testUsingEntrySet");

        Map<String, Integer> map = new MapWrapper<String, Integer>();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // TODO Add one by one
        // TODO Add several at once
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put("Four", 4);
        map.put("Five", 5);
        map.put("Six", 6);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Replace one by one
        for (Map.Entry<String, Integer> entry : entries) {
            entry.setValue(entry.getValue() * 11);
        }
        System.out.println(map.get("One"));
        System.out.println(map.get("Two"));
        System.out.println(map.get("Three"));
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());
    }

    @Test
    public void testUsingKeySet() {
        System.out.println();
        System.out.println("MapWrapperTest.testUsingKeySet");

        Map<String, Integer> map = new MapWrapper<String, Integer>();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put("Four", 4);
        map.put("Five", 5);
        map.put("Six", 6);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove one by one
        keys.remove("One");
        keys.remove("Three");
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove several at once
        Collection<String> toBeRemoved = new HashSet<String>();
        toBeRemoved.add("Four");
        toBeRemoved.add("Five");
        keys.removeAll(toBeRemoved);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Clear
        keys.clear();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());
    }

    @Test
    public void testUsingValueCollection() {
        System.out.println();
        System.out.println("MapWrapperTest.testUsingValueCollection");

        Map<String, Integer> map = new MapWrapper<String, Integer>();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put("Four", 4);
        map.put("Five", 5);
        map.put("Six", 6);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove one by one
        values.remove(1);
        values.remove(3);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove several at once
        Collection<Integer> toBeRemoved = new HashSet<Integer>();
        toBeRemoved.add(4);
        toBeRemoved.add(5);
        values.removeAll(toBeRemoved);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Clear
        values.clear();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());
    }
}
