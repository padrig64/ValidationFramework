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

package com.google.code.validationframework.swing.rule;

import com.google.code.validationframework.base.rule.string.AbstractStringBooleanRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import java.text.ParseException;

/**
 * Convenience rule that will check if the formatter of the editor component of a spinner can successfully parse the
 * input text.
 */
public class JSpinnerFormatterRule extends AbstractStringBooleanRule {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSpinnerFormatterRule.class);

    /**
     * Spinner whose formatter is to be checked.
     */
    private final JSpinner spinner;

    /**
     * Constructor specifying the spinner whose formatter is to be checked.
     *
     * @param spinner Spinner whose formatter is to be checked.
     */
    public JSpinnerFormatterRule(final JSpinner spinner) {
        super();
        this.spinner = spinner;
    }

    /**
     * Gets the spinner whose formatter is to be checked.
     *
     * @return Spinner whose formatter is to be checked.
     */
    public JSpinner getComponent() {
        return spinner;
    }

    /**
     * @see AbstractStringBooleanRule#validate(Object)
     */
    @Override
    public Boolean validate(final String data) {
        Boolean result = false;

        // Try to find a formatted textfield in the spinner
        final JFormattedTextField formattedTextField;
        final JComponent spinnerEditor = spinner.getEditor();
        if (spinnerEditor instanceof JFormattedTextField) {
            formattedTextField = (JFormattedTextField) spinnerEditor;
        } else if (spinnerEditor instanceof JSpinner.DefaultEditor) {
            formattedTextField = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
        } else {
            formattedTextField = null;
        }

        if (formattedTextField == null) {
            LOGGER.warn("Cannot find any formatted textfield in the spinner editor component: " + spinnerEditor);
        } else {
            // Try to parse the input value
            final JFormattedTextField.AbstractFormatter formatter = formattedTextField.getFormatter();
            if (formatter != null) {
                final String dataToBeValidated = trimIfNeeded(data);
                try {
                    formatter.stringToValue(dataToBeValidated);
                    result = true;
                } catch (ParseException e) {
                    result = false;
                }
            }
        }

        return result;
    }
}
