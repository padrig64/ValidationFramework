/*
 * Copyright THALES NEDERLAND B.V. and/or its suppliers
 *
 * THIS SOFTWARE SOURCE CODE AND ANY EXECUTABLE DERIVED THEREOF ARE PROPRIETARY
 * TO THALES NEDERLAND B.V. AND/OR ITS SUPPLIERS, AS APPLICABLE, AND SHALL NOT
 * BE USED IN ANY WAY OTHER THAN BEFOREHAND AGREED ON BY THALES NEDERLAND B.V.,
 * NOR BE REPRODUCED OR DISCLOSED TO THIRD PARTIES WITHOUT PRIOR WRITTEN
 * AUTHORIZATION BY THALES NEDERLAND B.V. AND/OR ITS SUPPLIERS, AS APPLICABLE.
 */

package com.github.validationframework.swing.trigger;

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
