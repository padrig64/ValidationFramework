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

import com.google.code.validationframework.demo.swing.support.CountDecoration;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import net.miginfocom.swing.MigLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CountDecorationDemo extends JFrame {

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
        final CountDecoration countDecoration = new CountDecoration(decoratedButton, new Anchor(1.0f, -25, 0.0f, 25),
                Anchor.CENTER);
        countDecoration.getCountProperty().setValue(1);

        decoratedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int newCount = countDecoration.getCountProperty().getValue() + 11;
                if (newCount >= 112) {
                    newCount = 0;
                }
                countDecoration.getCountProperty().setValue(newCount);
            }
        });

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
