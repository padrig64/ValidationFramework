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

import org.junit.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see MapProxy
 */
public class MapProxyTest {

    @Test
    public void testAddition() {
        // Initialization
        MapProxy<String, Integer> map = new MapProxy<String, Integer>();
        MapProxyListener<String, Integer> listener = mock(MapProxyListener.class);
        map.addMapProxyListener(listener);

        // Add entries one by one
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);

        assertEquals(3, map.size());
        verify(listener, times(3)).entriesAdded(any(MapProxy.class), anyMap());
        verify(listener, times(0)).entriesChanged(any(MapProxy.class), anyMap());
        verify(listener, times(0)).entriesRemoved(any(MapProxy.class), anyMap());
    }

    @Test
    public void testUsingMapProxyDirectly() {
        System.out.println();
        System.out.println("MapProxyTest.testUsingMapProxyDirectly");

        MapProxy<String, Integer> map = new MapProxy<String, Integer>();
        map.addMapProxyListener(new MapProxyListener<String, Integer>() {
            @Override
            public void entriesAdded(MapProxy<String, Integer> mapProxy, Map<String, Integer> added) {
                System.out.println("ADDED: +" + added.size());
            }

            @Override
            public void entriesChanged(MapProxy<String, Integer> mapProxy, Map<String, Integer> changed) {
                System.out.println("CHANGED: ~" + changed.size());
            }

            @Override
            public void entriesRemoved(MapProxy<String, Integer> mapProxy, Map<String, Integer> removed) {
                System.out.println("REMOVED: -" + removed.size());
            }
        });

        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        // Add one by one
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        // Add several at once
        Map<String, Integer> added = new HashMap<String, Integer>();
        added.put("Four", 4);
        added.put("Five", 5);
        added.put("Six", 6);
        map.putAll(added);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        // Replace one by one
        map.put("One", 11);
        map.put("Three", 33);
        map.put("Five", 55);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        // Replace several at once
        Map<String, Integer> replaced = new HashMap<String, Integer>();
        replaced.put("Two", 22);
        replaced.put("Four", 44);
        replaced.put("Six", 66);
        map.putAll(replaced);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        // Remove one by one
        map.remove("Four");
        map.remove("Five");
        map.remove("Six");
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size());

        // Remove several at once
        map.clear();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

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

        MapProxy<String, Integer> map = new MapProxy<String, Integer>();
        Set<Map.Entry<String, Integer>> entries = map.entrySet();
        Set<String> keys = map.keySet();
        Collection<Integer> values = map.values();
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        // TODO Add one by one
        // TODO Add several at once
        map.put("One", 1);
        map.put("Two", 2);
        map.put("Three", 3);
        map.put("Four", 4);
        map.put("Five", 5);
        map.put("Six", 6);
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size() + " re=" + map.getReadOnlyMap().entrySet().size());

        map.addMapProxyListener(new MapProxyListener<String, Integer>() {
            @Override
            public void entriesAdded(MapProxy<String, Integer> mapProxy, Map<String, Integer> added) {
                System.out.println("ADDED: +" + added.size());
            }

            @Override
            public void entriesChanged(MapProxy<String, Integer> mapProxy, Map<String, Integer> changed) {
                System.out.println("CHANGED: ~" + changed.size());
            }

            @Override
            public void entriesRemoved(MapProxy<String, Integer> mapProxy, Map<String, Integer> removed) {
                System.out.println("REMOVED: -" + removed.size());
            }
        });

        // Replace one by one
        for (Map.Entry<String, Integer> entry : map.getReadOnlyMap().entrySet()) {
            entry.setValue(entry.getValue() * 11);
        }
        System.out.println(map.get("One"));
        System.out.println(map.get("Two"));
        System.out.println(map.get("Three"));
        System.out.println("==> SIZE: m=" + map.size() + " e=" + entries.size() + " k=" + keys.size() + " v=" +
                values.size()+ " re=" + map.getReadOnlyMap().entrySet().size());
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
