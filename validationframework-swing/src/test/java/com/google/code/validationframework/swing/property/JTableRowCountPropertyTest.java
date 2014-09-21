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

import com.google.code.validationframework.api.property.ReadableProperty;
import org.junit.Before;
import org.junit.Test;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import static org.junit.Assert.assertEquals;

/**
 * @see JTableRowCountProperty
 */
public class JTableRowCountPropertyTest {

    private DefaultTableModel tableModel = null;

    private JTable table = null;

    @Before
    public void setUp() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("1");
        tableModel.addColumn("2");
        tableModel.addColumn("3");

        table = new JTable(tableModel);
    }

    @Test
    public void testRowsInserted() {
        ReadableProperty<Integer> property = new JTableRowCountProperty(table);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.addRow(new Object[]{1, 2, 3});
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.setRowCount(3);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());
    }

    @Test
    public void testRowsRemoved() {
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});

        ReadableProperty<Integer> property = new JTableRowCountProperty(table);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.removeRow(0);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.setRowCount(1);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());
    }

    @Test
    public void testRowsUpdated() {
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});

        ReadableProperty<Integer> property = new JTableRowCountProperty(table);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.setValueAt(5, 0, 0);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.moveRow(0, 0, 1);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());
    }

    @Test
    public void testStructureChanged() {
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});

        ReadableProperty<Integer> property = new JTableRowCountProperty(table);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.addColumn("New column");
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        Object[][] data = new Object[][]{{1, 2, 3}, {1, 2, 3}, {1, 2, 3}};
        Object[] identifiers = new Object[]{1, 2, 3};
        tableModel.setDataVector(data, identifiers);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.setColumnCount(2);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());
    }

    @Test
    public void testDataChanged() {
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});
        tableModel.addRow(new Object[]{1, 2, 3});

        ReadableProperty<Integer> property = new JTableRowCountProperty(table);
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());

        tableModel.getDataVector().clear();
        tableModel.fireTableDataChanged();
        assertEquals(Integer.valueOf(table.getRowCount()), property.getValue());
    }
}
