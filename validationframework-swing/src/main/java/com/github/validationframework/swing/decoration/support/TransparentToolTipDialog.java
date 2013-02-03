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

package com.github.validationframework.swing.decoration.support;

import com.github.validationframework.base.utils.ValueUtils;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import com.sun.jna.platform.WindowUtils;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;

public class TransparentToolTipDialog extends JWindow {

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

    private class TransparencyAdapter extends MouseAdapter implements TimingTarget {

        private static final float MIN_ALPHA = 0.25f;
        private static final float MAX_ALPHA = 1.0f;
        private static final float FADE_OUT_MAX_DURATION = 100;
        private static final float FADE_IN_MAX_DURATION = 55;

        private float currentAlpha = MAX_ALPHA;

        private Animator transparencyAnimator = null;

        public TransparencyAdapter() {
            super();
            final TimingSource ts = new SwingTimerTimingSource();
            Animator.setDefaultTimingSource(ts);
            ts.init();
        }

        /**
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(final MouseEvent e) {
            if (isRolloverAnimated()) {
                if ((transparencyAnimator != null) && (transparencyAnimator.isRunning())) {
                    transparencyAnimator.stop();
                }
                final long duration = (long) ((currentAlpha - MIN_ALPHA) * FADE_OUT_MAX_DURATION / (MAX_ALPHA -
                        MIN_ALPHA));
                if (duration <= 0) {
                    timingEvent(null, 0.0);
                } else {
                    transparencyAnimator = new Animator.Builder().setDuration(duration,
                            TimeUnit.MILLISECONDS).setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2,
                            0.8)).addTarget(this).build();
                    transparencyAnimator.startReverse();
                }
            } else {
                WindowUtils.setWindowAlpha(TransparentToolTipDialog.this, MIN_ALPHA);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(final MouseEvent e) {
            if (isRolloverAnimated()) {
                if ((transparencyAnimator != null) && (transparencyAnimator.isRunning())) {
                    transparencyAnimator.stop();
                }

                final long duration = (long) ((MAX_ALPHA - currentAlpha) * FADE_IN_MAX_DURATION / (MAX_ALPHA -
                        MIN_ALPHA));
                if (duration <= 0) {
                    timingEvent(null, 1.0);
                } else {
                    transparencyAnimator = new Animator.Builder().setDuration(duration,
                            TimeUnit.MILLISECONDS).setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2,
                            0.8)).addTarget(this).build();
                    transparencyAnimator.start();
                }
            } else {
                WindowUtils.setWindowAlpha(TransparentToolTipDialog.this, MAX_ALPHA);
            }
        }

        /**
         * @see TimingTarget#begin(Animator)
         */
        @Override
        public void begin(final Animator animator) {
            // Nothing to be done because we stop the animation manually
        }

        /**
         * @see TimingTarget#end(Animator)
         */
        @Override
        public void end(final Animator animator) {
            // Nothing to be done because we stop the animation manually
        }

        /**
         * @see TimingTarget#repeat(Animator)
         */
        @Override
        public void repeat(final Animator animator) {
            // Nothing to be done
        }

        /**
         * @see TimingTarget#reverse(Animator)
         */
        @Override
        public void reverse(final Animator animator) {
            // Nothing to be done
        }

        /**
         * @see TimingTarget#timingEvent(Animator, double)
         */
        @Override
        public void timingEvent(final Animator animator, final double v) {
            currentAlpha = (float) (v * (MAX_ALPHA - MIN_ALPHA)) + MIN_ALPHA;
            WindowUtils.setWindowAlpha(TransparentToolTipDialog.this, currentAlpha);
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -8703799877460319097L;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TransparentToolTipDialog.class);

    private final JComponent owner;

    private JToolTip toolTip = null;

    private final AnchorLink anchorLink;

    private final LocationAdapter locationAdapter = new LocationAdapter();

    private boolean rolloverAnimated = true;

    public TransparentToolTipDialog(final JComponent owner, final AnchorLink anchorLink) {
        super(SwingUtilities.getWindowAncestor(owner));
        this.owner = owner;
        this.anchorLink = anchorLink;
        initComponents();
    }

    private void initComponents() {
        toolTip = new JToolTip();
        toolTip.addMouseListener(new TransparencyAdapter());

        owner.addComponentListener(locationAdapter);
        owner.addAncestorListener(locationAdapter);

        getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Just in case...
        setFocusable(false); // Just in case...
        setFocusableWindowState(false);
        setContentPane(toolTip);
        pack(); // Seems to help for the very first setVisible(true) when window transparency is on

        if (WindowUtils.isWindowAlphaSupported()) {
            try {
                WindowUtils.setWindowTransparent(this, true);
            } catch (IllegalArgumentException e) {
                LOGGER.warn("Transparency reported as being supported, but it just does not work", e);
            }
        }
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
                        TransparentToolTipDialog.this.getSize());
                setLocation(screenLocation.x + relativeSlaveLocation.x, screenLocation.y + relativeSlaveLocation.y);
            } catch (IllegalComponentStateException e) {
                LOGGER.error("Failed getting location of component: " + owner, e);
            }
        }
    }
}
