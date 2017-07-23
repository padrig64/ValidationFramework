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

package com.google.code.validationframework.base.transform;

import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.simple.SimpleStringProperty;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * @see ConstantTransformer
 */
public class ConstantTransformerTest {

    @Test
    public void testNonNullOutput() {
        Transformer<Object, Integer> transformer = new ConstantTransformer<Object, Integer>(54);

        assertEquals(Integer.valueOf(54), transformer.transform(null));
        assertEquals(Integer.valueOf(54), transformer.transform(new Object()));
        assertEquals(Integer.valueOf(54), transformer.transform(53));
        assertEquals(Integer.valueOf(54), transformer.transform("toto"));
    }

    @Test
    public void testNullOutput() {
        Transformer<Object, String> transformer = new ConstantTransformer<Object,
                String>(new SimpleStringProperty(null));

        assertNull(transformer.transform(null));
        assertNull(transformer.transform(new Object()));
        assertNull(transformer.transform(53));
        assertNull(transformer.transform("toto"));
    }

    @Test
    public void testOutputChange() {
        ConstantTransformer<Object, Double> transformer = new ConstantTransformer<Object, Double>();

        assertNull(transformer.transform(null));
        assertNull(transformer.transform(new Object()));
        assertNull(transformer.transform(53));
        assertNull(transformer.transform("toto"));

        transformer.getOutputProperty().setValue(12.34);

        assertEquals(Double.valueOf(12.34), transformer.transform(null));
        assertEquals(Double.valueOf(12.34), transformer.transform(new Object()));
        assertEquals(Double.valueOf(12.34), transformer.transform(53));
        assertEquals(Double.valueOf(12.34), transformer.transform("toto"));
    }
}
