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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Composite transformer.
 *
 * @param <I> Type of input of the first transformer of the chain.
 * @param <O> Type of output of the last transformer of the chain.
 */
public class ChainedTransformer<I, O> implements Transformer<I, O> {

    /**
     * Transformers that are part of the chain.
     */
    private final Collection<Transformer<?, ?>> transformers = new ArrayList<Transformer<?, ?>>();

    /**
     * Last transformer casting the output to the wanted type.
     */
    private final Transformer<Object, O> lastTransformer = new CastTransformer<Object, O>();

    /**
     * Adds the specified transformer to the chain.
     *
     * @param transformer Transformer to be added.
     * @param <TO>        Type of output of the specified transformer.
     *
     * @return This.
     */
    @SuppressWarnings("unchecked")
    public <TO> ChainedTransformer<I, TO> chain(Transformer<O, TO> transformer) {
        transformers.add(transformer);
        return (ChainedTransformer<I, TO>) this;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @SuppressWarnings("unchecked")
    @Override
    public O transform(I input) {
        Object rawOutput = input;

        for (Transformer transformer : transformers) {
            rawOutput = transformer.transform(rawOutput);
        }

        return lastTransformer.transform(rawOutput);
    }
}
