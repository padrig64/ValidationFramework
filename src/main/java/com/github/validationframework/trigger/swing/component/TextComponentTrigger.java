/*
 * Copyright (c) 2012, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
