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

package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Before;
import org.junit.Test;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see JListSelectedItemCountProperty
 */
public class JListSelectedItemCountPropertyTest {

    private JList list;

    private ListSelectionModel selectionModel;

    private ReadableProperty<Integer> property;

    private ValueChangeListener<Integer> listenerMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Create list model
        DefaultListModel listModel = new DefaultListModel();

        listModel.addElement(1);
        listModel.addElement(2);
        listModel.addElement(3);

        // Create list
        list = new JList(listModel);
        selectionModel = list.getSelectionModel();

        // Create property
        property = new JListSelectedItemCountProperty(list);
        listenerMock = (ValueChangeListener<Integer>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);
    }

    @Test
    public void testSelectionChanges() {
        // Check initial state
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        // Test property value changes
        selectionModel.addSelectionInterval(0, 1);
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        selectionModel.addSelectionInterval(2, 2);
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        selectionModel.removeSelectionInterval(1, 1);
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        selectionModel.setSelectionInterval(1, 1);

        selectionModel.clearSelection();
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, 0, 2);
        verify(listenerMock).valueChanged(property, 2, 3);
        verify(listenerMock).valueChanged(property, 3, 2);
        verify(listenerMock).valueChanged(property, 2, 1);
        verify(listenerMock).valueChanged(property, 1, 0);
        verify(listenerMock, times(5)).valueChanged(any(JListSelectedItemCountProperty.class), anyInt(), anyInt());
    }

    @Test
    public void testNewModel() {
        // Check initial state
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        // Test property value changes
        selectionModel.setSelectionInterval(0, 1);
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        list.setModel(new DefaultListModel());
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, 0, 2);
        verify(listenerMock).valueChanged(property, 2, 0);
        verify(listenerMock, times(2)).valueChanged(any(JListSelectedItemCountProperty.class), anyInt(), anyInt());
    }

    @Test
    public void testNewSelectionModel() {
        // Check initial state
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        // Test property value changes
        selectionModel.setSelectionInterval(0, 1);
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        list.setSelectionModel(new DefaultListSelectionModel());
        assertEquals(Integer.valueOf(list.getSelectedIndices().length), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, 0, 2);
        verify(listenerMock).valueChanged(property, 2, 0);
        verify(listenerMock, times(2)).valueChanged(any(JListSelectedItemCountProperty.class), anyInt(), anyInt());
    }
}
