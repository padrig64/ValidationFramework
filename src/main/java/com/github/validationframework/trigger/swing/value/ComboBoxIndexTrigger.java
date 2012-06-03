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

package com.github.validationframework.trigger.swing.value;

import com.github.validationframework.trigger.AbstractTrigger;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;

public class ComboBoxIndexTrigger<R> extends AbstractTrigger<Integer> {

	private class InputAdapter implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			triggerValidation();
		}
	}

	private JComboBox comboBox = null;

	private final InputAdapter inputAdapter = new InputAdapter();

	public ComboBoxIndexTrigger(final JComboBox inputComponent) {
		super();
		attach(inputComponent);
	}

	public void attach(final JComboBox inputComponent) {
		detach();

		comboBox = inputComponent;

		if (comboBox != null) {
			comboBox.addActionListener(inputAdapter);
		}
	}

	public void detach() {
		if (comboBox != null) {
			comboBox.removeActionListener(inputAdapter);
			comboBox = null;
		}
	}

	@Override
	public Integer getInput() {
		final Integer index = -1;

		if (comboBox != null) {
			comboBox.getSelectedIndex();
		}

		return index;
	}
}
