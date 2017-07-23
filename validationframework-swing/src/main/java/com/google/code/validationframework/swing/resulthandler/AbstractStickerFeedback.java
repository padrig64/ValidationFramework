/*
 * Copyright (c) 2017, ValidationFramework Authors
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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
import com.google.code.validationframework.swing.decoration.support.TransparentToolTipDialog;

import javax.swing.JComponent;

/**
 * Abstract implementation of a sticker feedback showing a permanent tooltip on the right side of a component.
 *
 * @param <RHI> Type of validation result.
 */
public abstract class AbstractStickerFeedback<RHI> implements ResultHandler<RHI>, Disposable {

    /**
     * Tooltip sticker following the owner component.
     */
    private TransparentToolTipDialog toolTipDialog = null;

    /**
     * Constructor attaching the tooltip sticker to the specified component.
     *
     * @param owner Attaches the tooltip sticker to the specified component.
     */
    public AbstractStickerFeedback(JComponent owner) {
        attach(owner);
    }

    /**
     * Attaches the tooltip sticker to the specified component.
     *
     * @param owner Component to attach to.
     */
    public void attach(JComponent owner) {
        detach();
        toolTipDialog = new TransparentToolTipDialog(owner, new AnchorLink(Anchor.CENTER_RIGHT, Anchor.CENTER_LEFT));
    }

    /**
     * Hides and detaches the tooltip sticker from the owner component.
     */
    public void detach() {
        if (toolTipDialog != null) {
            toolTipDialog.dispose();
            toolTipDialog = null;
        }
    }

    /**
     * Gets the tooltip text to be displayed.
     *
     * @return Text displaued as a tooltip.
     */
    protected String getToolTipText() {
        String tip = null;

        if (toolTipDialog != null) {
            tip = toolTipDialog.getText();
        }

        return tip;
    }

    /**
     * Sets the tooltip text to be displayed.
     *
     * @param text Text to be displayed as a tooltip.
     */
    protected void setToolTipText(String text) {
        if (toolTipDialog != null) {
            toolTipDialog.setText(text);
        }
    }

    /**
     * Shows the tooltip sticker.
     */
    protected void showToolTip() {
        if (toolTipDialog != null) {
            toolTipDialog.setVisible(true);
        }
    }

    /**
     * Hides the tooltip sticker.
     */
    protected void hideToolTip() {
        if (toolTipDialog != null) {
            toolTipDialog.setVisible(false);
        }
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        detach();
    }
}
