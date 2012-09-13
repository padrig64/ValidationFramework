/*
 * Copyright THALES NEDERLAND B.V. and/or its suppliers
 *
 * THIS SOFTWARE SOURCE CODE AND ANY EXECUTABLE DERIVED THEREOF ARE PROPRIETARY
 * TO THALES NEDERLAND B.V. AND/OR ITS SUPPLIERS, AS APPLICABLE, AND SHALL NOT
 * BE USED IN ANY WAY OTHER THAN BEFOREHAND AGREED ON BY THALES NEDERLAND B.V.,
 * NOR BE REPRODUCED OR DISCLOSED TO THIRD PARTIES WITHOUT PRIOR WRITTEN
 * AUTHORIZATION BY THALES NEDERLAND B.V. AND/OR ITS SUPPLIERS, AS APPLICABLE.
 */

package com.github.moawad.validationframework.swing.rule;

import java.text.ParseException;

import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;

import com.github.moawad.validationframework.base.rule.string.AbstractStringBooleanRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience rule that will check if the formatter of the editor component of a spinner can successfully parse the
 * input text.
 */
public class JSpinnerFormatterRule extends AbstractStringBooleanRule {

	/**
	 * Logger for this class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(JSpinnerFormatterRule.class);

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
		this.spinner = spinner;
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
					// Nothing to be done
				}
			}
		}

		return result;
	}
}
