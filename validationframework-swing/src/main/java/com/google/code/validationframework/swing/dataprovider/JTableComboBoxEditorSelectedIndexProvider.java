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

package com.google.code.validationframework.swing.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComboBox;
import javax.swing.JTable;
import java.awt.Component;

/**
 * Provider of the selected index of the current combobox editor component from a given table.<br>Note that if the table
 * is not in editing, no index can be provided.
 *
 * @see DataProvider
 * @see JTable
 * @see JTable#getCellEditor()
 */
public class JTableComboBoxEditorSelectedIndexProvider implements DataProvider<Integer> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JTableComboBoxEditorSelectedIndexProvider.class);

    /**
     * Table holding the editor component to get the text from.
     */
    private final JTable table;

    /**
     * Constructor specifying the table holding the editor component to get the text from.
     *
     * @param table Editable table.
     */
    public JTableComboBoxEditorSelectedIndexProvider(final JTable table) {
        this.table = table;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public JTable getComponent() {
        return table;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public Integer getData() {
        Integer selectedIndex = null;

        // Get the combobox editor from the table, if any
        final Component editorComponent = table.getEditorComponent();
        if (editorComponent instanceof JComboBox) {
            // Get the selected index from the combobox
            selectedIndex = ((JComboBox) editorComponent).getSelectedIndex();
        } else {
            LOGGER.warn("Table editor component is not a JComboBox: " + editorComponent);
        }

        return selectedIndex;
    }
}
