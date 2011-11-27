package validation.test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;

import validation.feedback.AbstractColorFeedBack;
import validation.feedback.AbstractToolTipFeedBack;
import validation.rule.Rule;
import validation.validator.TextFieldValidator;

public class TestFrame extends JFrame {

	private class TextFieldRule implements Rule<String, TextFieldResult> {

		public TextFieldResult validate(String input) {
			TextFieldResult result = TextFieldResult.OK;

			if ((input == null) || (input.isEmpty())) {
				result = TextFieldResult.NOK_EMPTY;
			} else if (input.length() >= 5) {
				result = TextFieldResult.NOK_TOO_LONG;
			}

			return result;
		}
	}

	/**
	 * UIResource in case look-and-feel changes while result is visible. The new look-and-feel is allowed to replace it
	 * colors, otherwise, we may introduce permanent inconsistencies.
	 */
	private static final Color COLOR_NOK_EMPTY = new ColorUIResource(new Color(226, 125, 125, 127));
	private static final Color COLOR_NOK_TOO_LONG = new ColorUIResource(new Color(226, 125, 125));

	private enum TextFieldResult {

		OK("", null, null),
		NOK_EMPTY("Cannot be empty", null, COLOR_NOK_EMPTY),
		NOK_TOO_LONG("Should be less than 5 characters", COLOR_NOK_TOO_LONG, null);

		private String text;
		private Color foreground;
		private Color background;

		TextFieldResult(String text, Color foreground, Color background) {
			this.text = text;
			this.foreground = foreground;
			this.background = background;
		}

		public Color getForeground() {
			return foreground;
		}

		public Color getBackground() {
			return background;
		}

		@Override
		public String toString() {
			return text;
		}
	}

	private class TextFieldToolTipFeedBack extends AbstractToolTipFeedBack<TextFieldResult> {

		public TextFieldToolTipFeedBack(JComponent owner) {
			super(owner);
		}

		@Override
		public void feedback(TextFieldResult result) {
			setToolTipText(result.toString());
			switch (result) {
				case OK:
					hideToolTip();
					break;
				default:
					showToolTip();
			}
		}
	}

	private class TextFieldColorFeedBack extends AbstractColorFeedBack<TextFieldResult> {

		public TextFieldColorFeedBack(JComponent owner) {
			super(owner);
		}

		@Override
		public void feedback(TextFieldResult result) {
			setForeground(result.getForeground());
			setBackground(result.getBackground());
			switch (result) {
				case OK:
					hideColors();
					break;
				default:
					showColors();
			}
		}
	}

	public TestFrame() {
		super();
		init();
	}

	private void init() {
		setTitle("Validation Framework Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create contents
		JPanel contentPane = new JPanel(new GridLayout(3, 1));
		setContentPane(contentPane);

		JTextField textField = new JTextField();
		contentPane.add(textField);
		TextFieldValidator<TextFieldResult> validator1 = new TextFieldValidator<TextFieldResult>(textField);
		validator1.addRule(new TextFieldRule());
		validator1.addFeedBack(new TextFieldToolTipFeedBack(textField));
		validator1.addFeedBack(new TextFieldColorFeedBack(textField));

		textField = new JTextField();
		contentPane.add(textField);
		TextFieldValidator<TextFieldResult> validator2 = new TextFieldValidator<TextFieldResult>(textField);
		validator2.addRule(new TextFieldRule());
		validator2.addFeedBack(new TextFieldToolTipFeedBack(textField));
		validator2.addFeedBack(new TextFieldColorFeedBack(textField));

		JButton button = new JButton("Apply");
		contentPane.add(button);

		// Set size
		pack();
		Dimension size = new Dimension(getPreferredSize());
		size.width = 200;
		setSize(size);

		// Set location
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestFrame().setVisible(true);
			}
		});
	}
}
