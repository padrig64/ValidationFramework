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

package com.github.validationframework.swing.decoration;

import com.github.validationframework.swing.decoration.anchor.Anchor;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import com.github.validationframework.swing.decoration.support.ToolTipDialog;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;

public class IconComponentDecoration extends AbstractComponentDecoration {

	private class ToolTipAdapter extends MouseAdapter {

		@Override
		public void mouseMoved(final MouseEvent e) {
			if (toolTipDialog.isVisible()) {
				if (!decorationPainter.getClipBounds().contains(e.getPoint())) {
					toolTipDialog.setVisible(false);
				}
			} else {
				if (getDecoratedComponent().isShowing() && decorationPainter.getClipBounds().contains(e.getPoint())) {
					toolTipDialog.setVisible(true);
				}
			}
		}

		/**
		 * @see MouseAdapter#mouseExited(MouseEvent)
		 */
		@Override
		public void mouseExited(final MouseEvent e) {
			toolTipDialog.setVisible(false);
		}
	}

	/**
	 * Default anchor link with the owner component on which the decorator will be attached.
	 */
	// TODO Make this dependent on the LAF
	public static final AnchorLink DEFAULT_ANCHOR_LINK_WITH_OWNER =
			new AnchorLink(new Anchor(0.0f, 3, 1.0f, -3), Anchor.CENTER);

	/**
	 * Icon to be displayed as decoration on the owner component.
	 */
	private Icon icon = null;

	private ToolTipDialog toolTipDialog = null; // Lazy initialization to make sure we will have a parent

	private String toolTipText = null;

	private AnchorLink anchorLinkWithToolTip = new AnchorLink(Anchor.BOTTOM_RIGHT, Anchor.TOP_LEFT);

	public IconComponentDecoration(final JComponent decoratedComponent) {
		this(decoratedComponent, null);
	}

	public IconComponentDecoration(final JComponent decoratedComponent, final Icon icon) {
		super(decoratedComponent, DEFAULT_ANCHOR_LINK_WITH_OWNER);
		this.icon = icon;

		decorationPainter.addMouseListener(new ToolTipAdapter());
		decorationPainter.addMouseMotionListener(new ToolTipAdapter());
	}

	/**
	 * Gets the decoration icon.
	 *
	 * @return Decoration icon attached to the owner component.
	 */
	public Icon getIcon() {
		return icon;
	}

	/**
	 * Sets the decoration.
	 *
	 * @param icon Decoration icon to be attached to the owner component.
	 */
	public void setIcon(final Icon icon) {
		this.icon = icon;
		followDecoratedComponent();
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public void setToolTipText(final String text) {
		this.toolTipText = text;
		if (toolTipDialog != null) {
			toolTipDialog.setText(text);
		}
	}

	public AnchorLink getAnchorLinkWithToolTip() {
		return anchorLinkWithToolTip;
	}

	public void setAnchorLinkWithToolTip(final AnchorLink anchorLinkWithToolTip) {
		this.anchorLinkWithToolTip = anchorLinkWithToolTip;
	}

	/**
	 * @see AbstractComponentDecoration#setVisible(boolean)
	 */
	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		if ((toolTipDialog == null) && visible && (decorationPainter != null)) {
			toolTipDialog = new ToolTipDialog(decorationPainter, anchorLinkWithToolTip);
			toolTipDialog.setText(toolTipText);
		}
		if (!visible && (toolTipDialog != null)) {
			toolTipDialog.setVisible(false);
		}
	}

	/**
	 * @see AbstractComponentDecoration#getWidth()
	 */
	@Override
	protected int getWidth() {
		int width = 0;
		if (icon != null) {
			width = icon.getIconWidth();
		}
		return width;
	}

	/**
	 * @see AbstractComponentDecoration#getHeight()
	 */
	@Override
	protected int getHeight() {
		int height = 0;
		if (icon != null) {
			height = icon.getIconHeight();
		}
		return height;
	}

	/**
	 * @see AbstractComponentDecoration#paint(Graphics)
	 */
	@Override
	public void paint(final Graphics g) {
		if (isVisible() && (icon != null) && (decorationPainter != null)) {
			icon.paintIcon(decorationPainter, g, 0, 0);
		}
	}
}
