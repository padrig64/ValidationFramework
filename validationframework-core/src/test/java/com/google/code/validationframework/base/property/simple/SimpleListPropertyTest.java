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

import com.google.code.validationframework.api.property.ListValueChangeListener;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static com.google.code.validationframework.test.TestUtils.haveEqualElements;
import static com.google.code.validationframework.test.TestUtils.matches;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SimpleListPropertyTest {

    @Test
    public void testDefaultConstructor() {
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertTrue(haveEqualElements(property, Collections.<Integer>emptyList()));
        assertTrue(haveEqualElements(property.asUnmodifiableList(), Collections.<Integer>emptyList()));

        verifyZeroInteractions(listener);
    }

    @Test
    public void testConstructorWithItems() {
        List<Integer> ref = new ArrayList<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(ref);
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertTrue(haveEqualElements(ref, property));
        assertTrue(haveEqualElements(ref, property.asUnmodifiableList()));

        verifyZeroInteractions(listener);
    }

    @Test
    public void testConstructorWithListeners() {
        ListValueChangeListener<Integer> listener1 = mock(ListValueChangeListener.class);
        ListValueChangeListener<Integer> listener2 = mock(ListValueChangeListener.class);
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(listener1, listener2);

        property.add(4);

        verify(listener1).valuesAdded(eq(property), eq(0), matches(Collections.singletonList(4)));
        verify(listener2).valuesAdded(eq(property), eq(0), matches(Collections.singletonList(4)));
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testConstructorWithItemsAndListeners() {
        List<Integer> ref = new ArrayList<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        ListValueChangeListener<Integer> listener1 = mock(ListValueChangeListener.class);
        ListValueChangeListener<Integer> listener2 = mock(ListValueChangeListener.class);
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(ref, listener1, listener2);

        assertTrue(haveEqualElements(ref, property));
        assertTrue(haveEqualElements(ref, property.asUnmodifiableList()));

        property.add(4);

        verify(listener1).valuesAdded(eq(property), eq(3), matches(Collections.singletonList(4)));
        verify(listener2).valuesAdded(eq(property), eq(3), matches(Collections.singletonList(4)));
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testSize() {
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        assertEquals(0, property.size());

        property.add(1);
        assertEquals(1, property.size());
        property.add(2);
        assertEquals(2, property.size());
        property.add(3);
        assertEquals(3, property.size());

        property.clear();
        assertEquals(0, property.size());
    }

    @Test
    public void testIsEmpty() {
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        assertTrue(property.isEmpty());

        property.add(1);
        assertFalse(property.isEmpty());
        property.add(2);
        assertFalse(property.isEmpty());
        property.add(3);
        assertFalse(property.isEmpty());

        property.clear();
        assertTrue(property.isEmpty());
    }

    @Test
    public void testAdd() {
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.add(1);
        property.add(3);
        property.add(2);
        property.add(3);
        property.add(2);

        List<Integer> refAll = new ArrayList<Integer>();
        refAll.add(1);
        refAll.add(3);
        refAll.add(2);
        refAll.add(3);
        refAll.add(2);

        assertTrue(haveEqualElements(refAll, property));
        verify(listener).valuesAdded(eq(property), eq(0), matches(Collections.singletonList(1)));
        verify(listener).valuesAdded(eq(property), eq(1), matches(Collections.singletonList(3)));
        verify(listener).valuesAdded(eq(property), eq(2), matches(Collections.singletonList(2)));
        verify(listener).valuesAdded(eq(property), eq(3), matches(Collections.singletonList(3)));
        verify(listener).valuesAdded(eq(property), eq(4), matches(Collections.singletonList(2)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testAddAll() {
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        List<Integer> ref = new ArrayList<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);
        List<Integer> refAll = new ArrayList<Integer>();
        refAll.addAll(ref);
        refAll.addAll(ref);

        property.addAll(ref);
        property.addAll(ref);

        assertEquals(refAll.size(), property.size());
        assertTrue(haveEqualElements(refAll, property));
        verify(listener).valuesAdded(eq(property), eq(0), matches(ref));
        verify(listener).valuesAdded(eq(property), eq(3), matches(ref));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemove() {
        List<Integer> refAll = new ArrayList<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(refAll);
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.remove(Integer.valueOf(2));
        property.remove(Integer.valueOf(1));
        property.remove(Integer.valueOf(3));

        assertTrue(property.isEmpty());
        verify(listener).valuesRemoved(eq(property), eq(1), matches(Collections.singletonList(2)));
        verify(listener).valuesRemoved(eq(property), eq(0), matches(Collections.singletonList(1)));
        verify(listener).valuesRemoved(eq(property), eq(0), matches(Collections.singletonList(3)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemoveAt() {
        List<String> refAll = new ArrayList<String>();
        refAll.add("A");
        refAll.add("B");
        refAll.add("C");

        SimpleListProperty<String> property = new SimpleListProperty<String>(refAll);
        ListValueChangeListener<String> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.remove(1);
        property.remove(0);
        property.remove(0);

        assertTrue(property.isEmpty());
        verify(listener).valuesRemoved(eq(property), eq(1), matches(Collections.singletonList("B")));
        verify(listener).valuesRemoved(eq(property), eq(0), matches(Collections.singletonList("A")));
        verify(listener).valuesRemoved(eq(property), eq(0), matches(Collections.singletonList("C")));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemoveAll() {
        List<Integer> refAll = new ArrayList<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(refAll);
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        refAll.clear();
        refAll.add(2);
        refAll.add(3);
        refAll.add(1);
        property.removeAll(refAll);

        assertTrue(property.isEmpty());
        verify(listener, times(0)).valuesAdded(eq(property), anyInt(), Matchers.<List<Integer>>any());

        // Note that the number and ordering of calls depends on the implementation
        verify(listener).valuesRemoved(eq(property), eq(1), matches(Collections.singletonList(2)));
        verify(listener).valuesRemoved(eq(property), eq(1), matches(Collections.singletonList(3)));
        verify(listener).valuesRemoved(eq(property), eq(0), matches(Collections.singletonList(1)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRetainAll() {
        List<Integer> initial = new ArrayList<Integer>();
        initial.add(1);
        initial.add(1);
        initial.add(2);
        initial.add(3);
        initial.add(4);
        initial.add(5);
        initial.add(6);
        initial.add(7);
        initial.add(6);
        List<Integer> toBeRetained = new ArrayList<Integer>();
        toBeRetained.add(1);
        toBeRetained.add(2);
        toBeRetained.add(3);
        toBeRetained.add(11);
        toBeRetained.add(12);
        toBeRetained.add(13);
        toBeRetained.add(7);
        List<Integer> remaining = new ArrayList<Integer>();
        remaining.add(1);
        remaining.add(1);
        remaining.add(2);
        remaining.add(3);
        remaining.add(7);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(initial);
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.retainAll(toBeRetained);

        assertEquals(5, property.size());
        assertTrue(haveEqualElements(remaining, property));

        // Note that the number and ordering of calls depends on the implementation
        verify(listener).valuesRemoved(eq(property), eq(4), matches(Collections.singletonList(4)));
        verify(listener).valuesRemoved(eq(property), eq(4), matches(Collections.singletonList(5)));
        verify(listener).valuesRemoved(eq(property), eq(4), matches(Collections.singletonList(6)));
        verify(listener).valuesRemoved(eq(property), eq(5), matches(Collections.singletonList(6)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testClear() {
        List<Integer> refAll = new ArrayList<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>(refAll);
        ListValueChangeListener<Integer> listener = mock(ListValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.clear();

        assertTrue(property.isEmpty());
        verify(listener).valuesRemoved(eq(property), eq(0), matches(refAll));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testContains() {
        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();

        assertFalse(property.contains(1));
        assertFalse(property.contains(2));
        assertFalse(property.contains(3));
        assertFalse(property.contains(4));
        assertFalse(property.contains(5));

        property.add(1);
        property.add(2);
        property.add(3);

        assertTrue(property.contains(1));
        assertTrue(property.contains(2));
        assertTrue(property.contains(3));
        assertFalse(property.contains(4));
        assertFalse(property.contains(5));

        property.clear();

        assertFalse(property.contains(1));
        assertFalse(property.contains(2));
        assertFalse(property.contains(3));
        assertFalse(property.contains(4));
        assertFalse(property.contains(5));
    }

    @Test
    public void testContainsAll() {
        List<Integer> refSmall = new ArrayList<Integer>();
        refSmall.add(1);
        refSmall.add(2);
        refSmall.add(3);

        List<Integer> refBig = new ArrayList<Integer>();
        refBig.add(1);
        refBig.add(2);
        refBig.add(3);
        refBig.add(4);
        refBig.add(5);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(1);
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(2);
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(3);
        assertTrue(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(4);
        assertTrue(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));

        property.add(5);
        assertTrue(property.containsAll(refSmall));
        assertTrue(property.containsAll(refBig));

        property.clear();
        assertFalse(property.containsAll(refSmall));
        assertFalse(property.containsAll(refBig));
    }

    @Test
    public void testAsUnmodifiableListAndIterator() {
        List<Integer> ref = new ArrayList<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        List<Integer> unmodifiable = property.asUnmodifiableList();

        assertTrue(haveEqualElements(property, unmodifiable));
        assertTrue(unmodifiable.isEmpty());

        property.addAll(ref);
        assertTrue(haveEqualElements(property, unmodifiable));
        assertTrue(haveEqualElements(ref, unmodifiable));

        try {
            Iterator<Integer> iterator = property.iterator();
            assertTrue(iterator.hasNext());
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            Iterator<Integer> iterator = property.listIterator();
            assertTrue(iterator.hasNext());
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            Iterator<Integer> iterator = property.listIterator(1);
            assertTrue(iterator.hasNext());
            iterator.next();
            iterator.remove();
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.add(4);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.add(0, 4);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.set(0, 4);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.addAll(0, ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        ref.add(5);
        try {
            unmodifiable.addAll(ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.retainAll(ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.remove(Integer.valueOf(1));
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.remove(1);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.removeAll(ref);
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.clear();
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
        try {
            unmodifiable.subList(0, 2).clear();
            assertFalse(true);
        } catch (UnsupportedOperationException e) {
            // As expected
        }
    }

    @Test
    public void testToArray() {
        Integer[] ref = new Integer[]{1, 2, 3};

        SimpleListProperty<Integer> property = new SimpleListProperty<Integer>();
        property.add(1);
        property.add(2);
        property.add(3);

        assertArrayEquals(ref, property.toArray());
        assertArrayEquals(ref, property.toArray(new Integer[3]));
    }
}
