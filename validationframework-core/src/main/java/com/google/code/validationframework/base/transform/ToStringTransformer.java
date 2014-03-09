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

/**
 * Transformer using the {@link Object#toString()} method of the given objects.
 *
 * @param <I> Type of input to be transformed.<br>
 *            It is needed only to avoid compilation warning when using the validator builders.
 */
public class ToStringTransformer<I> implements Transformer<I, String> {

    /**
     * Default value to be returned in case null is passed to the {@link #transform(Object)} method.
     */
    private static final String DEFAULT_NULL_TO_STRING = null;

    /**
     * Value to be returned in case null is passed to the {@link #transform(Object)} method.
     */
    private final String nullToString;

    /**
     * Default constructor.
     * <p/>
     * If null is passed to the {@link #transform(Object)} method, null will be returned.
     */
    public ToStringTransformer() {
        this(DEFAULT_NULL_TO_STRING);
    }

    /**
     * Constructor specif the value to be returned in case null is passed to the {@link #transform(Object)} method.
     *
     * @param nullToString Value to be returned in case null is passed to the {@link #transform(Object)} method.
     */
    public ToStringTransformer(String nullToString) {
        this.nullToString = nullToString;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public String transform(I input) {
        String str;

        if (input == null) {
            str = nullToString;
        } else {
            str = input.toString();
        }

        return str;
    }
}
