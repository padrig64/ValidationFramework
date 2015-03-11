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
import com.google.code.validationframework.swing.type.CellPosition;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Read-only property holding the position of the cell currently hovered by the mouse.
 * <p/>
 * The row and column indices, that make the position of the hovered cell, are expressed in view coordinates.
 */
public class JTableRolloverCellProperty extends AbstractReadableProperty<CellPosition> implements Disposable {

    /**
     * Table subject to rollover.
     */
    private final JTable table;

    /**
     * Entity tracking the mouse over the table and updating the value of this property accordingly.
     */
    private final RolloverAdapter rolloverAdapter = new RolloverAdapter();

    /**
     * Current value of this property.
     */
    private CellPosition value;

    /**
     * Constructor specifying the table to be tracked.
     *
     * @param table Table subject to rollover.
     */
    public JTableRolloverCellProperty(JTable table) {
        super();
        this.table = table;
        this.table.addMouseListener(rolloverAdapter);
        this.table.addMouseMotionListener(rolloverAdapter);

        // Initialize current value
        Point location = null;
        if (table.isShowing()) {
            location = new Point(MouseInfo.getPointerInfo().getLocation());
            SwingUtilities.convertPointFromScreen(location, table);
        }
        updateValue(location);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        table.removeMouseListener(rolloverAdapter);
        table.removeMouseMotionListener(rolloverAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public CellPosition getValue() {
        return value;
    }

    /**
     * Updates the value of this property based on the location of the mouse pointer.
     *
     * @param location Location of the mouse in the relatively to the table.
     */
    private void updateValue(Point location) {
        CellPosition oldValue = value;
        if (location == null) {
            value = null;
        } else {
            int row = table.rowAtPoint(location);
            int column = table.columnAtPoint(location);
            value = new CellPosition(row, column);
        }
        maybeNotifyListeners(oldValue, value);
    }

    /**
     * Entity tracking the mouse movements.
     */
    private class RolloverAdapter implements MouseListener, MouseMotionListener {

        /**
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            updateValue(e.getPoint());
        }

        /**
         * @see MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(MouseEvent e) {
            updateValue(e.getPoint());
        }

        /**
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent e) {
            // Nothing to be done
        }

        /**
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            // Nothing to be done
        }

        /**
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            // Nothing to be done
        }

        /**
         * @see MouseMotionListener#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            updateValue(e.getPoint());
        }

        /**
         * @see MouseMotionListener#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            updateValue(e.getPoint());
        }
    }
}
