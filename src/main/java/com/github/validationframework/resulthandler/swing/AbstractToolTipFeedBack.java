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

package com.github.validationframework.resulthandler.swing;

import com.github.validationframework.decoration.swing.ToolTipDialog;
import com.github.validationframework.decoration.swing.utils.Anchor;
import com.github.validationframework.decoration.swing.utils.DualAnchor;
import com.github.validationframework.resulthandler.TypedResultHandler;
import javax.swing.JComponent;

public abstract class AbstractToolTipFeedBack<R> implements TypedResultHandler<R> {

	private ToolTipDialog toolTipDialog = null;

	public AbstractToolTipFeedBack(final JComponent owner) {
		attach(owner);
	}

	public void attach(final JComponent owner) {
		detach();
		toolTipDialog = new ToolTipDialog(owner, new DualAnchor(Anchor.TOP_RIGHT, Anchor.TOP_LEFT));
	}

	public void detach() {
		if (toolTipDialog != null) {
			toolTipDialog.dispose();
			toolTipDialog = null;
		}
	}

	protected String getToolTipText() {
		String tip = null;

		if (toolTipDialog != null) {
			tip = toolTipDialog.getText();
		}

		return tip;
	}

	protected void setToolTipText(final String text) {
		if (toolTipDialog != null) {
			toolTipDialog.setText(text);
		}
	}

	protected void showToolTip() {
		toolTipDialog.setVisible(true);
	}

	protected void hideToolTip() {
		toolTipDialog.setVisible(false);
	}
}
