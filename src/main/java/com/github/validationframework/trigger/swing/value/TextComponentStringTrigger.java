package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.AbstractTrigger;
import com.github.validationframework.trigger.swing.component.TextComponentTrigger;
import javax.swing.text.JTextComponent;

public class TextComponentStringTrigger<C extends JTextComponent> extends AbstractTrigger<String> {

	private final TextComponentTrigger<C> textComponentTrigger;

	public TextComponentStringTrigger(final C inputComponent) {
		super();
		textComponentTrigger = new TextComponentTrigger<C>(inputComponent);
	}

	@Override
	public String getInput() {
		return textComponentTrigger.getInput().getText();
	}
}
