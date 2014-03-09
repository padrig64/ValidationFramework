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

package com.google.code.validationframework.swing.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.CastTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;
import java.text.ParseException;

/**
 * Data provider reading the value from a spinner.
 * <p/>
 * Note that the value is not read from the model, but instead corresponds to the current text.
 *
 * @param <DPO> Type of data in the spinner editor.<br>
 *              You may use {@link Object}.
 *
 * @see DataProvider
 * @see JSpinner
 */
public class JSpinnerEditorValueProvider<DPO> implements DataProvider<DPO> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSpinnerEditorValueProvider.class);

    /**
     * Spinner to get the data from.
     * <p/>
     * Note that the text and the formatter are retrieved every time they are needed so that we do not have to listener
     * to changes in the spinner to track them.
     */
    private final JSpinner spinner;

    /**
     * Transformer used to convert the object parsed from the spinner to the expected type.
     */
    private final Transformer<Object, DPO> transformer;

    /**
     * Constructor specifying the spinner to get the value from.
     *
     * @param spinner Spinner to get the value from.
     */
    public JSpinnerEditorValueProvider(JSpinner spinner) {
        this(spinner, new CastTransformer<Object, DPO>());
    }

    /**
     * Constructor specifying the spinner to get the value from and the transformer to convert it to the required type.
     *
     * @param spinner     Spinner to get the value from.
     * @param transformer Transformer used to convert the object parsed from the spinner to the expected type.
     */
    public JSpinnerEditorValueProvider(JSpinner spinner, Transformer<Object, DPO> transformer) {
        this.spinner = spinner;
        this.transformer = transformer;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public JSpinner getComponent() {
        return spinner;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public DPO getData() {
        DPO typedValue = null;

        try {
            // Parse text
            JFormattedTextField.AbstractFormatter formatter = getFormatter();
            if (formatter != null) {
                Object dataValue = formatter.stringToValue(getText());

                // Convert it to the required type
                typedValue = transformer.transform(dataValue);
            }
        } catch (ParseException e) {
            typedValue = null;
        }

        return typedValue;
    }

    /**
     * Retrieves the text currently in the text component of the spinner, if any.
     * <p/>
     * Note that if editor of the spinner has been customized, this method may need to be adapted in a sub-class.
     *
     * @return Spinner text or null if not found.
     */
    protected String getText() {
        String text = null;

        // Try to find a text component in the spinner
        JTextComponent textEditorComponent;
        if (spinner.getEditor() instanceof JTextComponent) {
            textEditorComponent = (JTextComponent) spinner.getEditor();
        } else if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
            textEditorComponent = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        } else {
            LOGGER.error("No text component can be found in the spinner to get the text: " + spinner);
            textEditorComponent = null;
        }

        // Read text from text component
        if (textEditorComponent != null) {
            text = textEditorComponent.getText();
        }

        return text;
    }

    /**
     * Retrieves the formatter currently in the text component of the spinner, if any.
     * <p/>
     * Note that if editor of the spinner has been customized, this method may need to be adapted in a sub-class.
     *
     * @return Spinner formatter or null if not found.
     */
    protected JFormattedTextField.AbstractFormatter getFormatter() {
        JFormattedTextField.AbstractFormatter formatter = null;

        // Try to find a text component in the spinner
        JFormattedTextField textEditorComponent;
        if (spinner.getEditor() instanceof JFormattedTextField) {
            textEditorComponent = (JFormattedTextField) spinner.getEditor();
        } else if (spinner.getEditor() instanceof JSpinner.DefaultEditor) {
            textEditorComponent = ((JSpinner.DefaultEditor) spinner.getEditor()).getTextField();
        } else {
            LOGGER.error("No formatted textfield can be found in the spinner to get the formatter: " + spinner);
            textEditorComponent = null;
        }

        // Read text from text component
        if (textEditorComponent != null) {
            formatter = textEditorComponent.getFormatter();
        }

        return formatter;
    }
}
