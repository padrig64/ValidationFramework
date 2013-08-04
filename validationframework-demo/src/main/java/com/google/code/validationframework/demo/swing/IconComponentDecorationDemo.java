package com.google.code.validationframework.demo.swing;

import com.google.code.validationframework.base.rule.string.StringNotEmptyRule;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.decoration.IconComponentDecoration;
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import com.google.code.validationframework.swing.resulthandler.bool.IconBooleanFeedback;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import net.miginfocom.swing.MigLayout;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

public class IconComponentDecorationDemo extends JFrame {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -2561750109645186441L;

    /**
     * Default constructor.
     */
    public IconComponentDecorationDemo() {
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
        JPanel contentPane = new JPanel(new MigLayout("fill, wrap 1"));
        setContentPane(contentPane);

        // Tabbed pane
        JTabbedPane tabbedPane = new JTabbedPane();
        contentPane.add(tabbedPane, "grow");

        // Create tabs
        tabbedPane.add("Constant info", createTabConstantInfo());
        tabbedPane.add("Disabled", createTabDisabled());
        tabbedPane.add("Single validation", createTabSingleCompWithValidation());
        tabbedPane.add("Split pane", createTabSplitPane());
        tabbedPane.add("Scroll pane", createTabScrollPane());
        tabbedPane.add("Small panels", createTabSmallPanels());

        // Set size
        Dimension size = new Dimension(640, 480);
        setSize(size);

        // Set location
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation((screenSize.width - size.width) / 2, (screenSize.height - size.height) / 3);
    }

    private Component createTabConstantInfo() {
        JPanel panel = new JPanel(new MigLayout("fill"));

        JTextField textField = new JTextField("Not empty");
        textField.setColumns(15);
        panel.add(textField);

        IconComponentDecoration decoration = new IconComponentDecoration(textField);
        ImageIcon icon = IconUtils.loadImageIcon("/images/defaults/info.png", IconComponentDecoration.class);
        decoration.setIcon(icon);
        decoration.setToolTipText("Tooltip");

        return panel;
    }

    private Component createTabDisabled() {
        JPanel panel = new JPanel(new MigLayout("fill, wrap 1"));

        final JTextField textField = new JTextField();
        final JCheckBox checkBox = new JCheckBox("Enabled");
        checkBox.setSelected(true);
        panel.add(checkBox);
        checkBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                textField.setEnabled(checkBox.isSelected());
            }
        });

        panel.add(textField);
        textField.setColumns(15);

        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new StringNotEmptyRule()) //
                .handleWith(new IconBooleanFeedback(textField, "Cannot be empty")) //
                .trigger();

        return panel;
    }

    private Component createTabSingleCompWithValidation() {
        JPanel panel = new JPanel(new MigLayout("fill"));

        JTextField textField = new JTextField();
        textField.setName("tab1");
        textField.setColumns(15);
        panel.add(textField);

        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new StringNotEmptyRule()) //
                .handleWith(new IconBooleanFeedback(textField, "Cannot be empty")) //
                .trigger();

        return panel;
    }


    private Component createTabSplitPane() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);

        JPanel panel = new JPanel(new MigLayout("fill"));
        splitPane.setTopComponent(panel);
        JTextField textField = new JTextField();
        textField.setColumns(15);
        panel.add(textField);

        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new StringNotEmptyRule()) //
                .handleWith(new IconBooleanFeedback(textField)) //
                .trigger();

        panel = new JPanel(new MigLayout("fill"));
        splitPane.setBottomComponent(panel);
        textField = new JTextField();
        textField.setColumns(15);
        panel.add(textField);

        on(new JTextFieldDocumentChangedTrigger(textField)) //
                .read(new JTextFieldTextProvider(textField)) //
                .check(new StringNotEmptyRule()) //
                .handleWith(new IconBooleanFeedback(textField)) //
                .trigger();

        return splitPane;
    }

    private Component createTabScrollPane() {
        JPanel panel = new JPanel(new MigLayout("fill, wrap 1"));

        for (int i = 0; i < 3; i++) {
            JTextField textField = new JTextField();
            textField.setColumns(15);
            panel.add(textField);

            on(new JTextFieldDocumentChangedTrigger(textField)) //
                    .read(new JTextFieldTextProvider(textField)) //
                    .check(new StringNotEmptyRule()) //
                    .handleWith(new IconBooleanFeedback(textField, "Field " + i + " cannot be empty")) //
                    .trigger();
        }

        return new JScrollPane(panel);
    }

    private Component createTabSmallPanels() {
        JPanel panel = new JPanel(new MigLayout("fill, wrap 1", "[fill]", "[fill]0[fill]"));

        for (int i = 0; i < 3; i++) {
            JPanel smallPanel = new JPanel(new MigLayout("insets 0, fill", "[fill]", "[fill]"));
            panel.add(smallPanel);
            smallPanel.setBorder(new LineBorder(Color.YELLOW));

            JTextField textField = new JTextField();
            textField.setColumns(15);
            smallPanel.add(textField);

            on(new JTextFieldDocumentChangedTrigger(textField)) //
                    .read(new JTextFieldTextProvider(textField)) //
                    .check(new StringNotEmptyRule()) //
                    .handleWith(new IconBooleanFeedback(textField, "Field " + i + " cannot be empty")) //
                    .trigger();
        }

        return new JScrollPane(panel);
    }

    public static void main(String... args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
//                // Set look-and-feel
//                try {
//                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//                        if ("Nimbus".equals(info.getName())) {
//                            UIManager.setLookAndFeel(info.getClassName());
//                            break;
//                        }
//                    }
//                } catch (UnsupportedLookAndFeelException e) {
//                    // handle exception
//                } catch (ClassNotFoundException e) {
//                    // handle exception
//                } catch (InstantiationException e) {
//                    // handle exception
//                } catch (IllegalAccessException e) {
//                    // handle exception
//                }
                try {
                    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (UnsupportedLookAndFeelException e) {
                    e.printStackTrace();
                }

                // Show window
                new IconComponentDecorationDemo().setVisible(true);
            }
        });
    }
}
