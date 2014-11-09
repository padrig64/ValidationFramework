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
import com.google.code.validationframework.base.property.PrintStreamValueChangeAdapter;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * @see JTableSelectedRowIndicesProperty
 */
public class JTableSelectedRowIndicesPropertyTest {

    private JTable table;

    private ListSelectionModel selectionModel;

    private ReadableWritableProperty<List<Integer>, List<Integer>> property;

    private ValueChangeListener<List<Integer>> listenerMock;

    private void assertListEquals(List<Integer> expected, List<Integer> actual) {
        assertEquals(expected.size(), actual.size());

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), actual.get(i));
        }
    }

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
        property = new JTableSelectedRowIndicesProperty(table);
        listenerMock = (ValueChangeListener<List<Integer>>) mock(ValueChangeListener.class);
        property.addValueChangeListener(listenerMock);
        property.addValueChangeListener(new PrintStreamValueChangeAdapter<List<Integer>>("SELECTION"));
    }

    @Test
    public void testSelectionChanges() {
        // Check initial state
        verifyPropertyValue();

        // Test property value changes
        selectionModel.addSelectionInterval(0, 1);
        verifyPropertyValue();

        selectionModel.addSelectionInterval(2, 2);
        verifyPropertyValue();

        selectionModel.removeSelectionInterval(1, 1);
        verifyPropertyValue();

        selectionModel.setSelectionInterval(1, 1);

        selectionModel.clearSelection();
        verifyPropertyValue();

        // Check fired events
        verify(listenerMock).valueChanged(eq(property), //
                argThat(new CollectionMatcher<Integer>(Collections.<Integer>emptyList())), //
                argThat(new CollectionMatcher<Integer>(Arrays.asList(0, 1))));
//        verify(listenerMock).valueChanged(property, 2, 3);
//        verify(listenerMock).valueChanged(property, 3, 2);
//        verify(listenerMock).valueChanged(property, 2, 1);
//        verify(listenerMock).valueChanged(property, 1, 0);
//        verify(listenerMock, times(5)).valueChanged(any(JTableSelectedRowIndicesProperty.class), anyInt(), anyInt());
    }

    private void verifyPropertyValue() {
        int[] selectedRows = table.getSelectedRows();
        assertEquals(selectedRows.length, property.getValue().size());
        for (int i = 0; i < selectedRows.length; i++) {
            assertEquals(Integer.valueOf(selectedRows[i]), property.getValue().get(i));
        }
    }

//    @Test
//    public void testNewModel() {
//        // Check initial state
//        assertEquals(Integer.valueOf(table.getSelectedRowCount()), property.getValue());
//
//        // Test property value changes
//        selectionModel.setSelectionInterval(0, 1);
//        assertEquals(Integer.valueOf(table.getSelectedRowCount()), property.getValue());
//
//        table.setModel(new DefaultTableModel());
//        assertEquals(Integer.valueOf(table.getSelectedRowCount()), property.getValue());
//
//        // Check fired events
//        verify(listenerMock).valueChanged(property, 0, 2);
//        verify(listenerMock).valueChanged(property, 2, 0);
//        verify(listenerMock, times(2)).valueChanged(any(JTableSelectedRowIndicesProperty.class), anyInt(), anyInt());
//    }

//    @Test
//    public void testNewSelectionModel() {
//        // Check initial state
//        assertEquals(Integer.valueOf(table.getSelectedRowCount()), property.getValue());
//
//        // Test property value changes
//        selectionModel.setSelectionInterval(0, 1);
//        assertEquals(Integer.valueOf(table.getSelectedRowCount()), property.getValue());
//
//        table.setSelectionModel(new DefaultListSelectionModel());
//        assertEquals(Integer.valueOf(table.getSelectedRowCount()), property.getValue());
//
//        // Check fired events
//        verify(listenerMock).valueChanged(property, 0, 2);
//        verify(listenerMock).valueChanged(property, 2, 0);
//        verify(listenerMock, times(2)).valueChanged(any(JTableSelectedRowIndicesProperty.class), anyInt(), anyInt());
//    }

    private static class CollectionMatcher<T> extends ArgumentMatcher<List<T>> {

        private final List<T> refElements;

        public CollectionMatcher(List<T> refElements) {
            super();
            this.refElements = new ArrayList<T>(refElements);
        }

        @SuppressWarnings("unchecked")
        @Override
        public boolean matches(Object actualElements) {
            boolean match = false;

            if (actualElements instanceof Collection<?>) {
                match = haveEqualElements(refElements, (Collection<T>) actualElements);
            }

            return match;
        }

        public void describeTo(Description description) {
            // Do nothing
        }
    }

    private static <T> boolean haveEqualElements(Collection<T> first, Collection<T> second) {
        boolean match = false;

        // First, check size
        if (first.size() == second.size()) {

            // Then, check each element
            match = true;
            List<T> firstList = new ArrayList<T>(first);
            List<T> secondList = new ArrayList<T>(second);
            for (int i = 0; (i < first.size()) && match; i++) {
                match = firstList.get(i).equals(secondList.get(i));
            }
        }

        return match;
    }
}
