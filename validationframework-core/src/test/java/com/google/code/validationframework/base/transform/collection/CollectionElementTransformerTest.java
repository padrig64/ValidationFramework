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

package com.google.code.validationframework.base.transform.collection;

import com.google.code.validationframework.base.transform.ToStringTransformer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @see CollectionElementTransformer
 */
public class CollectionElementTransformerTest {

    @Test
    public void testNonNull() {
        CollectionElementTransformer<Long, String> transformer = new CollectionElementTransformer<Long,
                String>(new ToStringTransformer<Long>());

        // Prepare input to be transformed
        List<Long> input = new ArrayList<Long>();
        for (long i = 1000; i < 1030; i++) {
            input.add(i);
        }

        // Perform transformation
        Collection<String> output = transformer.transform(input);

        // Check output
        int i = 0;
        for (String outputElement : output) {
            assertEquals(input.get(i++).longValue(), Long.parseLong(outputElement));
        }
    }

    @Test
    public void testNull() {
        CollectionElementTransformer<Long, String> transformer = new CollectionElementTransformer<Long,
                String>(new ToStringTransformer<Long>("null value"));

        // Prepare input to be transformed
        List<Long> input = new ArrayList<Long>();
        input.add(null);

        // Perform transformation
        Collection<String> output = transformer.transform(input);

        // Check output
        assertEquals("null value", output.iterator().next());
    }
}
