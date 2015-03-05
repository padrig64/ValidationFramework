/*
 * Copyright (c) 2015, Patrick Moawad
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

import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.simple.SimpleProperty;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;
import com.google.code.validationframework.base.transform.collection.CollectionElementTransformer;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import com.google.code.validationframework.swing.property.ComponentEnabledProperty;
import com.google.code.validationframework.swing.resulthandler.AbstractColorFeedback;
import com.google.code.validationframework.swing.resulthandler.AbstractIconFeedback;
import com.google.code.validationframework.swing.resulthandler.AbstractStickerFeedback;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.Icon;
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
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import static com.google.code.validationframework.base.binding.Binder.read;
import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

/**
 * Example showing how different types of feedback could be achieved (icon, tooltip, enabling/disabling component,
 * etc.).
 */
public class FeedbackDemo extends JFrame {

    /**
     * Input validation rule.
     */
    private class InputFieldRule implements Rule<String, Result> {

        @Override
        public Result validate(String input) {
            Result result;

            if ((input == null) || (input.length() < 5)) {
                result = Result.OK;
            } else if (input.length() >= 5 && input.length() < 10) {
                result = Result.QUITE_LONG;
            } else {
                result = Result.TOO_LONG;
            }

            return result;
        }
    }

    /**
     * Type of result returned by the validation rules.
     */
    private enum Result {

        OK("", null, null),
        QUITE_LONG("Entered text is quite long", IconUtils.WARNING_ICON, new Color(230, 175, 0)),
        TOO_LONG("Entered text is too long", IconUtils.INVALID_ICON, new Color(255, 60, 56));

        private final String message;
        private Icon icon;
        private final Color color;

        Result(String message, Icon icon, Color color) {
            this.message = message;
            this.color = color;
            this.icon = icon;
        }

        public String getMessage() {
            return message;
        }

        public Icon getIcon() {
            return icon;
        }

        public Color getColor() {
            return color;
        }
    }

    /**
     * Transformer to convert rule results to booleans.
     */
    private class ResultToBooleanTransformer implements Transformer<Result, Boolean> {

        @Override
        public Boolean transform(Result input) {
            // OK and QUITE_LONG are valid, but TOO_LONG is invalid
            return (input != Result.TOO_LONG);
        }
    }

    /**
     * Result handler showing a sticker on the side of the input field.
     */
    private class StickerFeedback extends AbstractStickerFeedback<Result> {

        public StickerFeedback(JComponent owner) {
            super(owner);
        }

        @Override
        public void handleResult(Result result) {
            setToolTipText(result.getMessage());
            switch (result) {
                case OK:
                    hideToolTip();
                    break;
                default:
                    showToolTip();
            }
        }
    }

    /**
     * Result handler changing the background or foreground color of the input field.
     */
    private class ColorFeedback extends AbstractColorFeedback<Result> {

        private final boolean background;

        public ColorFeedback(JComponent owner, boolean background) {
            super(owner);
            this.background = background;
        }

        @Override
        public void handleResult(Result result) {
            if (background) {
                setBackground(result.getColor());
            } else {
                setForeground(result.getColor());
            }
            switch (result) {
                case OK:
                    hideColors();
                    break;
                default:
                    showColors();
            }
        }
    }

    /**
     * Result handler decorating the input field with an icon with an optional tooltip.
     */
    private class IconFeedback extends AbstractIconFeedback<Result> {

        private final boolean showToolTip;

        public IconFeedback(JComponent owner, boolean showToolTip) {
            super(owner);
            this.showToolTip = showToolTip;
        }

        @Override
        public void handleResult(Result result) {
            setIcon(result.getIcon());
            if (showToolTip) {
                setToolTipText(result.getMessage());
            } else {
                setToolTipText(null);
            }
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

    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JTextField textField4;
    private JTextField textField5;
    private JButton applyButton;

    /**
     * Constructor.
     */
    public FeedbackDemo() {
        super();
        initComponents();
        initValidation();
    }

    /**
     * Initializes the frame by creating its contents.
     */
    private void initComponents() {
        setTitle("Validation Framework Test");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Create content pane
        String notForNimbus;
        if ("Nimbus".equals(UIManager.getLookAndFeel().getName())) {
            notForNimbus = "";
        } else {
            notForNimbus = "related[]";
        }
        JPanel contentPane = new JPanel(new MigLayout("fill, wrap 2", "[]related[grow]", "[]20[]related[]" +
                notForNimbus + "related[]related[]unrelated[]"));
        setContentPane(contentPane);

        JLabel infoLabel = new JLabel("Fill in the fields with less than 10 characters.");
        infoLabel.setForeground(Color.GRAY);
        contentPane.add(infoLabel, "span 2");

        // Input fields
        contentPane.add(new JLabel("Sticker:"));
        textField1 = new JTextField();
        contentPane.add(textField1, "growx");
        contentPane.add(new JLabel("Foreground color:"));
        textField2 = new JTextField();
        contentPane.add(textField2, "growx");
        textField3 = new JTextField();
        if (!notForNimbus.isEmpty()) {
            // Changing the background color of a textfield with the Nimbus is not easy...
            contentPane.add(new JLabel("Background color:"));
            contentPane.add(textField3, "growx");
        }
        contentPane.add(new JLabel("Icon:"));
        textField4 = new JTextField();
        contentPane.add(textField4, "growx");
        contentPane.add(new JLabel("Icon with tooltip:"));
        textField5 = new JTextField();
        contentPane.add(textField5, "growx");

        // Apply button
        applyButton = new JButton("Apply");
        contentPane.add(applyButton, "skip 1, right aligned");

        // Set size
        pack();
        Dimension size = getSize();
        size.width += 100;
        setMinimumSize(size);

        // Set location
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
    }

    /**
     * Initializes the input validation and conditional logic.
     */
    private void initValidation() {
        // Create validators and collect the results
        SimpleProperty<Result> resultCollector1 = new SimpleProperty<Result>();
        createValidator(textField1, new StickerFeedback(textField1), resultCollector1);
        SimpleProperty<Result> resultCollector2 = new SimpleProperty<Result>();
        createValidator(textField2, new ColorFeedback(textField2, false), resultCollector2);
        SimpleProperty<Result> resultCollector3 = new SimpleProperty<Result>();
        createValidator(textField3, new ColorFeedback(textField3, true), resultCollector3);
        SimpleProperty<Result> resultCollector4 = new SimpleProperty<Result>();
        createValidator(textField4, new IconFeedback(textField4, false), resultCollector4);
        SimpleProperty<Result> resultCollector5 = new SimpleProperty<Result>();
        createValidator(textField5, new IconFeedback(textField5, true), resultCollector5);

        // Enable Apply button only when all fields are valid
        read(resultCollector1, resultCollector2, resultCollector3, resultCollector4, resultCollector5) //
                .transform(new CollectionElementTransformer<Result, Boolean>(new ResultToBooleanTransformer())) //
                .transform(new AndBooleanAggregator()) //
                .write(new ComponentEnabledProperty(applyButton));
    }

    private void createValidator(JTextField textField, ResultHandler<Result> resultHandler, SimpleProperty<Result>
            resultCollector) {
        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new InputFieldRule()) //
                .handleWith(resultHandler) //
                .handleWith(resultCollector) //
                .trigger();
    }

    /**
     * Entry point.
     *
     * @param args Ignored.
     */
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
                new FeedbackDemo().setVisible(true);
            }
        });
    }
}
