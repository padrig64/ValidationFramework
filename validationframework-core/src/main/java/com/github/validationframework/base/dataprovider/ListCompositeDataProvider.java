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

package com.github.validationframework.base.dataprovider;

import com.github.validationframework.api.dataprovider.DataProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Composite data provider returning the data of all the sub-data providers in a list.
 *
 * @param <D> Type of data in the list.
 *
 * @see DataProvider
 */
public class ListCompositeDataProvider<D> implements DataProvider<List<D>> {

    /**
     * Sub-data providers
     */
    protected final List<DataProvider<D>> dataProviders = new ArrayList<DataProvider<D>>();

    /**
     * Adds the specified data provider
     *
     * @param dataProvider Data provider to be added.
     */
    public void addDataProvider(final DataProvider<D> dataProvider) {
        dataProviders.add(dataProvider);
    }

    /**
     * Removes the specified data provider.
     *
     * @param dataProvider Data provider to be removed.
     */
    public void removeDataProvider(final DataProvider<D> dataProvider) {
        dataProviders.remove(dataProvider);
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public List<D> getData() {
        final List<D> dataList = new ArrayList<D>();

        // Read data from all data providers and put them in the list
        for (final DataProvider<D> dataProvider : dataProviders) {
            dataList.add(dataProvider.getData());
        }

        return dataList;
    }
}
