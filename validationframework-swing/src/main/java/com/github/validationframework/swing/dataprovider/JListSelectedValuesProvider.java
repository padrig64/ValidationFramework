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

import javax.swing.JList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Data provider retrieving the selected values of a list.
 *
 * @see DataProvider
 * @see JList
 * @see JList#getSelectedValue()
 * @see JListSelectedIndexProvider
 * @see JListSelectedIndicesProvider
 * @see JListSelectedValueProvider
 */
public class JListSelectedValuesProvider implements DataProvider<Collection<Object>> {

    /**
     * List to get the selected values from.
     */
    private final JList list;

    /**
     * Constructor specifying the list to get the selected values from.
     *
     * @param list List to get the selected values from.
     */
    public JListSelectedValuesProvider(final JList list) {
        this.list = list;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public Collection<Object> getData() {
        final Collection<Object> values = new ArrayList<Object>();
        Collections.addAll(values, list.getSelectedValues());
        return values;
    }
}
