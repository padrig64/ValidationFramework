package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.swing.component.TextComponentTrigger;
import javax.swing.JFormattedTextField;

public class FormattedTextFieldStringTrigger extends TextComponentTrigger<JFormattedTextField> {

	public FormattedTextFieldStringTrigger(final JFormattedTextField inputComponent) {
		super(inputComponent);
	}
}
