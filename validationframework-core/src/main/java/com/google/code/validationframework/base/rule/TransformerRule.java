/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.base.rule;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;

/**
 * Rule making use of a {@link Transformer} to produce the results.
 *
 * @param <RI> Type of data to be transformed to RO.
 * @param <RO> Type of data transformed form RI.
 *
 * @see Rule
 * @see Disposable
 */
public class TransformerRule<RI, RO> implements Rule<RI, RO>, Disposable {

    /**
     * Transformer that will transform the data under validation to a result.
     */
    private final Transformer<RI, RO> transformer;

    /**
     * Constructor specifying the {@link Transformer} to be used to produce the results.
     *
     * @param transformer Transformer to be used to produce the results.
     */
    public TransformerRule(Transformer<RI, RO> transformer) {
        this.transformer = transformer;
    }

    /**
     * @see Rule#validate(Object)
     */
    @Override
    public RO validate(RI data) {
        RO transformedData = null;

        if (transformer != null) {
            transformedData = transformer.transform(data);
        }

        return transformedData;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (transformer instanceof Disposable) {
            ((Disposable) transformer).dispose();
        }
    }
}
