package validation.trigger.swing;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import validation.trigger.AbstractTrigger;

public class TextFieldStringTrigger extends AbstractTrigger<String> {

	private class InputAdapter implements DocumentListener {

		public void insertUpdate(DocumentEvent e) {
			System.out.println("TextFieldStringTrigger$InputAdapter.insertUpdate");
			triggerValidation();
		}

		public void removeUpdate(DocumentEvent e) {
			System.out.println("TextFieldStringTrigger$InputAdapter.removeUpdate");
			triggerValidation();
		}

		public void changedUpdate(DocumentEvent e) {
			System.out.println("TextFieldStringTrigger$InputAdapter.changedUpdate");
			triggerValidation();
		}
	}

	private JTextField textField = null;

	private InputAdapter inputAdapter = new InputAdapter();

	public TextFieldStringTrigger(JTextField inputComponent) {
		super();
		attachComponent(inputComponent);
	}

	public void attachComponent(JTextField inputComponent) {
		detachComponent(textField);

		textField = inputComponent;
		if (textField != null) {
			textField.getDocument().addDocumentListener(inputAdapter);
		}
	}

	public void detachComponent(JTextField inputComponent) {
		if (textField != null) {
			textField.getDocument().removeDocumentListener(inputAdapter);
			textField = null;
		}
	}

	@Override
	protected String getData() {
		String data = null;

		if (textField != null) {
			data = textField.getText();
		}

		return data;
	}
}
