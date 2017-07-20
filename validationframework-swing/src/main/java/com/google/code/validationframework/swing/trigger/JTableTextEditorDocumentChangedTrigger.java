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

package com.google.code.validationframework.swing.trigger;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.trigger.TriggerEvent;
import com.google.code.validationframework.api.trigger.TriggerListener;
import com.google.code.validationframework.base.trigger.AbstractTrigger;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Trigger initiating the validation whenever the document of the text editor of one particular cell or any cell is
 * changed.
 */
public class JTableTextEditorDocumentChangedTrigger extends AbstractTrigger implements Disposable {

    /**
     * Entity responsible of tracking text editors whenever the table is being edited.
     * <p>
     * This entity will create and install a trigger for any new text component responsible for editing the cell.
     */
    private class SourceAdapter implements PropertyChangeListener, Disposable {

        /**
         * Mapping between editors and the triggers installed on them.
         */
        private final Map<Object, Disposable> editorToTrigger = new HashMap<Object, Disposable>();

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // Detach from previous editor
            if (evt.getOldValue() != null) {
                detach(evt.getOldValue());
            }

            // Attach to new editor if possible
            if (evt.getNewValue() != null) {
                attach(evt.getNewValue());
            }
        }

        /**
         * Installs a trigger on the editor component if it is a text component.
         *
         * @param editor Editor to install a trigger to.
         */
        private void attach(Object editor) {
            if ((editor instanceof DefaultCellEditor) && !editorToTrigger.containsKey(editor)) {
                Component editorComponent = ((DefaultCellEditor) editor).getComponent();
                if (editorComponent instanceof JTextComponent) {
                    JTextComponentDocumentChangedTrigger trigger = new JTextComponentDocumentChangedTrigger(
                            (JTextComponent) editorComponent);
                    trigger.addTriggerListener(triggerForwarder);
                    editorToTrigger.put(editor, trigger);
                }
            }
        }

        /**
         * Uninstalls the previously installed trigger on the editor component.
         *
         * @param editor Editor to uninstall the trigger from.
         */
        private void detach(Object editor) {
            Disposable trigger = editorToTrigger.get(editor);
            if (trigger != null) {
                trigger.dispose();
                editorToTrigger.remove(editor);
            }
        }

        /**
         * @see Disposable#dispose()
         * @see JTableTextEditorDocumentChangedTrigger#dispose()
         */
        @Override
        public void dispose() {
            // Detach all
            for (Map.Entry<Object, Disposable> entry : editorToTrigger.entrySet()) {
                entry.getValue().dispose();
            }
            editorToTrigger.clear();
        }
    }

    /**
     * Trigger listener that will forward the trigger events from the text component editing the cell to the trigger
     * listeners installed on this {@link JTableTextEditorDocumentChangedTrigger}.
     */
    private class TriggerForwarder implements TriggerListener {

        /**
         * @see TriggerListener#triggerValidation(TriggerEvent)
         */
        @Override
        public void triggerValidation(TriggerEvent event) {
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
            boolean allow;

            if ((modelRowIndex == ALL_ROWS) && (modelColumnIndex == ALL_COLUMNS)) {
                allow = true;
            } else if (modelRowIndex == ALL_ROWS) {
                allow = isTriggerAllowedOnColumn();
            } else if (modelColumnIndex == ALL_COLUMNS) {
                allow = isTriggerAllowedOnRow();
            } else {
                allow = isTriggerAllowedOnRow() && isTriggerAllowedOnColumn();
            }

            return allow;
        }

        /**
         * Checks whether the trigger event is allowed to be fired according the particular row model index.
         *
         * @return True if trigger is allowed, false otherwise.
         */
        private boolean isTriggerAllowedOnRow() {
            boolean allow = false;

            try {
                int viewRowIndex = table.convertRowIndexToView(modelRowIndex);
                if (viewRowIndex == table.getEditingRow()) {
                    allow = true;
                }
            } catch (IndexOutOfBoundsException e) {
                allow = false;
            }

            return allow;
        }

        /**
         * Checks whether the trigger event is allowed to be fired according the particular column model index.
         *
         * @return True if trigger is allowed, false otherwise.
         */
        private boolean isTriggerAllowedOnColumn() {
            boolean allow = false;

            try {
                int viewColumnIndex = table.convertColumnIndexToView(modelColumnIndex);
                if (viewColumnIndex == table.getEditingColumn()) {
                    allow = true;
                }
            } catch (IndexOutOfBoundsException e) {
                allow = false;
            }

            return allow;
        }
    }

    /**
     * Constant representing any row.
     *
     * @see #JTableTextEditorDocumentChangedTrigger(JTable, int, int)
     */
    public static final int ALL_ROWS = -1;

    /**
     * Constant representing any column.
     *
     * @see #JTableTextEditorDocumentChangedTrigger(JTable, int, int)
     */
    public static final int ALL_COLUMNS = -1;

    /**
     * Table to be monitored.
     */
    private final JTable table;

    /**
     * Model index of the row of the cell to be monitored.
     *
     * @see #ALL_ROWS
     */
    private final int modelRowIndex;

    /**
     * Model index of the column of the cell to be monitored.
     *
     * @see #ALL_COLUMNS
     */
    private final int modelColumnIndex;

    /**
     * Entity tracking the editor on the table.
     */
    private final SourceAdapter sourceAdapter = new SourceAdapter();

    /**
     * Trigger listener forwarding trigger events from the text component editor to the listeners of this trigger.
     */
    private final TriggerListener triggerForwarder = new TriggerForwarder();

    /**
     * Constructor specifying the table whose text editor should trigger the validation.
     * <p>
     * Validation will be triggered for any edited cell.
     *
     * @param table Table whose text editor should trigger the validation.
     */
    public JTableTextEditorDocumentChangedTrigger(JTable table) {
        this(table, ALL_ROWS, ALL_COLUMNS);
    }

    /**
     * Constructor specifying the table and cell position whose text editor should trigger the validation.
     *
     * @param table            Table whose text editor should trigger the validation.
     * @param modelRowIndex    Model row index of the cell whose text editor should trigger the validation.
     * @param modelColumnIndex Model column index of the cell whose text editor should trigger the validation.
     *
     * @see #ALL_ROWS
     * @see #ALL_COLUMNS
     * @see #JTableTextEditorDocumentChangedTrigger(JTable)
     */
    public JTableTextEditorDocumentChangedTrigger(JTable table, int modelRowIndex, int modelColumnIndex) {
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
        sourceAdapter.dispose();
    }
}
