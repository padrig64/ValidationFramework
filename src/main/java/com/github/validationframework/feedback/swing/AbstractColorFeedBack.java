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

package com.github.validationframework.feedback.swing;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.utils.swing.ColorUtils;
import java.awt.Color;
import javax.swing.JComponent;

public abstract class AbstractColorFeedBack<R> implements FeedBack<R> {

	private JComponent owner = null;
	private Color origForeground = null;
	private Color origBackground = null;
	private Color resultForeground = null;
	private Color resultBackground = null;
	private boolean showing = false;

	public AbstractColorFeedBack(final JComponent owner) {
		attach(owner);
	}

	public void attach(final JComponent owner) {
		detach();
		this.owner = owner;
	}

	public void detach() {
		this.owner = null;
	}

	protected Color getForeground() {
		return resultForeground;
	}

	protected void setForeground(final Color foreground) {
		this.resultForeground = foreground;
	}

	protected Color getBackground() {
		return resultBackground;
	}

	protected void setBackground(final Color background) {
		this.resultBackground = background;
	}

	protected void showColors() {
		if (!showing) {
			origForeground = owner.getForeground();
			origBackground = owner.getBackground();
		}

		if (resultForeground == null) {
			owner.setForeground(origForeground);
		} else {
			owner.setForeground(ColorUtils.alphaBlend(resultForeground, origForeground));
		}
		if (resultBackground == null) {
			owner.setBackground(origBackground);
		} else {
			owner.setBackground(ColorUtils.alphaBlend(resultBackground, origBackground));
		}
		owner.getParent().repaint();

		showing = true;
	}

	protected void hideColors() {
		if (showing) {
			owner.setForeground(origForeground);
			owner.setBackground(origBackground);
		}
		showing = false;
	}
}
