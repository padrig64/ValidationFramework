/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.github.validationframework.swing.trigger;

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.api.trigger.TriggerListener;
import com.github.validationframework.base.trigger.AbstractTrigger;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

public class JTableComboBoxEditorModelChangedTrigger extends AbstractTrigger implements Disposable {

    private class SourceAdapter implements PropertyChangeListener {

        private final Map<Object, Disposable> editorToTrigger = new HashMap<Object, Disposable>();

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(final PropertyChangeEvent evt) {
            // Detach from previous editor
            if (evt.getOldValue() != null) {
                detach(evt.getOldValue());
            }

            // Attach to new only if it is the right cell
            if (evt.getNewValue() instanceof DefaultCellEditor) {
                attach((DefaultCellEditor) evt.getNewValue());
            }
        }

        private void attach(final DefaultCellEditor editor) {
            final Component editorComponent = editor.getComponent();
            if (editorComponent instanceof JComboBox) {
                final JComboBoxModelChangedTrigger trigger = new JComboBoxModelChangedTrigger((JComboBox)
                        editorComponent);
                trigger.addTriggerListener(triggerForwarder);
                editorToTrigger.put(editor, trigger);
                // TODO Check if already there?
            }
        }

        private void detach(final Object editor) {
            final Disposable trigger = editorToTrigger.get(editor);
            if (trigger != null) {
                trigger.dispose();
                editorToTrigger.remove(editor);
            }
        }
    }

    private class TriggerForwarder implements TriggerListener {

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(final TriggerEvent event) {
            /*
             * Check if trigger allowed here, because at the moment the trigger is registered (when the editor component
             * is set on the table), the editing row and editing column are not yet set in the table.
             */
            if (isTriggerAllowed()) {
                fireTriggerEvent(event);
            }
        }

        /**
         * Checks whether the trigger event is allowed to be fired according the row and column model indices.
         *
         * @return True if trigger is allowed, false otherwise.
         */
        private boolean isTriggerAllowed() {
            final boolean allow;

            if ((modelRowIndex == ALL_ROWS) && (modelColumnIndex == ALL_COLUMNS)) {
                allow = true;
            } else if (modelRowIndex == ALL_ROWS) {
                allow = isTriggerAllowedOnColumn();
            } else if (modelColumnIndex == ALL_COLUMNS) {
                allow = isTriggerAllowedOnRow();
            } else {
                allow = isTriggerAllowedOnCell();
            }

            return allow;
        }

        private boolean isTriggerAllowedOnRow() {
            boolean allow = false;

            try {
                final int viewRowIndex = table.convertRowIndexToView(modelRowIndex);
                if (viewRowIndex == table.getEditingRow()) {
                    allow = true;
                }
            } catch (IndexOutOfBoundsException e) {
                allow = false;
            }

            return allow;
        }

        private boolean isTriggerAllowedOnColumn() {
            boolean allow = false;

            try {
                final int viewColumnIndex = table.convertColumnIndexToView(modelColumnIndex);
                if (viewColumnIndex == table.getEditingColumn()) {
                    allow = true;
                }
            } catch (IndexOutOfBoundsException e) {
                allow = false;
            }

            return allow;
        }

        private boolean isTriggerAllowedOnCell() {
            boolean allow = false;

            try {
                final int viewRowIndex = table.convertRowIndexToView(modelRowIndex);
                final int viewColumnIndex = table.convertColumnIndexToView(modelColumnIndex);
                if ((viewRowIndex == table.getEditingRow()) && (viewColumnIndex == table.getEditingColumn())) {
                    allow = true;
                }
            } catch (IndexOutOfBoundsException e) {
                allow = false;
            }

            return allow;
        }
    }

    public static final int ALL_ROWS = -1;
    public static final int ALL_COLUMNS = -1;

    private JTable table = null;

    private final int modelRowIndex;

    private final int modelColumnIndex;

    private final SourceAdapter sourceAdapter = new SourceAdapter();

    private final TriggerListener triggerForwarder = new TriggerForwarder();

    public JTableComboBoxEditorModelChangedTrigger(final JTable table) {
        this(table, ALL_ROWS, ALL_COLUMNS);
    }

    public JTableComboBoxEditorModelChangedTrigger(final JTable table, final int modelRowIndex,
                                                   final int modelColumnIndex) {
        super();
        this.table = table;
        this.modelRowIndex = modelRowIndex;
        this.modelColumnIndex = modelColumnIndex;
        table.addPropertyChangeListener("tableCellEditor", sourceAdapter);
    }

    /**
     * Gets the source component.
     *
     * @return Source component.
     */
    public JTable getComponent() {
        return table;
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        table.removePropertyChangeListener("tableCellEditor", sourceAdapter);
        table = null;
    }
}
