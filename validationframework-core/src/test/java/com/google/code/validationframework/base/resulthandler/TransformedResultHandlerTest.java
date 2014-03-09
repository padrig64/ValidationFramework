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

package com.google.code.validationframework.base.resulthandler;

import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.base.transform.ToStringTransformer;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TransformedResultHandlerTest {

    @Test
    public void testNonNull() {
        TransformedResultHandler<Boolean, String> resultHandler = new TransformedResultHandler<Boolean,
                String>(new ToStringTransformer<Boolean>(), new ResultHandler<String>() {

            @Override
            public void handleResult(String result) {
                assertEquals("true", result);
            }
        });
        resultHandler.handleResult(true);

        resultHandler = new TransformedResultHandler<Boolean, String>(new ToStringTransformer<Boolean>(),
                new ResultHandler<String>() {

            @Override
            public void handleResult(String result) {
                assertEquals("false", result);
            }
        });
        resultHandler.handleResult(false);
    }

    @Test
    public void testNull() {
        TransformedResultHandler<Boolean, String> resultHandler = new TransformedResultHandler<Boolean,
                String>(new ToStringTransformer<Boolean>("null value"), new ResultHandler<String>() {

            @Override
            public void handleResult(String result) {
                assertEquals("null value", result);
            }
        });
        resultHandler.handleResult(null);
    }
}
