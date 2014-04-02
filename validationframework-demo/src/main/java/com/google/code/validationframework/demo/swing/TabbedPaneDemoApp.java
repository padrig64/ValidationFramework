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

import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.base.resulthandler.ResultCollector;
import com.google.code.validationframework.base.rule.bool.AndBooleanRule;
import com.google.code.validationframework.base.rule.string.StringNotEmptyRule;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.property.ComponentEnabledProperty;
import com.google.code.validationframework.swing.property.JToggleButtonSelectedProperty;
import com.google.code.validationframework.swing.resulthandler.bool.ComponentEnablingBooleanResultHandler;
import com.google.code.validationframework.swing.resulthandler.bool.IconBooleanFeedback;
import com.google.code.validationframework.swing.resulthandler.bool.TabIconBooleanFeedback;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.HashSet;
import java.util.Set;

import static com.google.code.validationframework.base.binding.Binder.read;
import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.collect;
import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

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
        JPanel contentPane = new JPanel(new MigLayout("fill, wrap 1", "[]", "[]related[]unrelated[]unrelated[]"));
        setContentPane(contentPane);

        // Checkbox to enable the tabbed pane
        JCheckBox tabbedPaneEnabledCheckBox = new JCheckBox("Enable tabbed pane");
        tabbedPaneEnabledCheckBox.setSelected(true);
        contentPane.add(tabbedPaneEnabledCheckBox);
        JCheckBox firstTabEnabledCheckBox = new JCheckBox("Enable first tab");
        firstTabEnabledCheckBox.setSelected(false);
        contentPane.add(firstTabEnabledCheckBox);

        // Tabbed pane
        final JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, "grow");

        // Create tabs
        Set<ResultCollector<?, Boolean>> tabResultCollectors = new HashSet<ResultCollector<?, Boolean>>();
        for (int i = 0; i < 3; i++) {
            Set<ResultCollector<?, Boolean>> fieldResultCollectors = new HashSet<ResultCollector<?, Boolean>>();
            tabbedPane.add("Tab " + i, createTabContent(fieldResultCollectors));
            installTabValidator(tabbedPane, i, fieldResultCollectors, tabResultCollectors);
            tabbedPane.setTitleAt(i, "Tab");
        }

        // Enable tabbed pane only if checkbox is selected
        read(new JToggleButtonSelectedProperty(tabbedPaneEnabledCheckBox)) //
                .write(new ComponentEnabledProperty(tabbedPane));

        // Enable first tab only if checkbox is selected
        read(new JToggleButtonSelectedProperty(firstTabEnabledCheckBox)) //
                .write(new WritableProperty<Boolean>() {
                    @Override
                    public void setValue(Boolean value) {
                        if (tabbedPane.getTabCount() > 0) {
                            tabbedPane.setEnabledAt(0, (value != null) && value);
                        }
                    }
                });

        // Install global validator
        installGlobalValidator(tabResultCollectors, applyButton);

        // Apply button
        contentPane.add(applyButton, "align right");

        // Set size
        pack();
        Dimension size = getSize();
        size.width += 100;
        setMinimumSize(size);

        // Set location
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
    }

    private Component createTabContent(Set<ResultCollector<?, Boolean>> fieldResultCollectors) {
        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill, wrap 2", "[][grow, fill]"));

        for (int i = 0; i < 2; i++) {
            panel.add(new JLabel("Field " + (i + 1) + ":"));

            // Create formatted textfield
            JTextField field = new JTextField();
            panel.add(field);
            field.setColumns(10);
            field.setText("Value");

            // Create field validator
            installFieldValidator(field, fieldResultCollectors);
        }

        return panel;
    }

    private void installFieldValidator(JTextField field, Set<ResultCollector<?, Boolean>> fieldResultCollectors) {
        // Create result collector for the validator of the whole tab
        ResultCollector<Boolean, Boolean> fieldResultCollector = new ResultCollector<Boolean, Boolean>();
        fieldResultCollectors.add(fieldResultCollector);

        on(new JTextFieldDocumentChangedTrigger(field)) //
                .read(new JTextFieldTextProvider(field)) //
                .check(new StringNotEmptyRule()) //
                .handleWith(new IconBooleanFeedback(field)) //
                .handleWith(fieldResultCollector) //
                .getValidator().trigger();
    }

    private void installTabValidator(JTabbedPane tabbedPane, int i, Set<ResultCollector<?,
            Boolean>> fieldResultCollectors, Set<ResultCollector<?, Boolean>> tabResultCollectors) {
        // Create result collector for the global validator
        ResultCollector<Boolean, Boolean> tabResultCollector = new ResultCollector<Boolean, Boolean>();
        tabResultCollectors.add(tabResultCollector);

        // Create validator for the whole tab
        collect(fieldResultCollectors) //
                .check(new AndBooleanRule()) //
                .handleWith(new TabIconBooleanFeedback(tabbedPane, i, "This tab contains errors.")) //
                .handleWith(tabResultCollector) //
                .getValidator().trigger();
    }

    private void installGlobalValidator(Set<ResultCollector<?, Boolean>> tabResultCollectors, Component... buttons) {
        collect(tabResultCollectors) //
                .check(new AndBooleanRule()) //
                .handleWith(new ComponentEnablingBooleanResultHandler(buttons)) //
                .getValidator().trigger();
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
                new TabbedPaneDemoApp().setVisible(true);
            }
        });
    }
}
