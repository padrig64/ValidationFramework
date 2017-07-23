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

/**
 * Transformer negating the boolean input.
 *
 * @see Transformer
 */
public class NegateBooleanTransformer implements Transformer<Boolean, Boolean> {

    /**
     * Default result to be returned for the negation of null input.
     */
    private static final Boolean DEFAULT_NULL_NEGATION = null;

    /**
     * Result to be returned for the negation of null input.
     */
    private final Boolean nullNegation;

    /**
     * Default constructor considering null to be the result returned for the negation of null input.
     */
    public NegateBooleanTransformer() {
        this(DEFAULT_NULL_NEGATION);
    }

    /**
     * Constructor specifying the result to be returned for the negation of null input.
     *
     * @param nullNegation Result to be returned for the negation of null input.
     */
    public NegateBooleanTransformer(Boolean nullNegation) {
        this.nullNegation = nullNegation;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public Boolean transform(Boolean input) {
        Boolean output;

        if (input == null) {
            output = nullNegation;
        } else {
            output = !input;
        }

        return output;
    }
}
