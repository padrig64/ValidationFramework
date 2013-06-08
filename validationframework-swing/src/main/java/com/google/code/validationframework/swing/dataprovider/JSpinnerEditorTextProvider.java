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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

/**
 * Data provider reading the text from the text editor of a spinner.
 *
 * @see DataProvider
 * @see JSpinner
 */
public class JSpinnerEditorTextProvider implements DataProvider<String> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSpinnerEditorTextProvider.class);

    /**
     * Spinner to get the text from.
     */
    private final JSpinner spinner;

    /**
     * Constructor specifying the spinner to get the text from.
     *
     * @param spinner Spinner to get the text from.
     */
    public JSpinnerEditorTextProvider(final JSpinner spinner) {
        this.spinner = spinner;
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
    public String getData() {
        final String data;

        // Try to find a text component in the spinner
        final JTextComponent textComponent;
        final JComponent spinnerEditor = spinner.getEditor();
        if (spinnerEditor instanceof JTextComponent) {
            textComponent = (JTextComponent) spinnerEditor;
        } else if (spinnerEditor instanceof JSpinner.DefaultEditor) {
            textComponent = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
        } else {
            textComponent = null;
        }

        // Read text from the text component if found
        if (textComponent == null) {
            LOGGER.warn("Cannot read text from spinner editor component: " + spinnerEditor);
            data = null;
        } else {
            data = textComponent.getText();
        }

        return data;
    }
}
