package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.swing.component.TextComponentTrigger;
import javax.swing.JPasswordField;

public class PasswordFieldStringTrigger extends TextComponentTrigger<JPasswordField> {

	public PasswordFieldStringTrigger(final JPasswordField inputComponent) {
		super(inputComponent);
	}
}
