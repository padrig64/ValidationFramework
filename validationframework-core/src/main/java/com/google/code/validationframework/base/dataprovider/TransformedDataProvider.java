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

package com.google.code.validationframework.base.dataprovider;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.CastTransformer;

/**
 * Wrapper for data providers that will transform the data to another type.
 * <p/>
 * This can be useful to adapt existing data providers to be put in validators requiring another type of input.
 *
 * @param <DPO>  Type of data returned by the wrapped data provider.
 * @param <TDPO> Type of data to be validated.<br>
 *               It can be, for instance, the type of data handled by a component, or the type of the component itself.
 *
 * @see DataProvider
 * @see Disposable
 */
public class TransformedDataProvider<DPO, TDPO> implements DataProvider<TDPO>, Disposable {

    /**
     * Wrapped data provider whose output is to be transformed.
     */
    private final DataProvider<DPO> wrappedDataProvider;

    /**
     * Transformer adapting the output of the wrapped data provider.
     */
    private final Transformer<DPO, TDPO> dataTransformer;

    /**
     * Constructor specifying the wrapped data provider and the transformer to be used to adapt the output of the
     * wrapped data provider.
     *
     * @param wrappedDataProvider Wrapped data provider whose output is to be transformed.
     * @param dataTransformer     Transformer adapting the output of the wrapped data provider.<br>
     *                            If null, the provided data will be cast to the wanted type. In case the data cannot be
     *                            cast, null will be provided.
     */
    public TransformedDataProvider(DataProvider<DPO> wrappedDataProvider, Transformer<DPO, TDPO> dataTransformer) {
        this.wrappedDataProvider = wrappedDataProvider;
        if (dataTransformer == null) {
            this.dataTransformer = new CastTransformer<DPO, TDPO>();
        } else {
            this.dataTransformer = dataTransformer;
        }
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public TDPO getData() {
        TDPO transformedData = null;

        if (wrappedDataProvider != null) {
            transformedData = dataTransformer.transform(wrappedDataProvider.getData());
        }

        return transformedData;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (wrappedDataProvider instanceof Disposable) {
            ((Disposable) wrappedDataProvider).dispose();
        }
        if (dataTransformer instanceof Disposable) {
            ((Disposable) dataTransformer).dispose();
        }
    }
}
