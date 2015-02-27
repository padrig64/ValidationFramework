/*
 * Copyright (c) 2014, Patrick Moawad
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

import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.swing.utils.ColorUtils;

import javax.swing.JComponent;
import java.awt.Color;

public abstract class AbstractColorFeedback<RHI> implements ResultHandler<RHI> {

    private JComponent owner = null;
    private Color origForeground = null;
    private Color origBackground = null;
    private Color resultForeground = null;
    private Color resultBackground = null;
    private boolean showing = false;

    /**
     * Constructor specifying the component to attached to.
     * @param componentToBeColored Component to be colored to show the validation results.
     */
    public AbstractColorFeedback(final JComponent componentToBeColored) {
        attach(componentToBeColored);
    }

    /**
     * Attaches the feedback to the specified component.
     * @param componentToBeColored Component to be colored to show the validation results.
     */
    public void attach(final JComponent componentToBeColored) {
        detach();
        owner = componentToBeColored;
        showColors();
    }

    /**
     * Detaches the feedback from the previously attached component.
     */
    public void detach() {
        this.owner = null;
    }

    protected Color getForeground() {
        return resultForeground;
    }

    protected void setForeground(final Color foreground) {
        resultForeground = foreground;
    }

    protected Color getBackground() {
        return resultBackground;
    }

    protected void setBackground(final Color background) {
        resultBackground = background;
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
        if (owner.getParent() != null) {
            owner.getParent().repaint();
        }

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
