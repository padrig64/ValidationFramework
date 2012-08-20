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

package com.github.validationframework.decoration.swing;

import com.github.validationframework.decoration.swing.utils.Anchor;
import com.github.validationframework.decoration.swing.utils.AnchorLink;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;

public class IconTipDecorator extends AbstractDecorator {

	private class ToolTipAdapter extends MouseAdapter {

		@Override
		public void mouseEntered(final MouseEvent e) {
			toolTipDialog.setVisible(true);
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			toolTipDialog.setVisible(false);
		}
	}

	private Icon icon = null;

	private ToolTipDialog toolTipDialog = null;

	private static final AnchorLink DEFAULT_ANCHOR_LINK_WITH_OWNER = new AnchorLink(Anchor.BOTTOM_LEFT, Anchor.CENTER);

	private AnchorLink anchorLinkWithToolTip = new AnchorLink(Anchor.BOTTOM_RIGHT, Anchor.TOP_LEFT);

	public IconTipDecorator(final JComponent owner) {
		this(owner, null);
	}

	public IconTipDecorator(final JComponent owner, final Icon icon) {
		super(owner, DEFAULT_ANCHOR_LINK_WITH_OWNER);
		this.icon = icon;

		toolTipDialog = new ToolTipDialog(decorationHolder, anchorLinkWithToolTip);
		decorationHolder.addMouseListener(new ToolTipAdapter());
	}

	public String getText() {
		return toolTipDialog.getText();
	}

	public void setText(final String text) {
		toolTipDialog.setText(text);
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(final Icon icon) {
		this.icon = icon;
		followOwner();
	}

	public AnchorLink getAnchorLinkWithToolTip() {
		return anchorLinkWithToolTip;
	}

	public void setAnchorLinkWithToolTip(final AnchorLink anchorLinkWithToolTip) {
		this.anchorLinkWithToolTip = anchorLinkWithToolTip;
	}

	@Override
	public void setVisible(final boolean visible) {
		super.setVisible(visible);

		if (!visible && (toolTipDialog != null)) {
			toolTipDialog.setVisible(false);
		}
	}

	@Override
	protected int getWidth() {
		int width = 0;
		if (icon != null) {
			width = icon.getIconWidth();
		}
		return width;
	}

	@Override
	protected int getHeight() {
		int height = 0;
		if (icon != null) {
			height = icon.getIconHeight();
		}
		return height;
	}

	@Override
	public void paint(final Graphics g) {
		if (isVisible() && (icon != null)) {
			icon.paintIcon(decorationHolder, g, 0, 0);
		}
	}
}
