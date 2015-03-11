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
import java.text.ParseException;

/**
 * Data provider reading the value from a formatted textfield.
 * <p/>
 * Note that the value is not read from the model, but instead corresponds to the current text.
 *
 * @see DataProvider
 * @see JFormattedTextField
 * @see JFormattedTextField#getText()
 */
public class JFormattedTextFieldValueProvider<DPO> implements DataProvider<DPO> {

    /**
     * Formatted textfield to get the data from.
     * <p/>
     * Note that the text and the formatter are retrieved every time they are needed so that we do not have to listener
     * to changes in the formatted textfield to track them.
     */
    private final JFormattedTextField formattedTextField;

    /**
     * Transformer used to convert the object parsed from the formatted textfield to the expected type.
     */
    private final Transformer<Object, DPO> transformer;

    /**
     * Constructor specifying the formatted textfield to get the value from.
     *
     * @param formattedTextField Formatted textfield to get the value from.
     */
    public JFormattedTextFieldValueProvider(JFormattedTextField formattedTextField) {
        this(formattedTextField, new CastTransformer<Object, DPO>());
    }

    /**
     * Constructor specifying the formatted textfield to get the value from and the transformer to convert it to the
     * required type.
     *
     * @param formattedTextField Formatted textfield to get the value from.
     * @param transformer        Transformer used to convert the object parsed from the spinner to the expected type.
     */
    public JFormattedTextFieldValueProvider(JFormattedTextField formattedTextField, Transformer<Object,
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
        DPO typedValue = null;

        try {
            // Parse text
            String dataText = formattedTextField.getText();
            JFormattedTextField.AbstractFormatter formatter = formattedTextField.getFormatter();
            if (formatter != null) {
                Object dataValue = formatter.stringToValue(dataText);

                // Convert it to the required type
                typedValue = transformer.transform(dataValue);
            }
        } catch (ParseException e) {
            typedValue = null;
        }

        return typedValue;
    }
}
