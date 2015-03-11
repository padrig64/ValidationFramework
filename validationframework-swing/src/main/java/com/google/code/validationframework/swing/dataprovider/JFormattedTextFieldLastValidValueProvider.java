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

package com.google.code.validationframework.swing.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.CastTransformer;

import javax.swing.JFormattedTextField;

/**
 * Data provider retrieving the last valid value from a formatted textfield.
 *
 * @param <DPO> Type of data in the formatted textfield.<br>
 *              You may use {@link Object}.
 *
 * @see DataProvider
 * @see JFormattedTextField
 * @see JFormattedTextField#getValue()
 */
public class JFormattedTextFieldLastValidValueProvider<DPO> implements DataProvider<DPO> {

    /**
     * Formatted textfield to retrieve the last valid value from.
     */
    private final JFormattedTextField formattedTextField;

    /**
     * Transformer to convert the formatted textfield value.
     */
    private final Transformer<Object, DPO> transformer;

    /**
     * Constructor specifying the formatted textfield to retrieve the last valid value from.
     * <p/>
     * By default, the formatted textfield will be cast to DPO.
     *
     * @param formattedTextField Formatted textfield to retrieve the last valid value from.
     */
    public JFormattedTextFieldLastValidValueProvider(JFormattedTextField formattedTextField) {
        this(formattedTextField, new CastTransformer<Object, DPO>());
    }

    /**
     * Constructor specifying the formatted textfield to retrieve the last valid value from, and the transformer to
     * convert the formatted textfield value to DPO.
     *
     * @param formattedTextField Formatted textfield to retrieve the last valid value from.
     * @param transformer        Transformer to convert the formatted textfield value.
     */
    public JFormattedTextFieldLastValidValueProvider(JFormattedTextField formattedTextField, Transformer<Object,
            DPO> transformer) {
        this.formattedTextField = formattedTextField;
        this.transformer = transformer;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public JFormattedTextField getComponent() {
        return formattedTextField;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public DPO getData() {
        return transformer.transform(formattedTextField.getValue());
    }
}
