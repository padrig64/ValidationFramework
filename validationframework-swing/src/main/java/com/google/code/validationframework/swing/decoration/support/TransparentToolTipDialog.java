/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.swing.decoration.support;

import com.google.code.validationframework.base.utils.ValueUtils;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
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

/**
 * Small window looking like a tooltip and becoming transparent/translucent upon rollover.
 *
 * It can be used, for instance, to show a kind of sticker next to a component.
 */
public class TransparentToolTipDialog extends JWindow {

    /**
     * Entity responsible for updating the location of the tooltip window based on the owner component.
     */
    private class LocationAdapter implements ComponentListener, AncestorListener {

        /**
         * @see ComponentListener#componentResized(ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            followOwner();
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            followOwner();
        }

        /**
         * @see ComponentListener#componentShown(ComponentEvent)
         */
        @Override
        public void componentShown(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see ComponentListener#componentHidden(ComponentEvent)
         */
        @Override
        public void componentHidden(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see AncestorListener#ancestorAdded(AncestorEvent)
         */
        @Override
        public void ancestorAdded(AncestorEvent event) {
            // Nothing to be done
        }

        /**
         * @see AncestorListener#ancestorRemoved(AncestorEvent)
         */
        @Override
        public void ancestorRemoved(AncestorEvent event) {
            // Nothing to be done
        }

        /**
         * @see AncestorListener#ancestorMoved(AncestorEvent)
         */
        @Override
        public void ancestorMoved(AncestorEvent event) {
            followOwner();
        }
    }

    /**
     * Entity responsible for animating the rollover effect.
     */
    private class TransparencyAdapter extends MouseAdapter implements TimingTarget {

        private static final float MIN_ALPHA = 0.25f;
        private static final float MAX_ALPHA = 1.0f;
        private static final float FADE_OUT_MAX_DURATION = 100;
        private static final float FADE_IN_MAX_DURATION = 55;

        private float currentAlpha = MAX_ALPHA;

        private Animator transparencyAnimator = null;

        /**
         * Constructor.
         */
        public TransparencyAdapter() {
            super();
            TimingSource ts = new SwingTimerTimingSource();
            Animator.setDefaultTimingSource(ts);
            ts.init();
        }

        /**
         * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            if (rolloverAnimated) {
                if ((transparencyAnimator != null) && (transparencyAnimator.isRunning())) {
                    transparencyAnimator.stop();
                }
                long duration = (long) ((currentAlpha - MIN_ALPHA) * FADE_OUT_MAX_DURATION / (MAX_ALPHA - MIN_ALPHA));
                if (duration <= 0) {
                    timingEvent(null, 0.0);
                } else {
                    transparencyAnimator = new Animator.Builder().setDuration(duration, TimeUnit.MILLISECONDS)
                            .setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(this).build();
                    transparencyAnimator.startReverse();
                }
            } else if (WindowUtils.isWindowAlphaSupported()) {
                WindowUtils.setWindowAlpha(TransparentToolTipDialog.this, MIN_ALPHA);
            }
        }

        /**
         * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(MouseEvent e) {
            if (rolloverAnimated) {
                if ((transparencyAnimator != null) && (transparencyAnimator.isRunning())) {
                    transparencyAnimator.stop();
                }

                long duration = (long) ((MAX_ALPHA - currentAlpha) * FADE_IN_MAX_DURATION / (MAX_ALPHA - MIN_ALPHA));
                if (duration <= 0) {
                    timingEvent(null, 1.0);
                } else {
                    transparencyAnimator = new Animator.Builder().setDuration(duration, TimeUnit.MILLISECONDS)
                            .setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(this).build();
                    transparencyAnimator.start();
                }
            } else if (WindowUtils.isWindowAlphaSupported()) {
                WindowUtils.setWindowAlpha(TransparentToolTipDialog.this, MAX_ALPHA);
            }
        }

        /**
         * @see TimingTarget#begin(Animator)
         */
        @Override
        public void begin(Animator animator) {
            // Nothing to be done because we stop the animation manually
        }

        /**
         * @see TimingTarget#end(Animator)
         */
        @Override
        public void end(Animator animator) {
            // Nothing to be done because we stop the animation manually
        }

        /**
         * @see TimingTarget#repeat(Animator)
         */
        @Override
        public void repeat(Animator animator) {
            // Nothing to be done
        }

        /**
         * @see TimingTarget#reverse(Animator)
         */
        @Override
        public void reverse(Animator animator) {
            // Nothing to be done
        }

        /**
         * @see TimingTarget#timingEvent(Animator, double)
         */
        @Override
        public void timingEvent(Animator animator, double v) {
            currentAlpha = (float) (v * (MAX_ALPHA - MIN_ALPHA)) + MIN_ALPHA;
            if (WindowUtils.isWindowAlphaSupported()) {
                WindowUtils.setWindowAlpha(TransparentToolTipDialog.this, currentAlpha);
            }
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

    /**
     * Component to stick the tooltip to.
     */
    private final JComponent owner;

    /**
     * Anchor link between the owner component (master) and the tooltip (slave).
     */
    private final AnchorLink anchorLink;

    /**
     * Internal tooltip component showing displaying the text.
     */
    private JToolTip toolTip = null;

    /**
     * Entity responsible for updating the location of the tooltip window based on the owner component.
     */
    private final LocationAdapter locationAdapter = new LocationAdapter();

    /**
     * Flag indicating whether the rollover should be animated or not.
     */
    private boolean rolloverAnimated = true;

    /**
     * Constructor.
     *
     * @param owner      Component to stick the tooltip to.
     * @param anchorLink Anchor link between the owner component (master) and the tooltip (slave).
     */
    public TransparentToolTipDialog(JComponent owner, AnchorLink anchorLink) {
        super(SwingUtilities.getWindowAncestor(owner));
        this.owner = owner;
        this.anchorLink = anchorLink;
        initComponents();
    }

    /**
     * Initializes the components of the tooltip window.
     */
    private void initComponents() {
        // Avoid warning on Mac OS X when changing the alpha
        getRootPane().putClientProperty("apple.awt.draggableWindowBackground", Boolean.FALSE);

        toolTip = new JToolTip();
        toolTip.addMouseListener(new TransparencyAdapter());

        owner.addComponentListener(locationAdapter);
        owner.addAncestorListener(locationAdapter);

        getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Just in case...
        setFocusable(false); // Just in case...
        setFocusableWindowState(false);
        setContentPane(toolTip);
        pack(); // Seems to help for the very first setVisible(true) when window transparency is on
    }

    /**
     * @see JWindow#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        setSize(toolTip.getPreferredSize());
        followOwner();

        if ((toolTip.getTipText() != null) && (!toolTip.getTipText().isEmpty())) {
            super.setVisible(visible);
        }
    }

    /**
     * @see JWindow#dispose()
     */
    @Override
    public void dispose() {
        owner.removeAncestorListener(locationAdapter);
        owner.removeComponentListener(locationAdapter);
        super.dispose();
    }

    /**
     * States whether the rollover state is animated.
     *
     * @return True if there is a rollover animation, false otherwise.
     */
    public boolean isRolloverAnimated() {
        return rolloverAnimated;
    }

    /**
     * State whether the rollover state should be animated.
     *
     * @param animated True to animate the rollover, false otherwise.
     */
    public void setRolloverAnimated(boolean animated) {
        this.rolloverAnimated = animated;
    }

    /**
     * Gets the text displayed as a tooltip.
     *
     * @return Displayed text.
     */
    public String getText() {
        return toolTip.getTipText();
    }

    /**
     * Sets the text to be displayed as a tooltip.
     *
     * @param text Text to be displayed
     */
    public void setText(String text) {
        // Only change if different
        if (!ValueUtils.areEqual(text, toolTip.getTipText())) {

            boolean wasVisible = isVisible();
            if (wasVisible) {
                setVisible(false);
            }

            toolTip.setTipText(text);

            if (wasVisible) {
                setVisible(true);
            }
        }
    }

    /**
     * Updates the location of the window based on the location of the owner component.
     */
    private void followOwner() {
        if (owner.isVisible() && owner.isShowing()) {
            try {
                Point screenLocation = owner.getLocationOnScreen();
                Point relativeSlaveLocation = anchorLink.getRelativeSlaveLocation(owner.getSize(),
                        TransparentToolTipDialog.this.getSize());
                setLocation(screenLocation.x + relativeSlaveLocation.x, screenLocation.y + relativeSlaveLocation.y);
            } catch (IllegalComponentStateException e) {
                LOGGER.error("Failed getting location of component: " + owner, e);
            }
        }
    }
}
