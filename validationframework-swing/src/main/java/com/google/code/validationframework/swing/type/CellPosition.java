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

package com.google.code.validationframework.swing.type;

/**
 * Entity representing the position of a cell in a table.
 * <p/>
 * The row and column indices may be model or view indices, depending on where this class is used.
 * <p/>
 * A row index of -1 represents the table header.
 */
public class CellPosition {

    /**
     * Row index.
     * <p/>
     * A value of -1 represents the table header.
     */
    private final int row;

    /**
     * Column index.
     */
    private final int column;

    /**
     * Constructor.
     *
     * @param row    Row index (-1 for the table header).
     * @param column column index.
     */
    public CellPosition(int row, int column) {
        this.row = row;
        this.column = column;
    }

    /**
     * Gets the row index of the cell.
     *
     * @return Row index (-1 for the table header)
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the cell.
     *
     * @return Column index.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Generated.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CellPosition)) {
            return false;
        }

        CellPosition cell = (CellPosition) o;

        if (column != cell.column) {
            return false;
        }
        if (row != cell.row) {
            return false;
        }

        return true;
    }

    /**
     * Generated.
     */
    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }

    /**
     * Generated.
     */
    @Override
    public String toString() {
        return "CellPosition{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}
