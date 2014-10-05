package com.google.code.validationframework.experimental.base.property;

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @see HashMapWrapper
 */
public class HashMapWrapperTest {

    @Test
    public void testUsingMapDirectly() {
        Map<String, Integer> map = new HashMapWrapper<String, Integer>();
        System.out.println("SIZE: " + map.size() + " ===================");

        // Add one by one
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Add several at once
        Map<String, Integer> added = new HashMap<String, Integer>();
        added.put("Four", 4);
        added.put("Five", 5);
        added.put("Six", 6);
        map.putAll(added);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Replace one by one
        map.put("One", 11);
        map.put("Three", 33);
        map.put("Five", 55);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Replace several at once
        Map<String, Integer> replaced = new HashMap<String, Integer>();
        replaced.put("Two", 22);
        replaced.put("Four", 44);
        replaced.put("Six", 66);
        map.putAll(replaced);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Remove one by one
        map.remove("Four");
        map.remove("Five");
        map.remove("Six");
        System.out.println("SIZE: " + map.size() + " ===================");

        // Remove several at once
        map.clear();
        System.out.println("SIZE: " + map.size() + " ===================");

        // TODO Entry set (*)
        // TODO Entry set iterator (*)
//        map.entrySet().iterator().next().setValue(0);

        // TODO Key set iterator (*)
        // TODO Value collection (*)
        // TODO Value collection iterator (*)
    }

    @Test
    public void testUsingKeySet() {
        Map<String, Integer> map = new HashMapWrapper<String, Integer>();
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put("Four", 4);
        map.put("Five", 5);
        map.put("Six", 6);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Remove one by one
        Set<String> keys = map.keySet();
        keys.remove("One");
        keys.remove("Three");
        System.out.println("SIZE: " + map.size() + " ===================");

        // Remove several at once
        Collection<String> toBeRemoved = new HashSet<String>();
        toBeRemoved.add("Four");
        toBeRemoved.add("Five");
        keys.removeAll(toBeRemoved);
        System.out.println("SIZE: " + map.size() + " ===================");

        // Clear
        keys.clear();
        System.out.println("SIZE: " + map.size() + " ===================");
    }
}
