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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.JTable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Read-only property stating whether a specified table is in the editing state or not.
 */
public class JTableEditingProperty extends AbstractReadableProperty<Boolean> implements Disposable {

    /**
     * Name of the bean property to be used to track changes of the editor in the table.
     * <p>
     * Depending on the value of this property, it can be determined whether the table is in the editing state or not.
     */
    private static final String EDITING_BEAN_PROPERTY_NAME = "tableCellEditor";

    /**
     * Entity tracking changes of the editor in the table.
     */
    private final PropertyChangeListener editingBeanPropertyAdapter = new EditingBeanPropertyAdapter();

    /**
     * Table subject to edition.
     */
    private final JTable table;

    /**
     * Current value of the
     */
    private boolean editing = false;

    /**
     * Constructor specifying the table to be tracked.
     *
     * @param table Table subject to edition.
     */
    public JTableEditingProperty(JTable table) {
        super();
        this.table = table;
        this.table.addPropertyChangeListener(EDITING_BEAN_PROPERTY_NAME, editingBeanPropertyAdapter);
        this.editing = table.getCellEditor() != null;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        table.removePropertyChangeListener(EDITING_BEAN_PROPERTY_NAME, editingBeanPropertyAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return editing;
    }

    /**
     * Updates the current value of this property and notifies the listeners if it changed.
     *
     * @param editing True if the table is in the editing state, false otherwise.
     */
    private void setValue(boolean editing) {
        boolean wasEditing = this.editing;
        this.editing = editing;
        maybeNotifyListeners(wasEditing, editing);
    }

    /**
     * Entity listening the the bean property changes to detect whether the table is in the editing state or not.
     */
    private class EditingBeanPropertyAdapter implements PropertyChangeListener {

        /**
         * PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (EDITING_BEAN_PROPERTY_NAME.equals(evt.getPropertyName())) {
                setValue(evt.getNewValue() != null);
            }
        }
    }
}
