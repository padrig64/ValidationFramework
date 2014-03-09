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

package com.google.code.validationframework.swing.resulthandler;

import com.google.code.validationframework.swing.decoration.IconComponentDecoration;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Abstract implementation of a result handler using an {@link IconComponentDecoration} to show feedback to the user on
 * a table cell.
 * <p/>
 * Concrete classes only need to implement the {@link #handleResult(Object)} method by calling the {@link #showIcon()}
 * and {@link #hideIcon()} methods according to the result.
 *
 * @param <RHI> Type of result handler input.
 *
 * @see AbstractIconFeedback
 */
public abstract class AbstractCellIconFeedback<RHI> extends AbstractIconFeedback<RHI> {

    /**
     * Entity responsible of tracking the location of the cell to which a feedback decoration is attached.
     */
    private class CellTracker implements ComponentListener, TableColumnModelListener, PropertyChangeListener,
            RowSorterListener {

        /**
         * @see ComponentListener#componentShown(ComponentEvent)
         */
        @Override
        public void componentShown(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentHidden(ComponentEvent)
         */
        @Override
        public void componentHidden(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentResized(ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            // Cell bounds may have changed
            followDecoratedCell(0);
        }

        /**
         * @see TableColumnModelListener#columnAdded(TableColumnModelEvent)
         */
        @Override
        public void columnAdded(TableColumnModelEvent e) {
            // Cell bounds may have changed
            followDecoratedCell(0);
        }

        /**
         * @see TableColumnModelListener#columnRemoved(TableColumnModelEvent)
         */
        @Override
        public void columnRemoved(TableColumnModelEvent e) {
            // Cell bounds may have changed
            followDecoratedCell(0);
        }

        /**
         * @see TableColumnModelListener#columnMoved(TableColumnModelEvent)
         */
        @Override
        public void columnMoved(TableColumnModelEvent e) {
            // Cell may have moved

            // Check if it is the moved column correspond to the decorated cell
            int columnIndex = e.getFromIndex();
            if ((0 <= columnIndex) && (columnIndex < table.getColumnCount())) {
                columnIndex = table.convertColumnIndexToModel(columnIndex);
            }
            if (columnIndex == modelColumnIndex) {
                // Column of decorated cell is being dragged
                followDecoratedCell(table.getTableHeader().getDraggedDistance());
            } else {
                // Another column has been dragged but this column have moved
                followDecoratedCell(0);
            }
        }

        /**
         * @see TableColumnModelListener#columnMarginChanged(ChangeEvent)
         */
        @Override
        public void columnMarginChanged(ChangeEvent e) {
            // Cell bounds may have changed
            followDecoratedCell(0);
        }

        /**
         * @see TableColumnModelListener#columnSelectionChanged(ListSelectionEvent)
         */
        @Override
        public void columnSelectionChanged(ListSelectionEvent e) {
            // Column ordering has changed, so cell may have moved
            followDecoratedCell(0);
        }

        /**
         * Tracks changes of the column model and row sorter on the table.<br>Every time the column model or row
         * sorter is
         * replaced, a listener is installed.
         *
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent e) {
            if ("columnModel".equals(e.getPropertyName())) {
                // Unhook from previous column model
                Object oldValue = e.getOldValue();
                if (oldValue instanceof TableColumnModel) {
                    ((TableColumnModel) oldValue).removeColumnModelListener(this);
                }

                // Hook to new column model
                Object newValue = e.getNewValue();
                if (newValue instanceof TableColumnModel) {
                    ((TableColumnModel) newValue).addColumnModelListener(this);
                }

            } else if ("sorter".equals(e.getPropertyName()) || "rowSorter".equals(e.getPropertyName())) {
                // Unhook from previous row sorter
                Object oldValue = e.getOldValue();
                if (oldValue instanceof RowSorter<?>) {
                    ((RowSorter<?>) oldValue).removeRowSorterListener(this);
                }

                // Hook to new row sorter
                Object newValue = e.getNewValue();
                if (newValue instanceof RowSorter<?>) {
                    ((RowSorter<?>) newValue).addRowSorterListener(this);
                }
            }
        }

        /**
         * @see RowSorterListener#sorterChanged(RowSorterEvent)
         */
        @Override
        public void sorterChanged(RowSorterEvent e) {
            followDecoratedCell(0);
        }
    }

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCellIconFeedback.class);

    /**
     * Table containing the cell to be decorated.
     */
    private JTable table = null;

    /**
     * Row model index of the cell to be decorated.
     */
    private int modelRowIndex = -1;

    /**
     * Column model index of the cell to be decorated.
     */
    private int modelColumnIndex = -1;

    /**
     * Anchor link between the cell and the decoration.
     * <p/>
     * It is used to slave the decoration to the cell in terms of location.
     */
    private AnchorLink anchorLinkWithCell = IconComponentDecoration.DEFAULT_ANCHOR_LINK_WITH_OWNER;

    /**
     * Entity track changes on the cell location and size and adjusting the decoration accordingly.
     */
    private final CellTracker cellTracker = new CellTracker();

    /**
     * Constructor specifying the table and the cell to be decorated.
     *
     * @param table            Table containing the cell to be decorated.
     * @param modelRowIndex    Model row index of the cell to be decorated.
     * @param modelColumnIndex Model column index of the cell to be decorated.
     */
    public AbstractCellIconFeedback(JTable table, int modelRowIndex, int modelColumnIndex) {
        super(table);
        this.table = table;
        this.modelRowIndex = modelRowIndex;
        this.modelColumnIndex = modelColumnIndex;
        setAnchorLink(super.getAnchorLink());
        attach(table);
    }

    /**
     * @see AbstractIconFeedback#detach()
     */
    @Override
    public void attach(JComponent decoratedComponent, AnchorLink anchorLinkWithOwner) {
        super.attach(decoratedComponent, anchorLinkWithOwner);
        if (decoratedComponent instanceof JTable) {
            table = (JTable) decoratedComponent;
            table.addComponentListener(cellTracker);
            table.addPropertyChangeListener("columnModel", cellTracker);
            if (table.getColumnModel() != null) {
                table.getColumnModel().addColumnModelListener(cellTracker);
            }
            table.addPropertyChangeListener("sorter", cellTracker);
            table.addPropertyChangeListener("rowSorter", cellTracker);
            if (table.getRowSorter() != null) {
                table.getRowSorter().addRowSorterListener(cellTracker);
            }
        }
    }

    /**
     * @see AbstractIconFeedback#detach()
     */
    @Override
    public void detach() {
        if (table != null) {
            table.removeComponentListener(cellTracker);
            table.removePropertyChangeListener("columnModel", cellTracker);
            if (table.getColumnModel() != null) {
                table.getColumnModel().removeColumnModelListener(cellTracker);
            }
            table.removePropertyChangeListener("sorter", cellTracker);
            table.removePropertyChangeListener("rowSorter", cellTracker);
            if (table.getRowSorter() != null) {
                table.getRowSorter().removeRowSorterListener(cellTracker);
            }
        }
        super.detach();
    }

    /**
     * Gets the row model index of the decorated cell.
     *
     * @return Row model index of the decorated cell.
     */
    public int getCellRowIndex() {
        return modelRowIndex;
    }

    /**
     * Sets the row model index of the cell to be decorated.
     *
     * @param cellModelRowIndex Row model index of the cell to be decorated.
     */
    public void setCellRowIndex(int cellModelRowIndex) {
        modelRowIndex = cellModelRowIndex;
        followDecoratedCell(0);
    }

    /**
     * Gets the column model index of the decorated cell.
     *
     * @return Column model index of the decorated cell.
     */
    public int getCellColumnIndex() {
        return modelColumnIndex;
    }

    /**
     * Sets the column model index of the cell to be decorated.
     *
     * @param cellModelColumnIndex Column model index of the cell to be decorated.
     */
    public void setCellColumnIndex(int cellModelColumnIndex) {
        modelColumnIndex = cellModelColumnIndex;
        followDecoratedCell(0);
    }

    /**
     * Gets the anchor link between the cell and its decoration.
     *
     * @return Anchor link between the cell and its decoration.
     */
    @Override
    public AnchorLink getAnchorLink() {
        return anchorLinkWithCell;
    }

    /**
     * Sets the anchor link between the cell and its decoration.
     *
     * @param anchorLinkWithTableCell Anchor link between the cell and its decoration.
     */
    @Override
    public void setAnchorLink(AnchorLink anchorLinkWithTableCell) {
        anchorLinkWithCell = anchorLinkWithTableCell;
        followDecoratedCell(0);
    }

    /**
     * Tracks the decorated cell and repositions the decoration.
     *
     * @param dragOffsetX Dragged distance in case the column is being dragged, 0 otherwise.
     */
    private void followDecoratedCell(int dragOffsetX) {
        // Gets the absolute anchor link and update the decoration
        super.setAnchorLink(getAbsoluteAnchorLinkWithCell(dragOffsetX));
    }

    /**
     * Retrieves the absolute anchor link to attach the decoration to the cell.
     *
     * @param dragOffsetX Dragged distance in case the column is being dragged, 0 otherwise.
     *
     * @return Absolute anchor link to attach the decoration to the cell.
     */
    private AnchorLink getAbsoluteAnchorLinkWithCell(int dragOffsetX) {
        AnchorLink absoluteAnchorLink;

        TableModel tableModel = table.getModel();
        if ((0 <= modelRowIndex) && (modelRowIndex < tableModel.getRowCount()) && (0 <= modelColumnIndex) &&
                (modelColumnIndex < tableModel.getColumnCount())) {
            int viewRowIndex = table.convertRowIndexToView(modelRowIndex);
            int viewColumnIndex = table.convertColumnIndexToView(modelColumnIndex);

            Rectangle cellBounds = table.getCellRect(viewRowIndex, viewColumnIndex, true);
            Anchor cellMasterAnchor = new Anchor(0.0f, cellBounds.x + dragOffsetX, 0.0f,
                    cellBounds.y + cellBounds.height);
            absoluteAnchorLink = new AnchorLink(cellMasterAnchor, anchorLinkWithCell.getSlaveAnchor());
        } else {
            // Maybe the table has been emptied? or the row has been filtered out? or invalid row/column index?
            LOGGER.debug("Cell at model row and/or column indices is not visible: (" + modelRowIndex + "," +
                    modelColumnIndex +
                    ") for table dimensions (" + tableModel.getRowCount() + "," + tableModel.getColumnCount() + ")");

            // Decoration will not be shown
            absoluteAnchorLink = null;
        }

        return absoluteAnchorLink;
    }
}
