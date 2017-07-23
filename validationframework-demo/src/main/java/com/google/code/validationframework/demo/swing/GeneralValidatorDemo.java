/*
 * Copyright (c) 2017, ValidationFramework Authors
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
import com.google.code.validationframework.base.resulthandler.ResultCollector;
import com.google.code.validationframework.base.rule.bool.AndBooleanRule;
import com.google.code.validationframework.base.rule.string.StringRegexRule;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;
import com.google.code.validationframework.base.trigger.ManualTrigger;
import com.google.code.validationframework.swing.dataprovider.JFormattedTextFieldTextProvider;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import com.google.code.validationframework.swing.resulthandler.AbstractColorFeedback;
import com.google.code.validationframework.swing.resulthandler.AbstractIconFeedback;
import com.google.code.validationframework.swing.resulthandler.AbstractStickerFeedback;
import com.google.code.validationframework.swing.resulthandler.bool.ComponentEnablingBooleanResultHandler;
import com.google.code.validationframework.swing.resulthandler.bool.IconBooleanFeedback;
import com.google.code.validationframework.swing.rule.JFormattedTextFieldFormatterRule;
import com.google.code.validationframework.swing.trigger.JFormattedTextFieldDocumentChangedTrigger;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import com.google.code.validationframework.swing.trigger.JTextFieldKeyStrokeTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.plaf.ColorUIResource;
import javax.swing.text.NumberFormatter;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;

import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.collect;
import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

public class GeneralValidatorDemo extends JFrame {

    /**
     * UIResource in case look-and-feel changes while result is visible. The new look-and-feel is allowed to replace it
     * colors, otherwise, we may introduce permanent inconsistencies.
     */
    private static final Color COLOR_NOK_EMPTY = new ColorUIResource(new Color(226, 125, 125, 127));
    private static final Color COLOR_NOK_TOO_LONG = new ColorUIResource(new Color(226, 125, 125));

    private enum InputFieldResult {

        OK("", null, null, null),
        NOK_EMPTY("Should not be empty", IconUtils.WARNING_ICON, null, COLOR_NOK_EMPTY),
        NOK_TOO_LONG("Cannot be more than 4 characters", IconUtils.INVALID_ICON, COLOR_NOK_TOO_LONG, null);

        private final String text;
        private Icon icon;
        private final Color foreground;
        private final Color background;

        InputFieldResult(String text, Icon icon, Color foreground, Color background) {
            this.text = text;
            this.foreground = foreground;
            this.background = background;
            this.icon = icon;
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
        public Boolean transform(InputFieldResult input) {
            return InputFieldResult.OK.equals(input);
        }
    }

    private class InputFieldRule implements Rule<String, InputFieldResult> {

        @Override
        public InputFieldResult validate(String input) {
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

        public InputFieldToolTipFeedback(JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(InputFieldResult result) {
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

        public InputFieldColorFeedback(JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(InputFieldResult result) {
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

        public InputFieldIconFeedback(JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(InputFieldResult result) {
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
    public GeneralValidatorDemo() {
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
        JPanel contentPane = new JPanel(new MigLayout("fill, wrap 2", "[]related[grow]",
                "[]related[]related[]related[]unrelated[]"));
//		contentPane.setBorder(new EmptyBorder(50, 50, 50, 50));
//		setContentPane(new JScrollPane(contentPane));
        setContentPane(contentPane);

        // Input fields
        contentPane.add(new JLabel("Tooltip:"));
        JTextField textField1 = new JTextField();
        contentPane.add(textField1, "growx");
        contentPane.add(new JLabel("Color:"));
        JTextField textField2 = new JTextField();
        contentPane.add(textField2, "growx");
        contentPane.add(new JLabel("Icon:"));
        JTextField textField3 = new JTextField();
        contentPane.add(textField3, "growx");
        contentPane.add(new JLabel("Icon with tooltip:"));

        NumberFormat courseFormat = NumberFormat.getIntegerInstance();
        courseFormat.setMinimumIntegerDigits(3);
        courseFormat.setMaximumIntegerDigits(4);
        courseFormat.setMaximumFractionDigits(0);
        NumberFormatter courseFormatter = new NumberFormatter(courseFormat);
        courseFormatter.setMinimum(0.0);
        courseFormatter.setMaximum(359.0);
        JFormattedTextField formattedTextField4 = new JFormattedTextField(courseFormatter);
        contentPane.add(formattedTextField4, "growx");

        contentPane.add(new JLabel("Icon with tooltip:"));

        courseFormat = NumberFormat.getIntegerInstance();
        courseFormat.setMinimumIntegerDigits(3);
        courseFormat.setMaximumIntegerDigits(4);
        courseFormat.setMaximumFractionDigits(0);
        courseFormatter = new NumberFormatter(courseFormat);
        courseFormatter.setMinimum(0.0);
        courseFormatter.setMaximum(359.0);
        JFormattedTextField formattedTextField5 = new JFormattedTextField(courseFormatter);
        contentPane.add(formattedTextField5, "growx");

        // Apply button
        JButton applyButton = new JButton("Apply");
        contentPane.add(applyButton, "growx, span");

        // Set size
        pack();
        Dimension size = getSize();
        size.width += 100;
        setMinimumSize(size);

        // Set location
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);

        // Create validators
        ResultCollector<InputFieldResult, Boolean> resultCollector1 = new ResultCollector<InputFieldResult,
                Boolean>(new InputFieldResultToBooleanTransformer());
        ResultCollector<InputFieldResult, Boolean> resultCollector2 = new ResultCollector<InputFieldResult,
                Boolean>(new InputFieldResultToBooleanTransformer());
        ResultCollector<InputFieldResult, Boolean> resultCollector3 = new ResultCollector<InputFieldResult,
                Boolean>(new InputFieldResultToBooleanTransformer());
        ResultCollector<Boolean, Boolean> resultCollector4 = new ResultCollector<Boolean, Boolean>();
        ResultCollector<Boolean, Boolean> resultCollector5 = new ResultCollector<Boolean, Boolean>();
        createValidator1(textField1, resultCollector1);
        createValidator2(textField2, resultCollector2);
        createValidator3(textField3, resultCollector3);
        createValidator4(formattedTextField4, resultCollector4);
        createValidator4(formattedTextField5, resultCollector5);

        // Create global validator
        collect(resultCollector1) //
                .collect(resultCollector2) //
                .collect(resultCollector3) //
                .collect(resultCollector4) //
                .collect(resultCollector5) //
                .check(new AndBooleanRule()) //
                .handleWith(new ComponentEnablingBooleanResultHandler(applyButton));
    }

    private void createValidator1(JTextField textField, ResultCollector<InputFieldResult, Boolean> resultCollector) {
//        on(new JTextFieldDocumentChangedTrigger(textField)) //
        on(new JTextFieldKeyStrokeTrigger(textField, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,
                0))).read(new JTextFieldTextProvider(textField)) //
                .check(new InputFieldRule()) //
                .handleWith(new InputFieldToolTipFeedback(textField)) //
                .handleWith(resultCollector);
    }

    private void createValidator2(JTextField textField, ResultCollector<InputFieldResult, Boolean> resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new InputFieldRule()) //
                .handleWith(new InputFieldColorFeedback(textField)) //
                .handleWith(resultCollector);
    }

    private void createValidator3(JTextField textField, ResultCollector<InputFieldResult, Boolean> resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new InputFieldRule()) //
                .handleWith(new InputFieldIconFeedback(textField)) //
                .handleWith(resultCollector);
    }

    private void createValidator4(JFormattedTextField formattedTextField, ResultCollector<Boolean,
            Boolean> resultCollector) {

        ManualTrigger manualTrigger = new ManualTrigger();
        JFormattedTextFieldDocumentChangedTrigger trigger = new JFormattedTextFieldDocumentChangedTrigger
                (formattedTextField);
        JFormattedTextFieldTextProvider dataProvider = new JFormattedTextFieldTextProvider(formattedTextField);
        JFormattedTextFieldFormatterRule rule1 = new JFormattedTextFieldFormatterRule(formattedTextField);
        StringRegexRule rule2 = new StringRegexRule("^[0-9]{1,3}$");
        IconBooleanFeedback resultHandler1 = new IconBooleanFeedback(formattedTextField, null, null,
                IconBooleanFeedback.DEFAULT_INVALID_ICON, "Angle should be between 000 and 359");

        // Example of decoration that would be clipped by the parent panel
        resultHandler1.setClippingAncestor((JComponent) formattedTextField.getParent().getParent());

        on(trigger) //
                .on(manualTrigger) //
                .read(dataProvider) //
                .check(rule1) //
                .check(rule2) //
                .transform(new AndBooleanAggregator()) //
                .handleWith(resultHandler1) //
                .handleWith(resultCollector);

        // Test of triggering the validation even before the dialog is visible
        manualTrigger.trigger();
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
                new GeneralValidatorDemo().setVisible(true);
            }
        });
    }
}
