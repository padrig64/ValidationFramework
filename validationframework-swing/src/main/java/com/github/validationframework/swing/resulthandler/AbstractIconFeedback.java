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

import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.swing.decoration.IconComponentDecoration;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import javax.swing.Icon;
import javax.swing.JComponent;

public abstract class AbstractIconFeedback<O> implements ResultHandler<O> {

	private IconComponentDecoration decoration = null;

	public AbstractIconFeedback(final JComponent decoratedComponent) {
		attach(decoratedComponent);
	}

	public void attach(final JComponent decoratedComponent) {
		attach(decoratedComponent, IconComponentDecoration.DEFAULT_ANCHOR_LINK_WITH_OWNER);
	}

	public void attach(final JComponent decoratedComponent, final AnchorLink anchorLinkWithOwner) {
		boolean wasVisible = false;
		if (decoration != null) {
			wasVisible = decoration.isVisible();
		}

		detach();

		if (decoratedComponent != null) {
			decoration = new IconComponentDecoration(decoratedComponent);
			decoration.setAnchorLink(anchorLinkWithOwner);
			decoration.setVisible(wasVisible);
		}
	}

	public void detach() {
		if (decoration != null) {
			decoration.dispose();
			decoration = null;
		}
	}

	/**
	 * @see IconComponentDecoration#getDecoratedComponent()
	 */
	public JComponent getDecoratedComponent() {
		JComponent component = null;
		if (decoration != null) {
			component = decoration.getDecoratedComponent();
		}
		return component;
	}

	/**
	 * @see IconComponentDecoration#getClippingAncestor()
	 */
	public JComponent getClippingAncestor() {
		JComponent component = null;
		if (decoration != null) {
			component = decoration.getClippingAncestor();
		}
		return component;
	}

	/**
	 * @see IconComponentDecoration#setClippingAncestor(JComponent)
	 */
	public void setClippingAncestor(final JComponent decorationClippingAncestor) {
		if (decoration != null) {
			decoration.setClippingAncestor(decorationClippingAncestor);
		}
	}

	/**
	 * @see IconComponentDecoration#getAnchorLink()
	 */
	public AnchorLink getAnchorLink() {
		return decoration.getAnchorLink();
	}

	/**
	 * @see IconComponentDecoration#setAnchorLink(AnchorLink)
	 */
	public void setAnchorLink(final AnchorLink anchorLink) {
		decoration.setAnchorLink(anchorLink);
	}

	/**
	 * @see IconComponentDecoration#getIcon()
	 */
	protected Icon getIcon() {
		Icon icon = null;
		if (decoration != null) {
			icon = decoration.getIcon();
		}
		return icon;
	}

	/**
	 * @see IconComponentDecoration#setIcon(Icon)
	 */
	protected void setIcon(final Icon icon) {
		if (decoration != null) {
			decoration.setIcon(icon);
		}
	}

	/**
	 * @see IconComponentDecoration#getToolTipText()
	 */
	protected String getToolTipText() {
		String tip = null;
		if (decoration != null) {
			tip = decoration.getToolTipText();
		}
		return tip;
	}

	/**
	 * @see IconComponentDecoration#setToolTipText(String)
	 */
	protected void setToolTipText(final String text) {
		if (decoration != null) {
			decoration.setToolTipText(text);
		}
	}

	protected void showIconTip() {
		if (decoration != null) {
			decoration.setVisible(true);
		}
	}

	protected void hideIconTip() {
		if (decoration != null) {
			decoration.setVisible(false);
		}
	}
}
