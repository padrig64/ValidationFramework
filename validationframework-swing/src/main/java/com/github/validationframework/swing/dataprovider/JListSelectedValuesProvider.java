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

package com.github.validationframework.swing.dataprovider;

import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.base.transform.CastTransformer;
import com.github.validationframework.base.transform.Transformer;

import javax.swing.JList;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Data provider retrieving the selected values of a list.
 *
 * @param <DPO> Type of data in the list.<br>You may use {@link Object}.
 *
 * @see DataProvider
 * @see JList
 * @see JList#getSelectedValue()
 * @see JListSelectedIndexProvider
 * @see JListSelectedIndicesProvider
 * @see JListSelectedValueProvider
 */
public class JListSelectedValuesProvider<DPO> implements DataProvider<Collection<DPO>> {

    /**
     * List to get the selected values from.
     */
    private final JList list;

    /**
     * Transformer to convert the list values.
     */
    private final Transformer<Object, DPO> transformer;

    /**
     * Constructor specifying the list to get the selected values from.<br>By default, the list values will be cast to
     * {@link DPO}.
     *
     * @param list List to get the selected values from.
     */
    public JListSelectedValuesProvider(final JList list) {
        this(list, new CastTransformer<Object, DPO>());
    }

    /**
     * Constructor specifying the list to get the selected values from, and the transformer to convert the list values
     * to {@link DPO}.
     *
     * @param list        List to get the selected values from.
     * @param transformer Transformer to convert the list values.
     */
    public JListSelectedValuesProvider(final JList list, final Transformer<Object, DPO> transformer) {
        this.list = list;
        this.transformer = transformer;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public JList getComponent() {
        return list;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public Collection<DPO> getData() {
        final Collection<DPO> values = new ArrayList<DPO>();

        for (final Object value : list.getSelectedValues()) {
            values.add(transformer.transform(value));
        }

        return values;
    }
}
