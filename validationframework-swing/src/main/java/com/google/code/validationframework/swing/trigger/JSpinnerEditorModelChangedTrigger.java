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

package com.google.code.validationframework.swing.trigger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.text.JTextComponent;

// TODO Don't extend from JTextComponentDocumentChangedTrigger
public class JSpinnerEditorModelChangedTrigger extends JTextComponentDocumentChangedTrigger {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JSpinnerEditorModelChangedTrigger.class);

    public JSpinnerEditorModelChangedTrigger(final JSpinner source) {
        super(getEditorTextComponent(source));
        // TODO Track editor component and document changes
    }

    private static JTextComponent getEditorTextComponent(final JSpinner spinner) {
        JTextComponent textComponent = null;

        // Try to find a text component in the spinner
        final JComponent spinnerEditor = spinner.getEditor();
        if (spinnerEditor instanceof JTextComponent) {
            textComponent = (JTextComponent) spinnerEditor;
        } else if (spinnerEditor instanceof JSpinner.DefaultEditor) {
            textComponent = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
        } else {
            LOGGER.error("Cannot find any text component in the spinner editor component: " + spinnerEditor);
            textComponent = null;
        }

        return textComponent;
    }
}
