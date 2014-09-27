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

package com.google.code.validationframework.swing.transform;

import com.google.code.validationframework.api.transform.Transformer;

import javax.swing.JTable;
import javax.swing.RowSorter;

/**
 * Transformer converting table row model indices to view indices.
 */
public class JTableRowIndexToViewTransformer implements Transformer<Integer, Integer> {

    /**
     * Table holding the row sorter to be used to convert the model row index to view row index.
     *
     * @see #rowSorter
     */
    private final JTable table;

    /**
     * Table row sorter to be used to convert the model row index to view row index in case no table is specified.
     *
     * @see #table
     */
    private final RowSorter<?> rowSorter;

    /**
     * Constructor specifying the table to be used to convert model row indices to view indices.
     *
     * @param table Table to be used to convert model row indices to view indices.
     */
    public JTableRowIndexToViewTransformer(JTable table) {
        this.table = table;
        this.rowSorter = null;
    }

    /**
     * Constructor specifying the table row sorter to be used to convert model row indices to view indices.
     *
     * @param rowSorter Table row sorter to be used to convert model row indices to view indices.
     */
    public JTableRowIndexToViewTransformer(RowSorter<?> rowSorter) {
        this.table = null;
        this.rowSorter = rowSorter;
    }

    /**
     * @see Transformer#transform(Object)
     */
    @Override
    public Integer transform(Integer modelRowIndex) {
        int viewRowIndex;

        if (table != null) {
            viewRowIndex = table.convertRowIndexToView(modelRowIndex);
        } else if (rowSorter != null) {
            viewRowIndex = rowSorter.convertRowIndexToView(modelRowIndex);
        } else {
            viewRowIndex = modelRowIndex;
        }

        return viewRowIndex;
    }
}
