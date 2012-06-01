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
