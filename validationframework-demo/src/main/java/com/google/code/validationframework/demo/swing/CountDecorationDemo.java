package com.google.code.validationframework.demo.swing;

import com.google.code.validationframework.swing.decoration.AbstractComponentDecoration;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;

public class CountDecorationDemo extends JFrame {

    /**
     * Example of implementation of a component decoration showing a number of something (e.g. messages).
     */
    private static class CountDecoration extends AbstractComponentDecoration {

        private int width = 0;

        private int height = 0;

        private int count = 0;

        public CountDecoration(JComponent decoratedComponent) {
            super(decoratedComponent, new AnchorLink(new Anchor(1.0f, -25, 0.0f, 25), Anchor.CENTER));
        }

        @Override
        protected int getWidth() {
            return width;
        }

        @Override
        protected int getHeight() {
            return height;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
            updateSize();
        }

        private void updateSize() {
            String text = Integer.toString(count);

            Font font = getDecoratedComponent().getFont().deriveFont(Font.BOLD);
            width = getDecoratedComponent().getFontMetrics(font).stringWidth(text) + 10;
            height = getDecoratedComponent().getFontMetrics(font).getAscent() + 5;
        }

        @Override
        public void paint(Graphics graphics) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(new Color(20, 20, 20, 100));
            g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

            g.setColor(new Color(235, 235, 235));
            g.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 10, 10);

            g.setColor(new Color(255, 89, 71));
            g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);

            g.setColor(Color.WHITE);
            g.setFont(getDecoratedComponent().getFont().deriveFont(Font.BOLD));
            g.drawString(Integer.toString(count), 5, getHeight() - 4);

            g.dispose();
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -8327067408222994890L;

    public CountDecorationDemo() {
        super();

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Create content pane and button to be decorated
        JPanel contentPane = new JPanel(new MigLayout("fill, insets 40", "[center]", "[center]"));
        setContentPane(contentPane);

        JButton decoratedButton = new JButton("Messages", IconUtils.loadImageIcon("/images/mails64.png",
                CountDecoration.class));
        decoratedButton.setVerticalTextPosition(SwingConstants.BOTTOM);
        decoratedButton.setHorizontalTextPosition(SwingConstants.CENTER);
        contentPane.add(decoratedButton);

        // Decorate button
        CountDecoration countDecoration = new CountDecoration(decoratedButton);
        countDecoration.setCount(10);

        // Set size
        pack();
        Dimension size = getSize();
        setMinimumSize(size);
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
                CountDecorationDemo frame = new CountDecorationDemo();
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                frame.setLocation((screenSize.width - frame.getWidth()) / 2, (screenSize.height - frame.getHeight())
                        / 3);
                frame.setVisible(true);
            }
        });
    }
}
