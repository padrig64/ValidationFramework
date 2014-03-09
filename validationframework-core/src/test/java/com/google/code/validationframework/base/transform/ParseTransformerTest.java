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

import java.text.DecimalFormat;

import static org.junit.Assert.assertEquals;

/**
 * @see ParseTransformer
 */
public class ParseTransformerTest {

    @Test
    public void testDefaultParsing() {
        Transformer<String, Number> transformer = new ParseTransformer<Number>(new DecimalFormat(), false);

        assertEquals(1.23, transformer.transform("1.23"));
        assertEquals(45.6, transformer.transform("45.6invalid"));
        assertEquals(null, transformer.transform("invalid"));
        assertEquals(null, transformer.transform(""));
        assertEquals(null, transformer.transform(null));
    }

    @Test
    public void testStrictParsing() {
        Transformer<String, Number> transformer = new ParseTransformer<Number>(new DecimalFormat(), true);

        assertEquals(1.23, transformer.transform("1.23"));
        assertEquals(null, transformer.transform("4.56invalid"));
        assertEquals(null, transformer.transform("invalid"));
        assertEquals(null, transformer.transform(""));
        assertEquals(null, transformer.transform(null));
    }
}
