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

import com.google.code.validationframework.base.rule.string.StringLengthLessThanOrEqualToRule;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.resulthandler.bool.IconBooleanFeedback;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

public class IconComponentDecorationInCardLayoutDemoApp extends JFrame implements ItemListener {

    private static class Card extends JPanel {

        private static final long serialVersionUID = -7961649303239986607L;
        private static int count = 0;

        public Card() {
            super();
            init();
        }

        private void init() {
            setLayout(new FlowLayout());

            add(new JLabel("Field: "));

            final JTextField textField = new JTextField();
            textField.setColumns(8);
            textField.setName("Textfield " + count);
            add(textField);

            installValidator(textField);
            count++;
        }

        private void installValidator(final JTextField textField) {
            on(new JTextFieldDocumentChangedTrigger(textField)) //
                    .read(new JTextFieldTextProvider(textField)) //
                    .check(new StringLengthLessThanOrEqualToRule(5 + count)) //
                    .handleWith(new IconBooleanFeedback(textField, "Should be less then " + (6 + count) + " " +
                            "characters"));
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -7459554050305728899L;

    private final JButton applyButton = new JButton("Apply");

    private JPanel cardContainer = null;

    /**
     * Default constructor.
     */
    public IconComponentDecorationInCardLayoutDemoApp() {
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
        final JPanel contentPane = new JPanel(new MigLayout("fill, wrap 1", "[]", "[grow]unrelated[]"));
        setContentPane(contentPane);

        // Combobox
        final JComboBox comboBox = new JComboBox();
        contentPane.add(comboBox, "grow");
        comboBox.addItem("First");
        comboBox.addItem("Second");
        comboBox.addItem("Third");
        comboBox.addItemListener(this);

        // Panels
        cardContainer = new JPanel(new CardLayout());
        cardContainer.setBorder(new EmptyBorder(30, 30, 30, 30));
        cardContainer.add(new Card(), "First");
        cardContainer.add(new Card(), "Second");
        cardContainer.add(new Card(), "Third");
        contentPane.add(new JScrollPane(cardContainer));

        // Apply button
        contentPane.add(applyButton, "align right");

        // Set size
        pack();
        final Dimension size = getSize();
//		size.width += 100;
//		setMinimumSize(size);

        // Set location
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
    }

    @Override
    public void itemStateChanged(final ItemEvent evt) {
        final CardLayout cl = (CardLayout) (cardContainer.getLayout());
        cl.show(cardContainer, (String) evt.getItem());
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
                new IconComponentDecorationInCardLayoutDemoApp().setVisible(true);
            }
        });
    }
}
