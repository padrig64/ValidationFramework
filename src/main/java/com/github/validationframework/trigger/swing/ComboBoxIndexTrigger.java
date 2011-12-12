package com.github.validationframework.trigger.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import com.github.validationframework.trigger.AbstractTrigger;

public class ComboBoxIndexTrigger<R> extends AbstractTrigger<Integer> {

	private class InputAdapter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			triggerValidation();
		}
	}

	private JComboBox comboBox = null;

	private InputAdapter inputAdapter = new InputAdapter();

	public ComboBoxIndexTrigger(JComboBox inputComponent) {
		super();
		attach(inputComponent);
	}

	public void attach(JComboBox inputComponent) {
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
	public Integer getData() {
		Integer index = -1;

		if (comboBox != null) {
			comboBox.getSelectedIndex();
		}

		return index;
	}
}
