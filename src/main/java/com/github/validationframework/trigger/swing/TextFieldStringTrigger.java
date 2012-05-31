package com.github.validationframework.trigger.swing;

import com.github.validationframework.trigger.AbstractTrigger;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class TextFieldStringTrigger extends AbstractTrigger<String> {

	private class InputAdapter implements DocumentListener {

		@Override
		public void insertUpdate(final DocumentEvent e) {
			triggerValidation();
		}

		@Override
		public void removeUpdate(final DocumentEvent e) {
			triggerValidation();
		}

		@Override
		public void changedUpdate(final DocumentEvent e) {
			triggerValidation();
		}
	}

	private JTextField textField = null;

	private final InputAdapter inputAdapter = new InputAdapter();

	public TextFieldStringTrigger(final JTextField inputComponent) {
		super();
		attach(inputComponent);
	}

	public void attach(final JTextField inputComponent) {
		detach();

		textField = inputComponent;
		if (textField != null) {
			textField.getDocument().addDocumentListener(inputAdapter);
		}
	}

	public void detach() {
		if (textField != null) {
			textField.getDocument().removeDocumentListener(inputAdapter);
			textField = null;
		}
	}

	@Override
	public String getData() {
		String data = null;

		if (textField != null) {
			data = textField.getText();
		}

		return data;
	}
}
