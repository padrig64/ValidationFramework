/*
 * Copyright (c) 2012, Patrick Moawad
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

package com.github.validationframework.swing.resulthandler;

import com.github.validationframework.swing.decoration.IconComponentDecoration;
import com.github.validationframework.swing.decoration.anchor.Anchor;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractCellIconFeedback<O> extends AbstractIconFeedback<O> {

	private class CellTracker implements ComponentListener, TableColumnModelListener {

		/**
		 * @see ComponentListener#componentShown(ComponentEvent)
		 */
		@Override
		public void componentShown(final ComponentEvent e) {
			// Nothing to be done
		}

		/**
		 * @see ComponentListener#componentHidden(ComponentEvent)
		 */
		@Override
		public void componentHidden(final ComponentEvent e) {
			// Nothing to be done
		}

		/**
		 * @see ComponentListener#componentMoved(ComponentEvent)
		 */
		@Override
		public void componentMoved(final ComponentEvent e) {
			// Nothing to be done
		}

		/**
		 * @see ComponentListener#componentResized(ComponentEvent)
		 */
		@Override
		public void componentResized(final ComponentEvent e) {
			// Cell bounds may have changed
			followerDecoratedCell(0);
		}

		/**
		 * @see TableColumnModelListener#columnAdded(TableColumnModelEvent)
		 */
		@Override
		public void columnAdded(final TableColumnModelEvent e) {
			// Cell bounds may have changed
			followerDecoratedCell(0);
		}

		/**
		 * @see TableColumnModelListener#columnRemoved(TableColumnModelEvent)
		 */
		@Override
		public void columnRemoved(final TableColumnModelEvent e) {
			// Cell bounds may have changed
			followerDecoratedCell(0);
		}

		/**
		 * @see TableColumnModelListener#columnMoved(TableColumnModelEvent)
		 */
		@Override
		public void columnMoved(final TableColumnModelEvent e) {
			// Cell may have moved
			followerDecoratedCell(table.getTableHeader().getDraggedDistance());
		}

		/**
		 * @see TableColumnModelListener#columnMarginChanged(ChangeEvent)
		 */
		@Override
		public void columnMarginChanged(final ChangeEvent e) {
			// Cell bounds may have changed
			followerDecoratedCell(0);
		}

		/**
		 * @see TableColumnModelListener#columnSelectionChanged(ListSelectionEvent)
		 */
		@Override
		public void columnSelectionChanged(final ListSelectionEvent e) {
			// Column ordering has changed, so cell may have moved
			followerDecoratedCell(0);
		}
	}

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCellIconFeedback.class);

	private JTable table = null;

	private int modelRowIndex = -1;

	private int modelColumnIndex = -1;

	private AnchorLink anchorLinkWithCell = IconComponentDecoration.DEFAULT_ANCHOR_LINK_WITH_OWNER;

	private final CellTracker cellTracker = new CellTracker();

	public AbstractCellIconFeedback(final JTable table, final int modelRowIndex, final int modelColumnIndex) {
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
	public void attach(final JComponent decoratedComponent, final AnchorLink anchorLinkWithOwner) {
		super.attach(decoratedComponent, anchorLinkWithOwner);
		if (decoratedComponent instanceof JTable) {
			table = (JTable) decoratedComponent;
			table.addComponentListener(cellTracker);
			table.getColumnModel().addColumnModelListener(cellTracker);
		}
	}

	/**
	 * @see AbstractIconFeedback#detach()
	 */
	@Override
	public void detach() {
		if (table != null) {
			table.removeComponentListener(cellTracker);
			table.getColumnModel().removeColumnModelListener(cellTracker);
			// TODO Track column model replacement
		}
		super.detach();
	}

	public int getCellRowIndex() {
		return modelRowIndex;
	}

	public void setCellRowIndex(final int cellModelRowIndex) {
		modelRowIndex = cellModelRowIndex;
	}

	public int getCellColumnIndex() {
		return modelRowIndex;
	}

	public void setCellColumnIndex(final int cellModelColumnIndex) {
		modelColumnIndex = cellModelColumnIndex;
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
	public void setAnchorLink(final AnchorLink anchorLinkWithTableCell) {
		anchorLinkWithCell = anchorLinkWithTableCell;
		followerDecoratedCell(0);
	}

	/**
	 * Tracks the decorated cell and repositions the decoration.
	 *
	 * @param dragOffsetX Dragged distance in case the column is being dragged, 0 otherwise.
	 */
	private void followerDecoratedCell(final int dragOffsetX) {
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
	private AnchorLink getAbsoluteAnchorLinkWithCell(final int dragOffsetX) {
		final AnchorLink absoluteAnchorLink;

		final TableModel tableModel = table.getModel();
		if ((0 <= modelRowIndex) && (modelRowIndex < tableModel.getRowCount()) && (0 <= modelColumnIndex) &&
				(modelColumnIndex < tableModel.getColumnCount())) {
			final int viewRowIndex = table.convertRowIndexToView(modelRowIndex);
			final int viewColumnIndex = table.convertColumnIndexToView(modelColumnIndex);

			final Rectangle cellBounds = table.getCellRect(viewRowIndex, viewColumnIndex, true);
			final Anchor cellMasterAnchor =
					new Anchor(0.0f, cellBounds.x + dragOffsetX, 0.0f, cellBounds.y + cellBounds.height);
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
