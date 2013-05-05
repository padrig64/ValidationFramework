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

package com.github.validationframework.swing.decoration.support;

import com.github.validationframework.base.utils.ValueUtils;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JRootPane;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.IllegalComponentStateException;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ToolTipDialog extends JWindow {

    private class LocationAdapter implements ComponentListener, AncestorListener {

        @Override
        public void componentResized(final ComponentEvent e) {
            followOwner();
        }

        @Override
        public void componentMoved(final ComponentEvent e) {
            followOwner();
        }

        @Override
        public void componentShown(final ComponentEvent e) {
            // Nothing to be done
        }

        @Override
        public void componentHidden(final ComponentEvent e) {
            // Nothing to be done
        }

        @Override
        public void ancestorAdded(final AncestorEvent event) {
            // Nothing to be done
        }

        @Override
        public void ancestorRemoved(final AncestorEvent event) {
            // Nothing to be done
        }

        @Override
        public void ancestorMoved(final AncestorEvent event) {
            followOwner();
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -8703799877460319097L;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ToolTipDialog.class);

    private final JComponent owner;

    private JToolTip toolTip = null;

    private final AnchorLink anchorLink;

    private final LocationAdapter locationAdapter = new LocationAdapter();

    private boolean rolloverAnimated = true;

    public ToolTipDialog(final JComponent owner, final AnchorLink anchorLink) {
        super(SwingUtilities.getWindowAncestor(owner));
        this.owner = owner;
        this.anchorLink = anchorLink;
        initComponents();
    }

    private void initComponents() {
        toolTip = new JToolTip();

        owner.addComponentListener(locationAdapter);
        owner.addAncestorListener(locationAdapter);

        getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Just in case...
        setFocusable(false); // Just in case...
        setFocusableWindowState(false);
        setContentPane(toolTip);
    }

    @Override
    public void setVisible(final boolean visible) {
        setSize(toolTip.getPreferredSize());
        followOwner();

        if ((toolTip.getTipText() != null) && (!toolTip.getTipText().isEmpty())) {
            super.setVisible(visible);
        }
    }

    @Override
    public void dispose() {
        owner.removeAncestorListener(locationAdapter);
        owner.removeComponentListener(locationAdapter);
        super.dispose();
    }

    public boolean isRolloverAnimated() {
        return rolloverAnimated;
    }

    public void setRolloverAnimated(final boolean animated) {
        this.rolloverAnimated = animated;
    }

    public String getText() {
        return toolTip.getTipText();
    }

    public void setText(final String text) {
        // Only change if different
        if (!ValueUtils.areEqual(text, toolTip.getTipText())) {

            final boolean wasVisible = isVisible();
            if (wasVisible) {
                setVisible(false);
            }

            toolTip.setTipText(text);

            if (wasVisible) {
                setVisible(wasVisible);
            }
        }
    }

    private void followOwner() {
        if (owner.isVisible() && owner.isShowing()) {
            try {
                final Point screenLocation = owner.getLocationOnScreen();
                final Point relativeSlaveLocation = anchorLink.getRelativeSlaveLocation(owner.getSize(),
                        ToolTipDialog.this.getSize());
                setLocation(screenLocation.x + relativeSlaveLocation.x, screenLocation.y + relativeSlaveLocation.y);
            } catch (IllegalComponentStateException e) {
                LOGGER.error("Failed getting location of component: " + owner, e);
            }
        }
    }
}
