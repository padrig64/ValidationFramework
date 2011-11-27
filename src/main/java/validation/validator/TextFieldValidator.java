package validation.validator;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import validation.feedback.FeedBack;
import validation.rule.Rule;

public class TextFieldValidator<R> extends AbstractValidator<String, R> {

	private class DocumentAdapter implements DocumentListener {

		public void insertUpdate(DocumentEvent e) {
			performValidation();
		}

		public void removeUpdate(DocumentEvent e) {
			performValidation();
		}

		public void changedUpdate(DocumentEvent e) {
			performValidation();
		}
	}

	private JTextField textField = null;

	private DocumentAdapter inputAdapter = new DocumentAdapter();

	public TextFieldValidator(JTextField inputComponent) {
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
