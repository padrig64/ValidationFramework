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

import javax.swing.JComboBox;

/**
 * Data provider retrieving the selected value of a combobox.
 *
 * @param <DPO> Type of data in the combobox.<br>You may use {@link Object}.
 *
 * @see DataProvider
 * @see JComboBoxSelectedIndexProvider
 * @see JComboBox
 * @see JComboBox#getSelectedItem()
 */
public class JComboBoxSelectedValueProvider<DPO> implements DataProvider<DPO> {

    /**
     * Combobox to get the selected value from.
     */
    private final JComboBox comboBox;

    /**
     * Transformer to convert the combobox value.
     */
    private final Transformer<Object, DPO> transformer;

    /**
     * Constructor specifying the combobox to get the selected value from.<br>By default, the combobox value will be
     * cast to DPO.
     *
     * @param comboBox Combobox to get the selected value from.
     */
    public JComboBoxSelectedValueProvider(final JComboBox comboBox) {
        this(comboBox, new CastTransformer<Object, DPO>());
    }

    /**
     * Constructor specifying the combobox to get the selected value from, and the transformer to convert the combobox
     * value to DPO.
     *
     * @param comboBox    Combobox to get the selected value from.
     * @param transformer Transformer to convert the combobox value.
     */
    public JComboBoxSelectedValueProvider(final JComboBox comboBox, final Transformer<Object, DPO> transformer) {
        this.comboBox = comboBox;
        this.transformer = transformer;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public JComboBox getComponent() {
        return comboBox;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public DPO getData() {
        return transformer.transform(comboBox.getSelectedItem());
    }
}
