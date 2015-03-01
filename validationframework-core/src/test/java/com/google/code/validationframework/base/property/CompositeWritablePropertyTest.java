/*
 * Copyright (c) 2015, Patrick Moawad
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

import com.google.code.validationframework.base.property.simple.SimpleIntegerProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @see CompositeWritableProperty
 */
public class CompositeWritablePropertyTest {

    @Test
    public void testInitialValue() {
        // Create composite property and set value before adding the sub-properties
        CompositeWritableProperty<Integer> compositeProperty = new CompositeWritableProperty<Integer>(10);

        // Create sub-properties with different values
        SimpleIntegerProperty compoundProperty1 = new SimpleIntegerProperty(1);
        SimpleIntegerProperty compoundProperty2 = new SimpleIntegerProperty(2);
        SimpleIntegerProperty compoundProperty3 = new SimpleIntegerProperty(3);

        // Add sub-properties to composition
        compositeProperty.addProperty(compoundProperty1);
        compositeProperty.addProperty(compoundProperty2);
        compositeProperty.addProperty(compoundProperty3);

        // Check that the values of the sub-properties is now the value set on the composition
        assertEquals(Integer.valueOf(10), compoundProperty1.getValue());
        assertEquals(Integer.valueOf(10), compoundProperty2.getValue());
        assertEquals(Integer.valueOf(10), compoundProperty3.getValue());
    }

    @Test
    public void testAddAndValueChange() {
        // Create composite property and set value before adding the sub-properties
        CompositeWritableProperty<Integer> compositeProperty = new CompositeWritableProperty<Integer>();
        compositeProperty.setValue(5);

        // Create sub-properties with different values
        SimpleIntegerProperty compoundProperty1 = new SimpleIntegerProperty(1);
        SimpleIntegerProperty compoundProperty2 = new SimpleIntegerProperty(2);
        SimpleIntegerProperty compoundProperty3 = new SimpleIntegerProperty(3);

        // Add sub-properties to composition
        compositeProperty.addProperty(compoundProperty1);
        compositeProperty.addProperty(compoundProperty2);
        compositeProperty.addProperty(compoundProperty3);

        // Check that the values of the sub-properties is now the value set on the composition
        assertEquals(Integer.valueOf(5), compoundProperty1.getValue());
        assertEquals(Integer.valueOf(5), compoundProperty2.getValue());
        assertEquals(Integer.valueOf(5), compoundProperty3.getValue());

        // Change the value of the composition
        compositeProperty.setValue(33);
        assertEquals(Integer.valueOf(33), compoundProperty1.getValue());
        assertEquals(Integer.valueOf(33), compoundProperty2.getValue());
        assertEquals(Integer.valueOf(33), compoundProperty3.getValue());
    }
}
