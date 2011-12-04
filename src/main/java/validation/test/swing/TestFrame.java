package validation.test.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.plaf.ColorUIResource;

import net.miginfocom.swing.MigLayout;
import validation.feedback.swing.AbstractColorFeedBack;
import validation.feedback.swing.AbstractIconTipFeedBack;
import validation.feedback.swing.AbstractToolTipFeedBack;
import validation.rule.Rule;
import validation.trigger.swing.TextFieldStringTrigger;
import validation.validator.DefaultValidator;

public class TestFrame extends JFrame {

	private class TextFieldDataRule implements Rule<String, TextFieldResult> {

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

	private enum TextFieldResult /*implements AggregatableResult<Boolean>*/ {

		OK(true, "", null, null),
		NOK_EMPTY(false, "Cannot be empty", null, COLOR_NOK_EMPTY),
		NOK_TOO_LONG(false, "Should be less than 5 characters", COLOR_NOK_TOO_LONG, null);

		private boolean aggregatableResult;
		private String text;
		private Color foreground;
		private Color background;

		TextFieldResult(boolean aggregatableResult, String text, Color foreground, Color background) {
			this.aggregatableResult = aggregatableResult;
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

//		@Override
//		public Boolean getAggregatableResult() {
//			return aggregatableResult;
//		}
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

	private class TextFieldIconTipFeedBack extends AbstractIconTipFeedBack<TextFieldResult> {

		public TextFieldIconTipFeedBack(JComponent owner) {
			super(owner);
		}

		@Override
		public void feedback(TextFieldResult result) {
			switch (result) {
				case OK:
					hideIconTip();
					break;
				default:
					showIconTip();
			}
		}
	}

//	private class GroupRule implements Rule<Object, GroupResult> {
//
//		@Override
//		public GroupResult validate(Object input) {
//			GroupResult result = GroupResult.OK;
//
//			// TODO
//
//			return result;
//		}
//	}

//	private enum GroupResult {
//		OK(true),
//		NOK(false);
//
//		private final boolean applyEnabled;
//
//		GroupResult(boolean applyEnabled) {
//			this.applyEnabled = applyEnabled;
//		}
//
//		public boolean isApplyEnabled() {
//			return applyEnabled;
//		}
//	}

	public TestFrame() {
		super();
		init();
	}

	private void init() {
		setTitle("Validation Framework Test");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create contents
		JPanel contentPane = new JPanel(new MigLayout("fill, wrap 1", "", "[]related[]related[]unrelated[]"));
		setContentPane(contentPane);

		// First textfield
		JTextField textField = new JTextField();
		contentPane.add(textField, "growx");
		DefaultValidator<String, TextFieldResult> validator1 = new DefaultValidator<String, TextFieldResult>();
		validator1.addTrigger(new TextFieldStringTrigger(textField));
		validator1.addRule(new TextFieldDataRule());
		validator1.addFeedBack(new TextFieldToolTipFeedBack(textField));

		// Second textfield
		textField = new JTextField();
		contentPane.add(textField, "growx");
		DefaultValidator<String, TextFieldResult> validator2 = new DefaultValidator<String, TextFieldResult>();
		validator2.addTrigger(new TextFieldStringTrigger(textField));
		validator2.addRule(new TextFieldDataRule());
		validator2.addFeedBack(new TextFieldColorFeedBack(textField));

		// Third textfield
		textField = new JTextField();
		contentPane.add(textField, "growx");
		DefaultValidator<String, TextFieldResult> validator3 = new DefaultValidator<String, TextFieldResult>();
		validator3.addTrigger(new TextFieldStringTrigger(textField));
		validator3.addRule(new TextFieldDataRule());
		validator3.addFeedBack(new TextFieldIconTipFeedBack(textField));

		// Apply button
		JButton button = new JButton("Apply");
		contentPane.add(button, "growx");

		// Set size
		pack();
		Dimension size = new Dimension(getContentPane().getPreferredSize());
		size.width = 200;
		setSize(size);
		setMinimumSize(getContentPane().getMinimumSize());

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
