package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.swing.component.TextComponentTrigger;
import javax.swing.JTextPane;

public class TextPaneStringTrigger extends TextComponentTrigger<JTextPane> {

	public TextPaneStringTrigger(final JTextPane inputComponent) {
		super(inputComponent);
	}
}
