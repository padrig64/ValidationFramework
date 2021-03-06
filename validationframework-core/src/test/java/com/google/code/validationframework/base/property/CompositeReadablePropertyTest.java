/*
 * Copyright (c) 2017, ValidationFramework Authors
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

import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.simple.SimpleIntegerProperty;
import com.google.code.validationframework.test.ListMatcher;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.google.code.validationframework.test.TestUtils.haveEqualElements;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see CompositeReadableProperty
 */
public class CompositeReadablePropertyTest {

    @Test
    public void testValueAfterChange() {
        SimpleIntegerProperty compoundProperty1 = new SimpleIntegerProperty(1);
        SimpleIntegerProperty compoundProperty2 = new SimpleIntegerProperty(2);
        CompositeReadableProperty<Integer> compositeProperty = new CompositeReadableProperty<Integer>
                (compoundProperty1, compoundProperty2);

        compoundProperty1.setValue(5);
        compoundProperty2.setValue(6);

        // Verify values
        List<Integer> expectedNewValues = new ArrayList<Integer>();
        expectedNewValues.add(5);
        expectedNewValues.add(6);
        assertTrue(haveEqualElements(expectedNewValues, new ArrayList<Integer>(compositeProperty.getValue())));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPropertyChangeEvent() {
        SimpleIntegerProperty compoundProperty1 = new SimpleIntegerProperty(1);
        SimpleIntegerProperty compoundProperty2 = new SimpleIntegerProperty(2);
        CompositeReadableProperty<Integer> compositeProperty = new CompositeReadableProperty<Integer>
                (compoundProperty1, compoundProperty2);

        ValueChangeListener<Collection<Integer>> mockListener = (ValueChangeListener<Collection<Integer>>) mock
                (ValueChangeListener.class);
        compositeProperty.addValueChangeListener(mockListener);
        compoundProperty1.setValue(5);
        compoundProperty2.setValue(6);

        // Verify first change event
        List<Integer> expectedOldValues = new ArrayList<Integer>();
        expectedOldValues.add(1);
        expectedOldValues.add(2);
        List<Integer> expectedNewValues = new ArrayList<Integer>();
        expectedNewValues.add(5);
        expectedNewValues.add(2);
        verify(mockListener).valueChanged(eq(compositeProperty), argThat(new ListMatcher<Integer>(expectedOldValues))
                , argThat(new ListMatcher<Integer>(expectedNewValues)));

        // Verify second change event
        expectedOldValues = expectedNewValues;
        expectedNewValues = new ArrayList<Integer>();
        expectedNewValues.add(5);
        expectedNewValues.add(6);
        verify(mockListener).valueChanged(eq(compositeProperty), argThat(new ListMatcher<Integer>(expectedOldValues))
                , argThat(new ListMatcher<Integer>(expectedNewValues)));
    }
}
