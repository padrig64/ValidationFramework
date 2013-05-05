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

package com.github.validationframework.swing.dataprovider;

import com.github.validationframework.api.dataprovider.DataProvider;

import javax.swing.JTable;
import javax.swing.text.JTextComponent;
import java.awt.Component;

/**
 * Provider of the text of the current text editor component from a given table.<br>Note that if the table is not in
 * editing state, no text can be provided.
 *
 * @see DataProvider
 * @see JTable
 * @see JTable#getCellEditor()
 */
public class JTableTextEditorTextProvider implements DataProvider<String> {

    /**
     * Table holding the editor component to get the text from.
     */
    private final JTable table;

    /**
     * Constructor specifying the table holding the editor component to get the text from.
     *
     * @param table Editable table.
     */
    public JTableTextEditorTextProvider(final JTable table) {
        this.table = table;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public String getData() {
        String data = null;

        final Component editorComponent = table.getEditorComponent();
        if (editorComponent instanceof JTextComponent) {
            data = ((JTextComponent) editorComponent).getText();
        }

        return data;
    }
}
