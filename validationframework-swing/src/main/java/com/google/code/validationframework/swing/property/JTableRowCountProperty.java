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
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Read-only property representing the number of rows in a {@link JTable}.
 * <p>
 * Note that this row count corresponds to the view and therefore may differ from the row count in the table model.
 */
public class JTableRowCountProperty extends AbstractReadableProperty<Integer> implements Disposable {

    /**
     * Entity tracking changes in the table model.
     */
    private class RowAdapter implements TableModelListener, PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if ("model".equals(evt.getPropertyName())) {
                // Unregister from previous table model
                if (evt.getOldValue() instanceof TableModel) {
                    ((TableModel) evt.getOldValue()).removeTableModelListener(this);
                }

                // Register to new table model
                if (evt.getNewValue() instanceof TableModel) {
                    ((TableModel) evt.getNewValue()).addTableModelListener(this);

                    // Update value
                    updateValue();
                }
            }
        }

        /**
         * @see TableModelListener#tableChanged(TableModelEvent)
         */
        @Override
        public void tableChanged(TableModelEvent e) {
            // Could be optimized, but good enough for now
            updateValue();
        }
    }

    /**
     * Table whose row count is to be tracked.
     */
    private JTable table = null;

    /**
     * Entity tracking changes in the table model.
     */
    private final RowAdapter rowAdapter = new RowAdapter();

    /**
     * Current property value.
     */
    private int count = 0;

    /**
     * Constructor specifying the table whose row count is represented by this property.
     *
     * @param table Table whose row count is to be tracked.
     */
    public JTableRowCountProperty(JTable table) {
        this.table = table;
        table.addPropertyChangeListener("model", rowAdapter);
        table.getModel().addTableModelListener(rowAdapter);
        count = table.getRowCount();
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (table != null) {
            table.getModel().removeTableModelListener(rowAdapter);
            table.removePropertyChangeListener("model", rowAdapter);
            table = null;
        }
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Integer getValue() {
        return count;
    }

    /**
     * Updates the value of this property and notify the listeners.
     */
    private void updateValue() {
        if (table != null) {
            int oldCount = this.count;
            this.count = table.getRowCount();
            maybeNotifyListeners(oldCount, count);
        }
    }
}
