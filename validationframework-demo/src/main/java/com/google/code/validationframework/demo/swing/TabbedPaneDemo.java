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

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.base.property.CompositeReadableProperty;
import com.google.code.validationframework.base.property.ResultHandlerProperty;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import com.google.code.validationframework.base.rule.string.StringNotEmptyRule;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.property.ComponentEnabledProperty;
import com.google.code.validationframework.swing.property.JToggleButtonSelectedProperty;
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
import java.util.Collection;

import static com.google.code.validationframework.base.binding.Binder.read;
import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

/**
 * Example shows:
 * . how validation can be composited inside window;
 * . how validation can be added to single components;
 * . how tabbed panes can be decorated depending on the validation result;
 * . how properties can be used for conditional logic.
 */
public class TabbedPaneDemo extends JFrame {

    /**
     * Generated serial UID.
     */
    final JButton applyButton = new JButton("Apply");

    private static final long serialVersionUID = 5788691760205320130L;

    /**
     * Default constructor.
     */
    public TabbedPaneDemo() {
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
        CompositeReadableProperty<Boolean> tabbedPaneResultsProperty = new CompositeReadableProperty<Boolean>();
        for (int i = 0; i < 3; i++) {
            CompositeReadableProperty<Boolean> tabResultsProperty = new CompositeReadableProperty<Boolean>();
            tabbedPane.add("Tab " + i, createTabContent(tabResultsProperty));
            tabbedPaneResultsProperty.addProperty(installTabValidation(tabbedPane, i, tabResultsProperty));
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
        installGlobalValidation(tabbedPaneResultsProperty, applyButton);

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

    /**
     * Creates some content to put in a tab in the tabbed pane.
     *
     * @param tabResultsProperty Composite property to put the tab-wise result into.
     *
     * @return Component representing the tab content and to be added to the tabbed pane.
     */
    private Component createTabContent(CompositeReadableProperty<Boolean> tabResultsProperty) {
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
            tabResultsProperty.addProperty(installFieldValidation(field));
        }

        return panel;
    }

    /**
     * Sets up a validator for the specified field.
     *
     * @param field Field on which a validator should be installed.
     *
     * @return Property representing the result of the field validation and that can be used for tab-wise validation.
     */
    private ReadableProperty<Boolean> installFieldValidation(JTextField field) {
        // FieLd is valid if not empty
        SimpleBooleanProperty fieldResult = new SimpleBooleanProperty();
        on(new JTextFieldDocumentChangedTrigger(field)) //
                .read(new JTextFieldTextProvider(field)) //
                .check(new StringNotEmptyRule()) //
                .handleWith(new IconBooleanFeedback(field)) //
                .handleWith(fieldResult) //
                .getValidator().trigger();

        // Tab will be valid only if all fields are valid
        return fieldResult;
    }

    /**
     * Sets up the tab-wise validation.
     * <p>
     * A tab is considered valid only if all of its fields are valid.
     *
     * @param tabbedPane         Tabbed pane holding the tab on which the validation should be installed.
     * @param i                  Index of the tab in the tabbed pane.
     * @param tabResultsProperty Results of the individual fields inside the tab.
     *
     * @return Property representing the result of the tab-wise validation and that can be used for tabbed pane-wise
     * validation.
     */
    private ReadableProperty<Boolean> installTabValidation(JTabbedPane tabbedPane, int i,
                                                           ReadableProperty<Collection<Boolean>> tabResultsProperty) {
        // Tab is valid only if all fields are valid
        SimpleBooleanProperty tabResultProperty = new SimpleBooleanProperty();
        read(tabResultsProperty).transform(new AndBooleanAggregator()).write(tabResultProperty);

        // Handle tab-wise result
        read(tabResultProperty).write(new ResultHandlerProperty<Boolean>(new TabIconBooleanFeedback(tabbedPane, i,
                "This tab contains errors.")));

        // Tabbed pane will be valid only if all tabs are valid
        return tabResultProperty;
    }

    /**
     * Sets up the tabbed-pane wise (global validation).
     * <p>
     * The tabbed pane is considered valid only if all of its tabs are valid.
     *
     * @param tabbedPaneResultsProperty Results of the individual tabs inside the tabbed pane.
     * @param button                    Component to be enabled/disabled depending on the global result.
     */
    private void installGlobalValidation(ReadableProperty<Collection<Boolean>> tabbedPaneResultsProperty,
                                         Component button) {
        read(tabbedPaneResultsProperty) //
                .transform(new AndBooleanAggregator()) //
                .write(new ComponentEnabledProperty(button));
    }

    /**
     * Main.
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
                new TabbedPaneDemo().setVisible(true);
            }
        });
    }
}
