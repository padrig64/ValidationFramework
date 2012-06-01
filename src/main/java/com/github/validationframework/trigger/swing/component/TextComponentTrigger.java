package com.github.validationframework.trigger.swing.component;

import com.github.validationframework.trigger.AbstractTrigger;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;

public class TextComponentTrigger<C extends JTextComponent> extends AbstractTrigger<C> {

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

	private C textComponent = null;

	private final InputAdapter inputAdapter = new InputAdapter();

	public TextComponentTrigger(final C inputComponent) {
		super();
		attach(inputComponent);
	}

	public void attach(final C inputComponent) {
		detach();

		textComponent = inputComponent;
		if (textComponent != null) {
			textComponent.getDocument().addDocumentListener(inputAdapter);
		}
	}

	public void detach() {
		if (textComponent != null) {
			textComponent.getDocument().removeDocumentListener(inputAdapter);
			textComponent = null;
		}
	}

	@Override
	public C getInput() {
		return textComponent;
	}
}
