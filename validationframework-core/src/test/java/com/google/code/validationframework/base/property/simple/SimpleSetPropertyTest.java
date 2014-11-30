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

import com.google.code.validationframework.api.property.ReadableSetProperty;
import com.google.code.validationframework.api.property.SetValueChangeListener;
import org.junit.Test;
import org.mockito.Matchers;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.google.code.validationframework.test.TestUtils.haveEqualElements;
import static com.google.code.validationframework.test.TestUtils.matchesSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SimpleSetPropertyTest {

    @Test
    public void testInitial() {
        SimpleSetProperty<Integer> property = new SimpleSetProperty<Integer>();
        SetValueChangeListener<Integer> listener = mock(SetValueChangeListener.class);
        property.addValueChangeListener(listener);

        assertTrue(haveEqualElements(property, Collections.<Integer>emptySet()));
        assertTrue(haveEqualElements(property.asUnmodifiableSet(), Collections.<Integer>emptySet()));

        verify(listener, times(0)).valuesAdded(eq(property), Matchers.<Set<Integer>>any());
        verify(listener, times(0)).valuesRemoved(eq(property), Matchers.<Set<Integer>>any());
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

        property.addValueChangeListener(new SetValueChangeListener<Integer>() {
            @Override
            public void valuesAdded(ReadableSetProperty<Integer> setProperty, Set<Integer> newValues) {
                System.out.println("SimpleSetPropertyTest.valuesAdded");
                for (Integer value : newValues) {
                    System.out.println(" |_ " + value);
                }
            }

            @Override
            public void valuesRemoved(ReadableSetProperty<Integer> setProperty, Set<Integer> oldValues) {
                System.out.println("SimpleSetPropertyTest.valuesRemoved");
                for (Integer value : oldValues) {
                    System.out.println(" |_ " + value);
                }
            }
        });

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
        verify(listener).valuesAdded(eq(property), matchesSet(refFirst));
        verify(listener).valuesAdded(eq(property), matchesSet(refSecond));
        verify(listener).valuesAdded(eq(property), matchesSet(refThird));
        verify(listener, times(0)).valuesRemoved(eq(property), Matchers.<Set<Integer>>any());
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
        verify(listener).valuesAdded(eq(property), matchesSet(ref));
        verify(listener, times(0)).valuesRemoved(eq(property), Matchers.<Set<Integer>>any());
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
        verify(listener, times(0)).valuesAdded(eq(property), Matchers.<Set<Integer>>any());
        verify(listener).valuesRemoved(eq(property), matchesSet(Collections.singleton(2)));
        verify(listener).valuesRemoved(eq(property), matchesSet(Collections.singleton(1)));
        verify(listener).valuesRemoved(eq(property), matchesSet(Collections.singleton(3)));
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
        verify(listener, times(0)).valuesAdded(eq(property), Matchers.<Set<Integer>>any());
        verify(listener).valuesRemoved(eq(property), matchesSet(refAll));
    }
}
