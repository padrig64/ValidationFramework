package validation.trigger.swing;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import validation.trigger.AbstractTrigger;

public class TextFieldStringTrigger extends AbstractTrigger<String> {

	private class InputAdapter implements DocumentListener {

		public void insertUpdate(DocumentEvent e) {
			triggerValidation();
		}

		public void removeUpdate(DocumentEvent e) {
			triggerValidation();
		}

		public void changedUpdate(DocumentEvent e) {
			triggerValidation();
		}
	}

	private JTextField textField = null;

	private InputAdapter inputAdapter = new InputAdapter();

	public TextFieldStringTrigger(JTextField inputComponent) {
		super();
		attach(inputComponent);
	}

	public void attach(JTextField inputComponent) {
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
