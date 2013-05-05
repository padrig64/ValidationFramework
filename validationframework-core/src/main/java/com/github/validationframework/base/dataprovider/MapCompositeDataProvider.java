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

import java.util.HashMap;
import java.util.Map;

/**
 * Composite data provider returning the data of all the sub-data providers in a map.
 *
 * @param <K> Type of the keys to retrieve the data from the map.
 * @param <D> Type of data in the map.
 *
 * @see DataProvider
 */
public class MapCompositeDataProvider<K, D> implements DataProvider<Map<K, D>> {

    /**
     * Sub-data providers.
     */
    private final Map<K, DataProvider<D>> dataProviders = new HashMap<K, DataProvider<D>>();

    /**
     * Adds the specified data provider with the specified key.
     *
     * @param key          Key associated to the data provider.
     * @param dataProvider Data provider associated to the key.
     */
    public void addDataProvider(final K key, final DataProvider<D> dataProvider) {
        dataProviders.put(key, dataProvider);
    }

    /**
     * Results the data provider associated to the specified key.
     *
     * @param key Key associated to the data provider to be remove.
     */
    public void removeDataProvider(final K key) {
        dataProviders.remove(key);
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public Map<K, D> getData() {
        final Map<K, D> dataList = new HashMap<K, D>();

        // Read the data from all data providers and put them in the map
        for (final Map.Entry<K, DataProvider<D>> entry : dataProviders.entrySet()) {
            dataList.put(entry.getKey(), entry.getValue().getData());
        }

        return dataList;
    }
}
