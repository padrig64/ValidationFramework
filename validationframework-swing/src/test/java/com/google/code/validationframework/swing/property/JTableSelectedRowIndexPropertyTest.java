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

import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import org.junit.Before;
import org.junit.Test;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @see JTableSelectedRowIndexProperty
 */
public class JTableSelectedRowIndexPropertyTest {

    private JTable table;

    private ListSelectionModel selectionModel;

    private ReadableWritableProperty<Integer, Integer> property;

    private ValueChangeListener<Integer> listenerMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();

        tableModel.addColumn("A");
        tableModel.addColumn("B");
        tableModel.addColumn("C");

        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{4, 5, 6});
        tableModel.addRow(new Object[]{7, 8, 9});

        // Create table
        table = new JTable(tableModel);
        selectionModel = table.getSelectionModel();

        // Create property
        property = new JTableSelectedRowIndexProperty(table);
        listenerMock = (ValueChangeListener<Integer>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);
    }

    @Test
    public void testSelectionChanges() {
        // Check initial state
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Test property value changes
        selectionModel.addSelectionInterval(0, 0);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        selectionModel.setSelectionInterval(2, 2);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        selectionModel.removeSelectionInterval(2, 2);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        selectionModel.setSelectionInterval(1, 1);

        selectionModel.clearSelection();
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, -1, 0);
        verify(listenerMock).valueChanged(property, 0, 2);
        verify(listenerMock).valueChanged(property, 2, -1);
        verify(listenerMock).valueChanged(property, -1, 1);
        verify(listenerMock).valueChanged(property, 1, -1);
        verify(listenerMock, times(5)).valueChanged(any(JTableSelectedRowIndexProperty.class), anyInt(), anyInt());
    }

    @Test
    public void testNewModel() {
        // Check initial state
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Test property value changes
        selectionModel.setSelectionInterval(1, 1);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        table.setModel(new DefaultTableModel());
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, -1, 1);
        verify(listenerMock).valueChanged(property, 1, -1);
        verify(listenerMock, times(2)).valueChanged(any(JTableSelectedRowIndexProperty.class), anyInt(), anyInt());
    }

    @Test
    public void testNewSelectionModel() {
        // Check initial state
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Test property value changes
        selectionModel.setSelectionInterval(1, 1);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        table.setSelectionModel(new DefaultListSelectionModel());
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, -1, 1);
        verify(listenerMock).valueChanged(property, 1, -1);
        verify(listenerMock, times(2)).valueChanged(any(JTableSelectedRowIndexProperty.class), anyInt(), anyInt());
    }

    @Test
    public void testSelectFromProperty() {
        // Check initial state
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Test selection model changes
        property.setValue(1);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        property.setValue(-1);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        property.setValue(2);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        property.setValue(null);
        assertEquals(Integer.valueOf(table.getSelectedRow()), property.getValue());

        // Check fired events
        verify(listenerMock).valueChanged(property, -1, 1);
        verify(listenerMock).valueChanged(property, 1, -1);
        verify(listenerMock).valueChanged(property, -1, 2);
        verify(listenerMock).valueChanged(property, 2, -1);
        verify(listenerMock, times(4)).valueChanged(any(JTableSelectedRowIndexProperty.class), anyInt(), anyInt());
    }
}
