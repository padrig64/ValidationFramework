package com.github.validationframework.test.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.ColorUIResource;

import com.github.validationframework.feedback.swing.AbstractColorFeedBack;
import com.github.validationframework.feedback.swing.AbstractIconTipFeedBack;
import com.github.validationframework.feedback.swing.AbstractToolTipFeedBack;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.swing.TextFieldStringTrigger;
import com.github.validationframework.validator.DefaultValidator;
import net.miginfocom.swing.MigLayout;

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

		OK(true, "", null, null, null),
		NOK_EMPTY(false, "Should not be empty", "/icons/warning.png", null, COLOR_NOK_EMPTY),
		NOK_TOO_LONG(false, "Cannot be more than 4 characters", "/icons/invalid2.png", COLOR_NOK_TOO_LONG, null);

		private final boolean aggregatableResult;
		private String text;
		private Icon icon;
		private Color foreground;
		private Color background;

		TextFieldResult(boolean aggregatableResult, String text, String iconName, Color foreground, Color background) {
			this.aggregatableResult = aggregatableResult;
			this.text = text;
			this.foreground = foreground;
			this.background = background;

			// Error icon
			if ((iconName != null) && !iconName.isEmpty()) {
				InputStream inputStream = getClass().getResourceAsStream(iconName);
				try {
					BufferedImage image = ImageIO.read(inputStream);
					icon = new ImageIcon(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public Icon getIcon() {
			return icon;
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
			setIcon(result.getIcon());
			setToolTipText(result.toString());
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
		JPanel contentPane = new JPanel(new MigLayout("fill, wrap 2", "[]related[grow]", "[]related[]related[]unrelated[]"));
		setContentPane(contentPane);

		// First textfield
		contentPane.add(new JLabel("Tooltip:"));
		JTextField textField = new JTextField();
		contentPane.add(textField, "growx");
		DefaultValidator<String, TextFieldResult> validator1 = new DefaultValidator<String, TextFieldResult>();
		validator1.addTrigger(new TextFieldStringTrigger(textField));
		validator1.addRule(new TextFieldDataRule());
		validator1.addFeedBack(new TextFieldToolTipFeedBack(textField));

		// Second textfield
		contentPane.add(new JLabel("Color:"));
		textField = new JTextField();
		contentPane.add(textField, "growx");
		DefaultValidator<String, TextFieldResult> validator2 = new DefaultValidator<String, TextFieldResult>();
		validator2.addTrigger(new TextFieldStringTrigger(textField));
		validator2.addRule(new TextFieldDataRule());
		validator2.addFeedBack(new TextFieldColorFeedBack(textField));

		// Third textfield
		contentPane.add(new JLabel("Icon tip:"));
		textField = new JTextField();
		contentPane.add(textField, "growx");
		DefaultValidator<String, TextFieldResult> validator3 = new DefaultValidator<String, TextFieldResult>();
		validator3.addTrigger(new TextFieldStringTrigger(textField));
		validator3.addRule(new TextFieldDataRule());
		validator3.addFeedBack(new TextFieldIconTipFeedBack(textField));

		// Apply button
		JButton button = new JButton("Apply");
		contentPane.add(button, "growx, span");

		// Set size
		pack();
//		Dimension size = new Dimension(getContentPane().getPreferredSize());
//		size.width = 200;
//		setSize(size);
//		setMinimumSize(getContentPane().getMinimumSize());
		Dimension size = getSize();
		size.width += 100;
		setSize(size);

		// Set location
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {

				// Set look-and-feel
				try {
					for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							break;
						}
					}
				} catch (UnsupportedLookAndFeelException e) {
					// handle exception
				} catch (ClassNotFoundException e) {
					// handle exception
				} catch (InstantiationException e) {
					// handle exception
				} catch (IllegalAccessException e) {
					// handle exception
				}

				// Show window
				new TestFrame().setVisible(true);
			}
		});
	}
}
