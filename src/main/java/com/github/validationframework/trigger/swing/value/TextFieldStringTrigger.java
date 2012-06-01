package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.swing.component.TextComponentTrigger;
import javax.swing.JTextField;

public class TextFieldStringTrigger extends TextComponentTrigger<JTextField> {

	public TextFieldStringTrigger(final JTextField inputComponent) {
		super(inputComponent);
	}
}
