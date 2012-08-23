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

package com.github.validationframework.experiment.utils.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import javax.swing.JFormattedTextField;
import javax.swing.text.JTextComponent;

// TODO
public class FormatAndSelectOnEnterFeature {

	private class EnterKeyAdapter extends KeyAdapter {

		/**
		 * @see KeyListener#keyTyped(KeyEvent)
		 */
		@Override
		public void keyTyped(final KeyEvent event) {
			final Object source = event.getSource();

			// Format
			if (source instanceof JFormattedTextField) {
				final JFormattedTextField formattedTextField = (JFormattedTextField) source;
				final String text = formattedTextField.getText();
				final JFormattedTextField.AbstractFormatter formatter = formattedTextField.getFormatter();
				if (formatter != null) {
					try {
						final Object value = formatter.stringToValue(text);
						formattedTextField.setValue(value);
					} catch (ParseException e) {
						System.err.println("FormatAndSelectOnEnterFeature$EnterKeyAdapter.keyTyped: \"" + text + "\"");
						e.printStackTrace();
					}
				}
			}

			// Select all
			if (source instanceof JTextComponent) {
				((JTextComponent) source).selectAll();
			}
		}
	}

	private final Set<JTextComponent> textComponents = new HashSet<JTextComponent>();

	private final KeyListener enterKeyAdapter = new EnterKeyAdapter();

	public FormatAndSelectOnEnterFeature(final JTextComponent textComponent) {
		attach(textComponent);
	}

	public void attach(final JTextComponent textComponent) {
		if (!textComponents.contains(textComponent)) {
			textComponent.addKeyListener(enterKeyAdapter);
			textComponents.add(textComponent);
		}
	}

	public void detach(final JTextComponent textComponent) {
		textComponents.remove(textComponent);
		textComponent.removeKeyListener(enterKeyAdapter);
	}
}
