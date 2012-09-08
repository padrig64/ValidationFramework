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

	private IconComponentDecoration decorator = null;

	public AbstractIconFeedback(final JComponent decoratedComponent) {
		attach(decoratedComponent);
	}

	public void attach(final JComponent decoratedComponent) {
		attach(decoratedComponent, IconComponentDecoration.DEFAULT_ANCHOR_LINK_WITH_OWNER);
	}

	public void attach(final JComponent decoratedComponent, final AnchorLink anchorLinkWithOwner) {
		boolean wasVisible = false;
		if (decorator != null) {
			wasVisible = decorator.isVisible();
		}

		detach();

		if (decoratedComponent != null) {
			decorator = new IconComponentDecoration(decoratedComponent);
			decorator.setAnchorLink(anchorLinkWithOwner);
			decorator.setVisible(wasVisible);
		}
	}

	public void detach() {
		if (decorator != null) {
			decorator.dispose();
			decorator = null;
		}
	}

	public JComponent getDecoratedComponent() {
		JComponent component = null;
		if (decorator != null) {
			component = decorator.getDecoratedComponent();
		}
		return component;
	}


	public AnchorLink getAnchorLink() {
		return decorator.getAnchorLink();
	}

	public void setAnchorLink(final AnchorLink anchorLink) {
		decorator.setAnchorLink(anchorLink);
	}

	protected Icon getIcon() {
		Icon icon = null;
		if (decorator != null) {
			icon = decorator.getIcon();
		}
		return icon;
	}

	protected void setIcon(final Icon icon) {
		if (decorator != null) {
			decorator.setIcon(icon);
		}
	}

	protected String getToolTipText() {
		String tip = null;
		if (decorator != null) {
			tip = decorator.getToolTipText();
		}
		return tip;
	}

	protected void setToolTipText(final String text) {
		if (decorator != null) {
			decorator.setToolTipText(text);
		}
	}

	protected void showIconTip() {
		if (decorator != null) {
			decorator.setVisible(true);
		}
	}

	protected void hideIconTip() {
		if (decorator != null) {
			decorator.setVisible(false);
		}
	}
}
