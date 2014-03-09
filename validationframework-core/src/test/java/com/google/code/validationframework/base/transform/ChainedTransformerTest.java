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

package com.google.code.validationframework.base.transform;

import com.google.code.validationframework.api.transform.Transformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * @see ChainedTransformer
 */
public class ChainedTransformerTest {

    @Test
    public void testBoolean() {
        // Test with no transformer
        ChainedTransformer<Boolean, Boolean> transformer = new ChainedTransformer<Boolean, Boolean>(null);

        // Test with one transformer
        transformer = transformer.chain(new NegateBooleanTransformer());
        assertTrue(transformer.transform(false));

        // Test with two transformers
        transformer = transformer.chain(new NegateBooleanTransformer());
        assertTrue(transformer.transform(true));
    }

    @Test
    public void testType() {
        ChainedTransformer<Object, Number> transformer = new ChainedTransformer<Object,
                Number>(new CastTransformer<Object, Number>());
        ChainedTransformer<Object, Double> transformer2 = transformer.chain(new CastTransformer<Number, Double>());
        ChainedTransformer<Object, Integer> transformer3 = transformer2.chain(new Transformer<Double, Integer>() {
            @Override
            public Integer transform(Double input) {
                return input.intValue();
            }
        });

        assertEquals(Integer.valueOf(3), transformer3.transform(3.2d));
    }
}
