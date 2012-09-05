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

package com.github.validationframework.swing.resulthandler.bool;

import com.github.validationframework.swing.decoration.utils.IconUtils;
import com.github.validationframework.swing.resulthandler.AbstractIconTipFeedback2;
import javax.swing.Icon;
import javax.swing.JComponent;

public class IconTipBooleanFeedback extends AbstractIconTipFeedback2<Boolean> {

	public static final Icon DEFAULT_VALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/valid.png", IconTipBooleanFeedback.class);

	public static final Icon DEFAULT_INVALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/invalid.png", IconTipBooleanFeedback.class);

	private Icon validIcon = null;

	private Icon invalidIcon = null;

	private String validText = null;

	private String invalidText = null;

	private Boolean lastResult = null;

	public IconTipBooleanFeedback(final JComponent owner) {
		this(owner, DEFAULT_VALID_ICON, DEFAULT_INVALID_ICON);
	}

	public IconTipBooleanFeedback(final JComponent owner, final Icon validIcon, final Icon invalidIcon) {
		this(owner, validIcon, null, invalidIcon, null);
	}

	public IconTipBooleanFeedback(final JComponent owner, final String validText, final String invalidText) {
		this(owner, DEFAULT_VALID_ICON, validText, DEFAULT_INVALID_ICON, invalidText);
	}

	public IconTipBooleanFeedback(final JComponent owner, final Icon validIcon, final String validText,
								  final Icon invalidIcon, final String invalidText) {
		super(owner);

		setValidIcon(validIcon);
		setValidText(validText);
		setInvalidIcon(invalidIcon);
		setInvalidText(invalidText);
	}

	public Icon getValidIcon() {
		return validIcon;
	}

	public void setValidIcon(final Icon validIcon) {
		this.validIcon = validIcon;
		updateDecoration();
	}

	public String getValidText() {
		return validText;
	}

	public void setValidText(final String validText) {
		this.validText = validText;
		updateDecoration();
	}

	public Icon getInvalidIcon() {
		return invalidIcon;
	}

	public void setInvalidIcon(final Icon invalidIcon) {
		this.invalidIcon = invalidIcon;
		updateDecoration();
	}

	public String getInvalidText() {
		return invalidText;
	}

	public void setInvalidText(final String invalidText) {
		this.invalidText = invalidText;
		updateDecoration();
	}

	/**
	 * @see com.github.validationframework.swing.resulthandler.AbstractIconTipFeedback2#handleResult(Object)
	 */
	@Override
	public void handleResult(final Boolean valid) {
		lastResult = valid;

		updateDecoration();

		if ((valid && (validIcon != null)) || (!valid && (invalidIcon != null))) {
			showIconTip();
		} else {
			hideIconTip();
		}
	}

	private void updateDecoration() {
		if (lastResult != null) {
			if (lastResult) {
				setIcon(validIcon);
				setToolTipText(validText);
			} else {
				setIcon(invalidIcon);
				setToolTipText(invalidText);
			}
		}
	}
}
