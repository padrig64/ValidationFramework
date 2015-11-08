/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.event.FocusEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see ComponentFocusedProperty
 */
public class ComponentFocusedPropertyTest {

    private JFrame frame;

    private JButton otherButton;

    private JButton buttonUnderTest;

    private ComponentFocusedProperty propertyUnderTest;

    private ValueChangeListener<Boolean> listener;

    private CountDownLatch focusLatch;

    @Test
    public void testDispose() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame = new JFrame();
                JPanel contentPane = new JPanel(new BorderLayout());

                otherButton = new TestButton("Other button");

                buttonUnderTest = new TestButton("Button under test");
                propertyUnderTest = new ComponentFocusedProperty(buttonUnderTest);
                listener = mock(ValueChangeListener.class);
                propertyUnderTest.addValueChangeListener(listener);

                contentPane.add(otherButton, BorderLayout.NORTH);
                contentPane.add(buttonUnderTest, BorderLayout.CENTER);
                frame.setContentPane(contentPane);
                frame.pack();
                frame.setVisible(true);
            }
        });

        focus(buttonUnderTest);
        focus(otherButton);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                propertyUnderTest.dispose();
            }
        });

        focus(buttonUnderTest);
        focus(otherButton);
        focus(buttonUnderTest);
        focus(otherButton);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                propertyUnderTest.dispose();
                propertyUnderTest.dispose();
            }
        });

        verify(listener).valueChanged(propertyUnderTest, false, true);
        verify(listener).valueChanged(propertyUnderTest, true, false);
        verifyNoMoreInteractions(listener);

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                frame.dispose();
            }
        });
    }

    private void focus(final JComponent component) throws InvocationTargetException, InterruptedException {
        focusLatch = new CountDownLatch(2);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                component.requestFocus();
            }
        });
        focusLatch.await(5, TimeUnit.SECONDS);
        focusLatch = null;
    }

    private class TestButton extends JButton {

        public TestButton(String text) {
            super(text);
        }

        @Override
        protected void processFocusEvent(FocusEvent e) {
            super.processFocusEvent(e);
            if (focusLatch != null) {
                focusLatch.countDown();
            }
        }
    }
}
