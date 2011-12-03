package validation.datavalidator.swing;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import validation.datavalidator.AbstractDataValidator;

public class TextFieldStringValidator<R> extends AbstractDataValidator<String, R> {

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

	public TextFieldStringValidator(JTextField inputComponent) {
		super();
		attachComponent(inputComponent);
	}

	public void attachComponent(JTextField inputComponent) {
		detachComponent(textField);

		textField = inputComponent;
		textField.getDocument().addDocumentListener(inputAdapter);
	}

	public void detachComponent(JTextField inputComponent) {
		if (textField != null) {
			textField.getDocument().removeDocumentListener(inputAdapter);
			textField = null;
		}
	}

	@Override
	protected String getData() {
		return textField.getText();
	}
}
