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

import javax.swing.JList;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Read-only property representing the number of selected items in a {@link JList}.
 */
public class JListSelectedItemCountProperty extends AbstractReadableProperty<Integer> implements Disposable {

    /**
     * Entity tracking changes of selection (and selection model).
     */
    private final SelectionAdapter selectionAdapter = new SelectionAdapter();

    /**
     * List whose selection count is to be tracked.
     */
    private JList list = null;

    /**
     * Current property value.
     */
    private int count = 0;

    /**
     * Constructor specifying the list whose selection count is represented by this property.
     *
     * @param list List whose selection count is to be tracked.
     */
    public JListSelectedItemCountProperty(JList list) {
        this.list = list;
        list.addPropertyChangeListener("selectionModel", selectionAdapter);
        list.getSelectionModel().addListSelectionListener(selectionAdapter);
        count = list.getSelectedIndices().length;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        if (list != null) {
            list.getSelectionModel().removeListSelectionListener(selectionAdapter);
            list.removePropertyChangeListener("selectionModel", selectionAdapter);
            list = null;
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
     * Updates the value of this property based on the list's selection model and notify the listeners.
     */
    private void updateValue() {
        if (list != null) {
            int oldCount = this.count;
            this.count = list.getSelectedIndices().length;
            maybeNotifyListeners(oldCount, count);
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
                if (evt.getNewValue() instanceof ListSelectionModel) {
                    ((ListSelectionModel) evt.getNewValue()).addListSelectionListener(this);

                    // Update value from new selection model
                    updateValue();
                }
            }
        }

        /**
         * @see ListSelectionListener#valueChanged(ListSelectionEvent)
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                updateValue();
            }
        }
    }
}
