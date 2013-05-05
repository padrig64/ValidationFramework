/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.github.validationframework.demo.swing;

import com.github.validationframework.base.resulthandler.ResultCollector;
import com.github.validationframework.base.rule.bool.AndBooleanRule;
import com.github.validationframework.swing.dataprovider.JFormattedTextFieldTextProvider;
import com.github.validationframework.swing.resulthandler.bool.ComponentEnablingBooleanResultHandler;
import com.github.validationframework.swing.resulthandler.bool.IconBooleanFeedback;
import com.github.validationframework.swing.resulthandler.bool.TabIconBooleanFeedback;
import com.github.validationframework.swing.rule.JFormattedTextFieldFormatterRule;
import com.github.validationframework.swing.trigger.JFormattedTextFieldDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.text.NumberFormatter;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Set;

import static com.github.validationframework.experimental.builder.ResultCollectorValidatorBuilder.collect;
import static com.github.validationframework.experimental.builder.SimpleValidatorBuilder.on;

public class TabbedPaneDemoApp extends JFrame {

    /**
     * Generated serial UID.
     */
    final JButton applyButton = new JButton("Apply");

    private static final long serialVersionUID = 5788691760205320130L;

    /**
     * Default constructor.
     */
    public TabbedPaneDemoApp() {
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
        final JPanel contentPane = new JPanel(new MigLayout("fill, wrap 1", "[]", "[]unrelated[]"));
        setContentPane(contentPane);

        // Tabbed pane
        final JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, "grow");

        // Create tabs
        final Set<ResultCollector<?, Boolean>> tabResultCollectors = new HashSet<ResultCollector<?, Boolean>>();
        for (int i = 0; i < 3; i++) {
            final Set<ResultCollector<?, Boolean>> fieldResultCollectors = new HashSet<ResultCollector<?, Boolean>>();
            tabbedPane.add("Tab " + i, createTabContent(fieldResultCollectors));
            installTabValidator(tabbedPane, i, fieldResultCollectors, tabResultCollectors);
            tabbedPane.setTitleAt(i, "Tab");
        }

        // Install global validator
        installGlobalValidator(tabResultCollectors, applyButton);

        // Apply button
        contentPane.add(applyButton, "align right");

        // Set size
        pack();
        final Dimension size = getSize();
        size.width += 100;
        setMinimumSize(size);

        // Set location
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
    }

    private Component createTabContent(final Set<ResultCollector<?, Boolean>> fieldResultCollectors) {
        final JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, wrap 2", "[][grow, fill]"));

        panel.add(new JLabel("Choice:"));
        final JComboBox comboBox = new JComboBox(new String[]{"Option 1", "Option 2", "Option 3", "Option 4",
                "Option 5"});
        panel.add(comboBox);

        for (int i = 0; i < 4; i++) {
            panel.add(new JLabel("Field " + (i + 1) + ":"));

            // Create formatter for the formatted textfield
            final NumberFormatter formatter = new NumberFormatter();
            formatter.setMinimum(0.0);
            formatter.setMinimum(30.0);
            final DecimalFormat format = new DecimalFormat("0.0");
            formatter.setFormat(format);

            // Create formatted textfield
            final JFormattedTextField field = new JFormattedTextField(formatter);
            panel.add(field);
            field.setColumns(10);
            field.setFocusLostBehavior(JFormattedTextField.COMMIT);

            // Create field validator
            installFieldValidator(field, fieldResultCollectors);
        }

        return panel;
    }

    private void installFieldValidator(final JFormattedTextField field, final Set<ResultCollector<?,
            Boolean>> fieldResultCollectors) {
        // Create result collector for the validator of the whole tab
        final ResultCollector<Boolean, Boolean> fieldResultCollector = new ResultCollector<Boolean, Boolean>();
        fieldResultCollectors.add(fieldResultCollector);

        on(new JFormattedTextFieldDocumentChangedTrigger(field)).read(new JFormattedTextFieldTextProvider(field))
                .check(new JFormattedTextFieldFormatterRule(field)).handleWith(new IconBooleanFeedback(field))
                .handleWith(fieldResultCollector).build();
    }

    private void installTabValidator(final JTabbedPane tabbedPane, final int i, final Set<ResultCollector<?,
            Boolean>> fieldResultCollectors, final Set<ResultCollector<?, Boolean>> tabResultCollectors) {
        // Create result collector for the global validator
        final ResultCollector<Boolean, Boolean> tabResultCollector = new ResultCollector<Boolean, Boolean>();
        tabResultCollectors.add(tabResultCollector);

        // Create validator for the whole tab
        collect(fieldResultCollectors).check(new AndBooleanRule()).handleWith(new TabIconBooleanFeedback(tabbedPane,
                i, null, null, TabIconBooleanFeedback.DEFAULT_INVALID_ICON, "One or several invalid input fields"))
                .handleWith(tabResultCollector).build();
    }

    private void installGlobalValidator(final Set<ResultCollector<?, Boolean>> tabResultCollectors,
                                        final Component... buttons) {
        collect(tabResultCollectors).check(new AndBooleanRule()).handleWith(new ComponentEnablingBooleanResultHandler
                (buttons)).build();
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
                new TabbedPaneDemoApp().setVisible(true);
            }
        });
    }
}
