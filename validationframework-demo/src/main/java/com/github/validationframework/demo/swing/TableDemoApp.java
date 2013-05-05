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

import com.github.validationframework.base.resulthandler.PrintStreamResultHandler;
import com.github.validationframework.base.rule.object.NotNullBooleanRule;
import com.github.validationframework.base.rule.string.StringLengthGreaterThanOrEqualToRule;
import com.github.validationframework.base.validator.DefaultSimpleValidator;
import com.github.validationframework.swing.dataprovider.JTableComboBoxEditorSelectedValueProvider;
import com.github.validationframework.swing.dataprovider.JTableTextEditorTextProvider;
import com.github.validationframework.swing.resulthandler.bool.CellIconBooleanFeedback;
import com.github.validationframework.swing.trigger.JTableComboBoxEditorModelChangedTrigger;
import com.github.validationframework.swing.trigger.JTableTextEditorDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.AbstractAction;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.Random;

public class TableDemoApp extends JFrame {

    private class ChangeAction extends AbstractAction {

        private static final long serialVersionUID = 3324532651113452622L;

        public ChangeAction() {
            super("Replace model");
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            table.setModel(createTableModel());

            if (validator != null) {
                validator.dispose();
                validator.dispose();
            }
            installValidators(table);
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -7459554050305728899L;

    private final JButton changeButton = new JButton(new ChangeAction());

    private final JTable table = createTable();

    private DefaultSimpleValidator<String, Boolean> validator;

    /**
     * Default constructor.
     */
    public TableDemoApp() {
        super();
        init();
    }

    /**
     * Initializes the frame by creating its contents.
     */
    private void init() {
        setTitle("Validation Framework Test");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Add contents
        final JPanel contentPane = new JPanel(new MigLayout("fill, wrap 1", "[]", "[grow]unrelated[]"));
        setContentPane(contentPane);

        contentPane.add(new JScrollPane(table), "grow");
        installValidators(table);
        contentPane.add(changeButton, "align right");

        // Set size
        pack();
        final Dimension size = getSize();
        size.width += 100;
        setMinimumSize(size);

        // Set location
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
    }

    private JTable createTable() {
        final JTable table = new JTable(createTableModel());

        final JComboBox comboBoxEditorComponent = new JComboBox();
        comboBoxEditorComponent.addItem("Option 1");
        comboBoxEditorComponent.addItem("Option 2");
        comboBoxEditorComponent.addItem("Option 3");
        comboBoxEditorComponent.addItem("Option 4");
        table.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(comboBoxEditorComponent));

        return table;
    }

    private TableModel createTableModel() {
        // Create table model
        final DefaultTableModel model = new DefaultTableModel();

        // Fill table model
        model.addColumn("First column");
        model.addColumn("Second column");
        model.addColumn("Third column");
        model.addColumn("Fourth column");

        final Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < 50; i++) {
            model.addRow(new String[]{"ABCD", Integer.toString(random.nextInt()), "ZZ", "123.456"});
        }

        return model;
    }

    private void installValidators(final JTable table) {
        validator = new DefaultSimpleValidator<String, Boolean>();

        validator.addTrigger(new JTableTextEditorDocumentChangedTrigger(table, 1, 1));
        validator.addDataProvider(new JTableTextEditorTextProvider(table));
        validator.addRule(new StringLengthGreaterThanOrEqualToRule(3));
        validator.addResultHandler(new PrintStreamResultHandler<Boolean>("(1,1) => "));
        validator.addResultHandler(new CellIconBooleanFeedback(table, 1, 1,
                CellIconBooleanFeedback.DEFAULT_INVALID_ICON, "Invalid text"));

        final DefaultSimpleValidator<Object, Boolean> validator2 = new DefaultSimpleValidator<Object, Boolean>();
        validator2.addTrigger(new JTableComboBoxEditorModelChangedTrigger(table));
        validator2.addDataProvider(new JTableComboBoxEditorSelectedValueProvider<Object>(table));
        validator2.addRule(new NotNullBooleanRule<Object>());
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
                new TableDemoApp().setVisible(true);
            }
        });
    }
}
