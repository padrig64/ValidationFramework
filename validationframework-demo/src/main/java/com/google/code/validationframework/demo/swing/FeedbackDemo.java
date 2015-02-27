/*
 * Copyright (c) 2014, Patrick Moawad
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

package com.google.code.validationframework.demo.swing;

import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.simple.SimpleProperty;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.resulthandler.AbstractColorFeedback;
import com.google.code.validationframework.swing.resulthandler.AbstractIconFeedback;
import com.google.code.validationframework.swing.resulthandler.AbstractStickerFeedback;
import com.google.code.validationframework.swing.resulthandler.bool.IconBooleanFeedback;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

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
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

public class FeedbackDemo extends JFrame {

    /**
     * UIResource in case look-and-feel changes while result is visible. The new look-and-feel is allowed to replace it
     * colors, otherwise, we may introduce permanent inconsistencies.
     */
    private static final Color COLOR_NOK_EMPTY = new ColorUIResource(new Color(226, 125, 125, 127));

    private static final Color COLOR_NOK_TOO_LONG = new ColorUIResource(new Color(226, 125, 125));

    private enum InputFieldResult {

        OK("", null, null, null),
        NOK_EMPTY("Should not be empty", "/images/defaults/warning.png", null, COLOR_NOK_EMPTY),
        NOK_TOO_LONG("Cannot be more than 4 characters", "/images/defaults/invalid.png", COLOR_NOK_TOO_LONG, null);

        private final String text;
        private Icon icon;
        private final Color foreground;
        private final Color background;

        InputFieldResult(final String text, final String iconName, final Color foreground, final Color background) {
            this.text = text;
            this.foreground = foreground;
            this.background = background;

            // Error icon
            if ((iconName != null) && !iconName.isEmpty()) {
                final InputStream inputStream = getClass().getResourceAsStream(iconName);
                try {
                    final BufferedImage image = ImageIO.read(inputStream);
                    icon = new ImageIcon(image);
                } catch (final IOException e) {
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
    }

    private class InputFieldResultToBooleanTransformer implements Transformer<InputFieldResult, Boolean> {

        @Override
        public Boolean transform(final InputFieldResult input) {
            return InputFieldResult.OK.equals(input);
        }
    }

    private class InputFieldRule implements Rule<String, InputFieldResult> {

        @Override
        public InputFieldResult validate(final String input) {
            InputFieldResult result = InputFieldResult.OK;

            if ((input == null) || (input.isEmpty())) {
                result = InputFieldResult.NOK_EMPTY;
            } else if (input.length() >= 5) {
                result = InputFieldResult.NOK_TOO_LONG;
            }

            return result;
        }
    }

    private class InputFieldToolTipFeedback extends AbstractStickerFeedback<InputFieldResult> {

        public InputFieldToolTipFeedback(final JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(final InputFieldResult result) {
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

    private class InputFieldColorFeedback extends AbstractColorFeedback<InputFieldResult> {

        public InputFieldColorFeedback(final JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(final InputFieldResult result) {
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

    private class InputFieldIconFeedback extends AbstractIconFeedback<InputFieldResult> {

        public InputFieldIconFeedback(final JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(final InputFieldResult result) {
            setIcon(result.getIcon());
            switch (result) {
                case OK:
                    hideIcon();
                    break;
                default:
                    showIcon();
            }
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -2039502440268195814L;

    /**
     * Default constructor.
     */
    public FeedbackDemo() {
        super();
        init();
    }

    /**
     * Initializes the frame by creating its contents.
     */
    private void init() {
        setTitle("Validation Framework Test");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create content pane
        final JPanel contentPane = new JPanel(new MigLayout("fill, wrap 2", "[]related[grow]",
            "[]related[]related[]related[]unrelated[]"));
        //		contentPane.setBorder(new EmptyBorder(50, 50, 50, 50));
        //		setContentPane(new JScrollPane(contentPane));
        setContentPane(contentPane);

        // Input fields
        contentPane.add(new JLabel("Tooltip:"));
        final JTextField textField1 = new JTextField();
        contentPane.add(textField1, "growx");
        contentPane.add(new JLabel("Color:"));
        final JTextField textField2 = new JTextField();
        contentPane.add(textField2, "growx");
        contentPane.add(new JLabel("Icon:"));
        final JTextField textField3 = new JTextField();
        contentPane.add(textField3, "growx");
        contentPane.add(new JLabel("Icon with tooltip:"));
        final JTextField textField4 = new JTextField();
        contentPane.add(textField4, "growx");

        // Apply button
        final JButton applyButton = new JButton("Apply");
        contentPane.add(applyButton, "growx, span");

        // Set size
        pack();
        final Dimension size = getSize();
        size.width += 100;
        setMinimumSize(size);

        // Set location
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);

        // Create validators
        final SimpleProperty<InputFieldResult> resultCollector1 = new SimpleProperty<InputFieldResult>();
        final SimpleProperty<InputFieldResult> resultCollector2 = new SimpleProperty<InputFieldResult>();
        final SimpleProperty<InputFieldResult> resultCollector3 = new SimpleProperty<InputFieldResult>();
        final SimpleProperty<InputFieldResult> resultCollector4 = new SimpleProperty<InputFieldResult>();
        createValidator1(textField1, resultCollector1);
        createValidator2(textField2, resultCollector2);
        createValidator3(textField3, resultCollector3);
        createValidator4(textField4, resultCollector4);

        //        // Create global validator
        //        collect(resultCollector1) //
        //            .collect(resultCollector2) //
        //            .collect(resultCollector3) //
        //            .collect(resultCollector4) //
        //            .check(new AndBooleanRule()) //
        //            .handleWith(new ComponentEnablingBooleanResultHandler(applyButton));
    }

    private void createValidator1(final JTextField textField, final SimpleProperty<InputFieldResult> resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField))
            .read(new JTextFieldTextProvider(textField))
            .check(new InputFieldRule())
            .handleWith(new InputFieldToolTipFeedback(textField))
            .handleWith(resultCollector)
            .trigger();
    }

    private void createValidator2(final JTextField textField, final SimpleProperty<InputFieldResult> resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField))
            .read(new JTextFieldTextProvider(textField))
            .check(new InputFieldRule())
            .handleWith(new InputFieldColorFeedback(textField))
            .handleWith(resultCollector)
            .trigger();
    }

    private void createValidator3(final JTextField textField, final SimpleProperty<InputFieldResult> resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField))
            .read(new JTextFieldTextProvider(textField))
            .check(new InputFieldRule())
            .handleWith(new InputFieldIconFeedback(textField))
            .handleWith(resultCollector)
            .trigger();
    }

    private void createValidator4(final JTextField textField, final SimpleProperty<InputFieldResult> resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField))
            .read(new JTextFieldTextProvider(textField))
            .check(new InputFieldRule())
            .handleWith(new InputFieldIconFeedback(textField))
            .handleWith(resultCollector)
            .trigger();
    }

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                // Set look-and-feel
                try {
                    for (final UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (final UnsupportedLookAndFeelException e) {
                    // handle exception
                } catch (final ClassNotFoundException e) {
                    // handle exception
                } catch (final InstantiationException e) {
                    // handle exception
                } catch (final IllegalAccessException e) {
                    // handle exception
                }

                // Show window
                new FeedbackDemo().setVisible(true);
            }
        });
    }
}
