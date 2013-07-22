/*
 * Copyright (c) 2013, Patrick Moawad
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
import com.google.code.validationframework.swing.decoration.IconComponentDecoration;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * Abstract implementation of a result handler using an {@link IconComponentDecoration} to show feedback to the user.
 *
 * Concrete classes only need to implement the {@link #handleResult(Object)} method by calling the {@link #showIcon()}
 * and {@link #hideIcon()} methods according to the result.
 *
 * @param <RHI> Type of result handler input.
 */
public abstract class AbstractIconFeedback<RHI> implements ResultHandler<RHI>, Disposable {

    // TODO Make this dependent on the LAF
    private static final AnchorLink DEFAULT_ANCHOR_LINK_WITH_OWNER = new AnchorLink(new Anchor(0.0f, 3, 1.0f, -3),
            Anchor.CENTER);

    private IconComponentDecoration decoration = null;

    public AbstractIconFeedback(final JComponent decoratedComponent) {
        attach(decoratedComponent);
    }

    public void attach(final JComponent decoratedComponent) {
        attach(decoratedComponent, DEFAULT_ANCHOR_LINK_WITH_OWNER);
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
        AnchorLink anchorLink = null;
        if (decoration != null) {
            anchorLink = decoration.getAnchorLink();
        }
        return anchorLink;
    }

    /**
     * @see IconComponentDecoration#setAnchorLink(AnchorLink)
     */
    public void setAnchorLink(final AnchorLink anchorLink) {
        if (decoration != null) {
            decoration.setAnchorLink(anchorLink);
        }
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

    /**
     * @see IconComponentDecoration#setVisible(boolean)
     */
    protected void showIcon() {
        if (decoration != null) {
            decoration.setVisible(true);
        }
    }

    /**
     * @see IconComponentDecoration#setVisible(boolean)
     */
    protected void hideIcon() {
        if (decoration != null) {
            decoration.setVisible(false);
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
