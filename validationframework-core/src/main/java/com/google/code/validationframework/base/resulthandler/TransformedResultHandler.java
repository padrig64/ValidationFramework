/*
 * Copyright (c) 2013, Patrick Moawad
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
import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

/**
 * Wrapper for result handlers that will transform the result to another type.<br>This can be useful, for instance, to
 * adapt existing result handlers to be put in validators requiring another type of result.
 *
 * @param <RI>  Type of result to be transformed.
 * @param <TRI> Type of result handled by the wrapped result handler.
 */
public class TransformedResultHandler<RI, TRI> implements ResultHandler<RI> {

    /**
     * Wrapped result handler whose input will be transformed.
     */
    private final ResultHandler<TRI> wrappedResultHandler;

    /**
     * Transformer adapting the result before processing with the wrapped result handler.
     */
    final Transformer<RI, TRI> resultTransformer;

    /**
     * Constructor specifying the wrapped result handler and the transformer to be used to adapt the result
     * before processing with the wrapped result handler.
     *
     * @param wrappedResultHandler Wrapped result handler whose input will be transformed.
     * @param resultTransformer    Transformer adapting the result before processing with the wrapped result
     *                             handler.<br>If null, the result will be cast to the wanted type. In case the
     *                             result cannot be cast, null will be provided.
     */
    public TransformedResultHandler(final ResultHandler<TRI> wrappedResultHandler, final Transformer<RI,
            TRI> resultTransformer) {
        this.wrappedResultHandler = wrappedResultHandler;
        if (resultTransformer == null) {
            this.resultTransformer = new CastTransformer<RI, TRI>();
        } else {
            this.resultTransformer = resultTransformer;
        }
    }

    /**
     * @see ResultHandler#handleResult(Object)
     */
    @Override
    public void handleResult(final RI result) {
        wrappedResultHandler.handleResult(resultTransformer.transform(result));
    }
}
