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

package com.github.validationframework.experimental.decoration;

import com.github.validationframework.swing.decoration.AbstractComponentDecoration;
import com.github.validationframework.swing.decoration.anchor.Anchor;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JComponent;

@Deprecated
public class IconComponentDecoration extends AbstractComponentDecoration {

	/**
	 * Default anchor link with the owner component on which the decorator will be attached.
	 */
	public static final AnchorLink DEFAULT_ANCHOR_LINK_WITH_OWNER = new AnchorLink(Anchor.BOTTOM_LEFT, Anchor.CENTER);

	/**
	 * Icon to be displayed as decoration on the owner component.
	 */
	private Icon icon = null;

	public IconComponentDecoration(final JComponent owner) {
		this(owner, null, DEFAULT_ANCHOR_LINK_WITH_OWNER);
	}

	public IconComponentDecoration(final JComponent owner, final AnchorLink anchorLinkWithOwner) {
		this(owner, null, anchorLinkWithOwner);
	}

	public IconComponentDecoration(final JComponent owner, final Icon icon) {
		this(owner, icon, DEFAULT_ANCHOR_LINK_WITH_OWNER);
	}

	public IconComponentDecoration(final JComponent owner, final Icon icon, final AnchorLink anchorLinkWithOwner) {
		super(owner, anchorLinkWithOwner);
		this.icon = icon;
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
		if (isVisible() && (icon != null)) {
			icon.paintIcon(decorationPainter, g, 0, 0);
		}
	}
}
