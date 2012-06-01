package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.swing.component.TextComponentTrigger;
import javax.swing.JEditorPane;

public class EditorPaneStringTrigger extends TextComponentTrigger<JEditorPane> {

	public EditorPaneStringTrigger(final JEditorPane inputComponent) {
		super(inputComponent);
	}
}
