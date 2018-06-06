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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableWritableProperty;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Property representing the index of the selected row of a {@link JTable}.
 * <p/>
 * The selection can be controlled by using the selection model of the table, by interacting with the table, or by
 * setting the value of this property.
 * <p/>
 * If the value is to -1, then the selected row(s) will be de-selected. If the value is set to null, it will be
 * converted to -1 and therefore the selected row(s) will be de-selected.
 *
 * @see JTable#getSelectedRow()
 * @see ListSelectionModel#clearSelection()
 */
@Deprecated
public class JTableSelectedRowIndexProperty extends AbstractReadableWritableProperty<Integer> {

    /**
     * Entity tracking changes of selection (and selection model).
     */
    private final SelectionAdapter selectionAdapter = new SelectionAdapter();

    /**
     * Table whose selected row index is represented by this property.
     */
    private JTable table = null;

    /**
     * Current property value.
     */
    private int value = -1;

    /**
     * Flag indicating whether the {@link #setValue(Object)} call is due to a property change event.
     */
    private boolean updatingFromComponent = false;

    /**
     * Constructor specifying the table
     *
     * @param table Table whose selected row index is represented by this property.
     */
    public JTableSelectedRowIndexProperty(JTable table) {
        super();
        this.table = table;
        table.addPropertyChangeListener("selectionModel", selectionAdapter);
        table.getSelectionModel().addListSelectionListener(selectionAdapter);
        value = table.getSelectedRow();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (table != null) {
            table.getSelectionModel().removeListSelectionListener(selectionAdapter);
            table.removePropertyChangeListener("selectionModel", selectionAdapter);
            table = null;
        }
    }

    /**
     * @see AbstractReadableWritableProperty#getValue()
     */
    @Override
    public Integer getValue() {
        return value;
    }

    /**
     * @see AbstractReadableWritableProperty#setValue(Object)
     */
    @Override
    public void setValue(Integer value) {
        if (table != null) {
            Integer effectiveValue = value;
            if (effectiveValue == null) {
                effectiveValue = -1;
            }

            if (!isNotifyingListeners()) {
                if (updatingFromComponent) {
                    Integer oldValue = this.value;
                    this.value = effectiveValue;
                    maybeNotifyListeners(oldValue, this.value);
                } else if (effectiveValue < 0) {
                    table.getSelectionModel().clearSelection();
                } else {
                    table.getSelectionModel().setLeadSelectionIndex(effectiveValue);
                    table.getSelectionModel().setSelectionInterval(effectiveValue, effectiveValue);
                }
            }
        }
    }

    /**
     * Entity tracking changes of selection (and selection model).
     */
    private class SelectionAdapter implements ListSelectionListener, PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("selectionModel".equals(evt.getPropertyName())) {
                // Unregister from previous selection model
                if (evt.getOldValue() instanceof ListSelectionModel) {
                    ((ListSelectionModel) evt.getOldValue()).removeListSelectionListener(this);
                }

                // Register to new selection model
                if ((table != null) && (evt.getNewValue() instanceof ListSelectionModel)) {
                    ((ListSelectionModel) evt.getNewValue()).addListSelectionListener(this);

                    // Update value from new selection model
                    updatingFromComponent = true;
                    setValue(table.getSelectedRow());
                    updatingFromComponent = false;
                }
            }
        }

        /**
         * @see ListSelectionListener#valueChanged(ListSelectionEvent)
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if ((table != null) && !e.getValueIsAdjusting()) {
                updatingFromComponent = true;
                setValue(table.getSelectedRow());
                updatingFromComponent = false;
            }
        }
    }
}
