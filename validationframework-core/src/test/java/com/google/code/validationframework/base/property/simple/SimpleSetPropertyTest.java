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

import com.google.code.validationframework.api.property.SetValueChangeListener;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static com.google.code.validationframework.test.TestUtils.haveEqualElements;
import static com.google.code.validationframework.test.TestUtils.matches;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SimpleSetPropertyTest {

    @Test
    public void testDefaultConstructor() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertTrue(haveEqualElements(property, Collections.<Integer>emptySet()));
        assertTrue(haveEqualElements(property.asUnmodifiableSet(), Collections.<Integer>emptySet()));

        verifyZeroInteractions(listener);
    }

    @Test
    public void testConstructorWithItems() {
        Set<Integer> ref = new HashSet<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(ref);
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertTrue(haveEqualElements(ref, property));
        assertTrue(haveEqualElements(ref, property.asUnmodifiableSet()));

        verifyZeroInteractions(listener);
    }

    @Test
    public void testConstructorWithListeners() {
        SetValueChangeListener<Integer> listener1 = mock(SetValueChangeListener.class);
        SetValueChangeListener<Integer> listener2 = mock(SetValueChangeListener.class);
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(listener1, listener2);

        property.add(4);

        verify(listener1).valuesAdded(eq(property), matches(Collections.singleton(4)));
        verify(listener2).valuesAdded(eq(property), matches(Collections.singleton(4)));
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testConstructorWithItemsAndListeners() {
        Set<Integer> ref = new HashSet<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SetValueChangeListener<Integer> listener1 = mock(SetValueChangeListener.class);
        SetValueChangeListener<Integer> listener2 = mock(SetValueChangeListener.class);
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(ref, listener1, listener2);

        assertTrue(haveEqualElements(ref, property));
        assertTrue(haveEqualElements(ref, property.asUnmodifiableSet()));

        property.add(4);

        verify(listener1).valuesAdded(eq(property), matches(Collections.singleton(4)));
        verify(listener2).valuesAdded(eq(property), matches(Collections.singleton(4)));
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void testSize() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
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
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
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
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.add(1);
        property.add(3);
        property.add(2);
        property.add(3);
        property.add(2);

        Set<Integer> refFirst = Collections.singleton(1);
        Set<Integer> refSecond = Collections.singleton(3);
        Set<Integer> refThird = Collections.singleton(2);
        Set<Integer> refAll = new HashSet<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        assertTrue(haveEqualElements(refAll, property));
        verify(listener).valuesAdded(eq(property), matches(refFirst));
        verify(listener).valuesAdded(eq(property), matches(refSecond));
        verify(listener).valuesAdded(eq(property), matches(refThird));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testAddAll() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        Set<Integer> ref = new HashSet<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        property.addAll(ref);

        assertEquals(ref.size(), property.size());
        assertTrue(haveEqualElements(ref, property));
        verify(listener).valuesAdded(eq(property), matches(ref));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemove() {
        Set<Integer> refAll = new HashSet<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(refAll);
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.remove(2);
        property.remove(1);
        property.remove(3);

        assertTrue(property.isEmpty());
        verify(listener).valuesRemoved(eq(property), matches(Collections.singleton(2)));
        verify(listener).valuesRemoved(eq(property), matches(Collections.singleton(1)));
        verify(listener).valuesRemoved(eq(property), matches(Collections.singleton(3)));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRemoveAll() {
        Set<Integer> refAll = new HashSet<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(refAll);
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.removeAll(refAll);

        assertTrue(property.isEmpty());
        verify(listener).valuesRemoved(eq(property), matches(refAll));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testRetainAll() {
        Set<Integer> initial = new HashSet<Integer>();
        initial.add(1);
        initial.add(2);
        initial.add(3);
        initial.add(4);
        initial.add(5);
        initial.add(6);
        Set<Integer> retained = new HashSet<Integer>();
        retained.add(1);
        retained.add(2);
        retained.add(3);
        retained.add(11);
        retained.add(12);
        retained.add(13);
        Set<Integer> removed = new HashSet<Integer>();
        removed.add(4);
        removed.add(5);
        removed.add(6);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(initial);
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.retainAll(retained);

        assertEquals(3, property.size());
        verify(listener).valuesRemoved(eq(property), matches(removed));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testClear() {
        Set<Integer> refAll = new HashSet<Integer>();
        refAll.add(1);
        refAll.add(2);
        refAll.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>(refAll);
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        property.clear();

        assertTrue(property.isEmpty());
        verify(listener).valuesRemoved(eq(property), matches(refAll));
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void testContains() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();

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
        Set<Integer> refSmall = new HashSet<Integer>();
        refSmall.add(1);
        refSmall.add(2);
        refSmall.add(3);

        Set<Integer> refBig = new HashSet<Integer>();
        refBig.add(1);
        refBig.add(2);
        refBig.add(3);
        refBig.add(4);
        refBig.add(5);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
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
    public void testAsUnmodifiableSetAndIterator() {
        Set<Integer> ref = new HashSet<Integer>();
        ref.add(1);
        ref.add(2);
        ref.add(3);

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
        Set<Integer> unmodifiable = property.asUnmodifiableSet();

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
            unmodifiable.add(4);
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
    }

    @Test
    public void testToArray() {
        Integer[] ref = new Integer[]{1, 2, 3};

        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
        property.add(1);
        property.add(2);
        property.add(3);

        assertArrayEquals(ref, property.toArray());
        assertArrayEquals(ref, property.toArray(new Integer[3]));
    }
}
