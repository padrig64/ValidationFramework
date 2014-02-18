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

package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.binding.ChangeListener;
import com.google.code.validationframework.base.binding.simple.IntegerProperty;
import org.hamcrest.Description;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see CompositeReadableProperty
 */
public class CompositeReadablePropertyTest {

    private static class CollectionMatcher<T> extends ArgumentMatcher<Collection<T>> {

        private final List<T> refElements;

        public CollectionMatcher(Collection<T> refElements) {
            super();
            this.refElements = new ArrayList<T>(refElements);
        }

        @Override
        public boolean matches(Object actualElements) {
            boolean match = false;

            if ((actualElements instanceof Collection<?>) && ((Collection<?>) actualElements).size() == refElements
                    .size()) {
                match = true;
                List<T> actual = new ArrayList<T>((Collection<T>) actualElements);
                for (int i = 0; (i < refElements.size()) && match; i++) {
                    match = actual.get(i).equals(refElements.get(i));
                }
            }

            return match;
        }

        public void describeTo(Description description) {
            // TODO
        }
    }

    public static Collection<Integer> sameValues(Collection<Integer> values) {

        return argThat(new CollectionMatcher<Integer>(values));
    }

    @Test
    public void testMultipleProperty() {
        Collection<Integer> expectedOldValues = new ArrayList<Integer>();
        expectedOldValues.add(1);
        expectedOldValues.add(2);

        IntegerProperty compoundProperty1 = new IntegerProperty(1);
        IntegerProperty compoundProperty2 = new IntegerProperty(2);
        CompositeReadableProperty<Integer> compositeProperty = new CompositeReadableProperty<Integer>
                (compoundProperty1, compoundProperty2);

        ChangeListener<Collection<Integer>> mockListener = (ChangeListener<Collection<Integer>>) mock(ChangeListener
                .class);
        compositeProperty.addChangeListener(mockListener);
        compoundProperty1.setValue(5);
        compoundProperty2.setValue(6);

        Collection<Integer> expectedNewValues = new ArrayList<Integer>();
        expectedNewValues.add(5);
        expectedNewValues.add(2);
        verify(mockListener).propertyChanged(eq(compositeProperty), sameValues(expectedOldValues),
                sameValues(expectedNewValues));

        expectedNewValues.clear();
        expectedNewValues.add(5);
        expectedNewValues.add(6);
        verify(mockListener).propertyChanged(eq(compositeProperty), sameValues(expectedOldValues),
                sameValues(expectedNewValues));
    }
}
