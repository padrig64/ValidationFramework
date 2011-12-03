package validation.validator.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import validation.validator.AbstractDataValidator;

public class ComboBoxIndexValidator<R> extends AbstractDataValidator<Integer, R> {

	private class InputAdapter implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			triggerValidation();
		}
	}

	private JComboBox comboBox = null;

	private InputAdapter inputAdapter = new InputAdapter();

	public ComboBoxIndexValidator(JComboBox inputComponent) {
		super();
		attachComponent(inputComponent);
	}

	public void attachComponent(JComboBox inputComponent) {
		detachComponent(comboBox);

		comboBox = inputComponent;
		comboBox.addActionListener(inputAdapter);
	}

	public void detachComponent(JComboBox inputComponent) {
		if (comboBox != null) {
			comboBox.removeActionListener(inputAdapter);
			comboBox = null;
		}
	}

	@Override
	protected Integer getData() {
		return comboBox.getSelectedIndex();
	}
}
