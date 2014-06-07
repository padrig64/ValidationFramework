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

package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Test;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see ComponentLocationProperty
 */
public class ComponentLocationPropertyTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromProperty() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ReadableWritableProperty<Point, Point> property = new ComponentLocationProperty(component);
        ValueChangeListener<Point> listenerMock = (ValueChangeListener<Point>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals(new Point(0, 0), property.getValue());
        assertEquals(new Point(0, 0), component.getLocation());
        property.setValue(new Point(11, 12));
        assertEquals(new Point(11, 12), component.getLocation());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, new Point(0, 0), new Point(11, 12));
        verify(listenerMock).valueChanged(any(ComponentLocationProperty.class), any(Point.class), any(Point.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testNonNullFromComponent() {
        JFrame window = new JFrame();
        Container contentPane = new JPanel(null);
        window.setContentPane(contentPane);
        Component component = new JLabel();
        contentPane.add(component);

        ReadableWritableProperty<Point, Point> property = new ComponentLocationProperty(component);
        ValueChangeListener<Point> listenerMock = (ValueChangeListener<Point>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);

        assertEquals(new Point(0, 0), property.getValue());
        assertEquals(new Point(0, 0), component.getLocation());
        component.setLocation(new Point(13, 14));
        // Wait a little bit because the location may be applied asynchronously
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(new Point(13, 14), property.getValue());

        // Check exactly one event fired
        verify(listenerMock).valueChanged(property, new Point(0, 0), new Point(13, 14));
        verify(listenerMock).valueChanged(any(ComponentLocationProperty.class), any(Point.class), any(Point.class));
    }
}
