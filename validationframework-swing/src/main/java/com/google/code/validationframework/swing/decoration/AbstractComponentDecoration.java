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

package com.google.code.validationframework.swing.decoration;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.Beans;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Abstract implementation of a decoration that can be attached to a component.
 * <p>
 * Concrete implementations will just need to provide the size and do the painting on the already-computed location.
 */
public abstract class AbstractComponentDecoration implements Disposable {

    /**
     * Entity responsible of tracking the changes on the decorated component and/or its ancestors that would require to
     * update the location and/or the clip bounds of the decoration.
     */
    private final class ComponentTracker implements AncestorListener, HierarchyBoundsListener, ComponentListener,
            HierarchyListener, PropertyChangeListener {

        /**
         * @see AncestorListener#ancestorAdded(AncestorEvent)
         */
        @Override
        public void ancestorAdded(AncestorEvent event) {
            followDecoratedComponent();
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
            followDecoratedComponent();
        }

        /**
         * @see HierarchyBoundsListener#ancestorMoved(HierarchyEvent)
         */
        @Override
        public void ancestorMoved(HierarchyEvent e) {
            followDecoratedComponent();
        }

        /**
         * @see HierarchyBoundsListener#ancestorResized(HierarchyEvent)
         */
        @Override
        public void ancestorResized(HierarchyEvent e) {
            followDecoratedComponent();
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            followDecoratedComponent();
        }

        /**
         * @see ComponentListener#componentResized(ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            followDecoratedComponent();
        }

        /**
         * @see ComponentListener#componentShown(ComponentEvent)
         */
        @Override
        public void componentShown(ComponentEvent e) {
            followDecoratedComponent();
        }

        /**
         * @see ComponentListener#componentHidden(ComponentEvent)
         */
        @Override
        public void componentHidden(ComponentEvent e) {
            // Nothing to be done
        }

        /**
         * @see HierarchyListener#hierarchyChanged(HierarchyEvent)
         */
        @Override
        public void hierarchyChanged(HierarchyEvent e) {
            if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0) {
                updateDecorationPainterVisibility();
            }
        }

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
            if ("enabled".equals(propertyChangeEvent.getPropertyName())) {
                updateDecorationPainterVisibility();
            } else if ("ancestor".equals(propertyChangeEvent.getPropertyName())) {
                // Make sure the decoration painter has a peer (otherwise it will never be painted)
                attachToLayeredPane();
            }
        }
    }

    /**
     * Entity responsible of calling the {@link AbstractComponentDecoration#paint(Graphics)} method.
     * <p>
     * It works as a hook in the Swing painting mechanism.
     * <p>
     * Note that the painter is made invisible whenever the decorated component is no longer showing on the screen, so
     * that the decoration does not try to paint when the component is on a hidden tab, for instance, and does not steal
     * the mouse events from other decorations at the same location on other tabs.
     *
     * @see #updateDecorationPainterVisibility()
     */
    protected class DecorationPainter extends JComponent {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = 7573896845503780433L;

        /**
         * Clipping bounds of the decoration.
         * <p>
         * The decoration can be clipped by scrollpane viewports, parent containers, etc..
         */
        private Rectangle clipBounds = null;

        /**
         * Default constructor.
         */
        public DecorationPainter() {
            super();
            setFocusable(false);
            setOpaque(false);

            // Quick workaround for use in JFormDesigner
            setVisible(!Beans.isDesignTime());
        }

        /**
         * @see JComponent#setVisible(boolean)
         */
        @Override
        public void setVisible(boolean visible) {
            // Quick workaround for use in JFormDesigner
            super.setVisible(visible && !Beans.isDesignTime());
        }

        /**
         * Gets the clipping bounds of the decoration.
         *
         * @return Clipping bounds.
         */
        public Rectangle getClipBounds() {
            return clipBounds;
        }

        /**
         * Sets the clipping bounds of the decoration.
         *
         * @param clipBounds Clipping bounds.
         */
        public void setClipBounds(Rectangle clipBounds) {
            this.clipBounds = clipBounds;
        }

        /**
         * Returns the width of the decoration.
         *
         * @return Decoration width.
         */
        @Override
        public int getWidth() {
            // Implementation in sub-classes
            return AbstractComponentDecoration.this.getWidth();
        }

        /**
         * Returns the height of the decoration.
         *
         * @return Decoration height.
         */
        @Override
        public int getHeight() {
            // Implementation in sub-classes
            return AbstractComponentDecoration.this.getHeight();
        }

        /**
         * Calls the method {@link AbstractComponentDecoration#paint(Graphics)} of the decoration.
         *
         * @param g Graphics to paint on.
         *
         * @see AbstractComponentDecoration#paint(Graphics)
         */
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (isVisible() && (decoratedComponent != null) && decoratedComponent.isShowing() && areBoundsValid
                    (clipBounds)) {
                // Clip graphics
                g.setClip(clipBounds);

                // Paint decoration (implementation in sub-classes)
                AbstractComponentDecoration.this.paint(g);
            }
        }

        /**
         * Checks whether the specified clipping bounds can be used for painting.
         * <p>
         * If the bounds cannot be used, it is not necessary to paint the decoration.
         *
         * @param bounds Clipping bounds to be checked.
         *
         * @return True if the bounds can be used, false otherwise.
         */
        private boolean areBoundsValid(Rectangle bounds) {
            return (bounds != null) && (bounds.width > 0) && (bounds.height > 0);
        }
    }

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractComponentDecoration.class);

    /**
     * Offset in the layer of the layered pane where the decoration is to be put.
     * <p>
     * The decoration will be added to the layered pane at the same layer index as the decorated component, incremented
     * by this offset.
     *
     * @see #getDecoratedComponentLayerInLayeredPane
     */
    private static final int DECORATION_LAYER_OFFSET = 1;

    /**
     * Decorated component on which the decoration is to be attached.
     */
    private JComponent decoratedComponent;

    /**
     * Ancestor component that will be used to determine the clipping bounds of the decoration.
     * <p>
     * If the default null value is specified, the parent component of the decorated component will be used.
     */
    private JComponent clippingAncestor = null;

    /**
     * Anchor link between the decorated component and its decoration.
     */
    private AnchorLink anchorLink;

    /**
     * Listener to decorated component changes that would affect properties of the decoration attached to it (for
     * instance, size, location, etc.).
     */
    private final ComponentTracker decoratedComponentTracker = new ComponentTracker();

    /**
     * Decoration painter component.
     * <p>
     * It is merely a hook into the Swing painting mechanism.
     * <p>
     * This is the component that is actually added to the layered pane.
     *
     * @see #DECORATION_LAYER_OFFSET
     * @see #attach(JComponent)
     * @see #getDecoratedComponentLayerInLayeredPane
     * @see #decorationPainter
     */
    protected final DecorationPainter decorationPainter = new DecorationPainter();

    /**
     * Layered pane to which the decoration painter will be added.
     * <p>
     * The decoration will actually contain a component responsible of painting the decoration.
     *
     * @see #decorationPainter
     */
    private JLayeredPane attachedLayeredPane = null;

    /**
     * Flag indicating whether the decoration is visible or not.
     * <p>
     * Note that it does not mean that it is showing on the screen.
     *
     * @see #isVisible()
     * @see #setVisible(boolean)
     * @see ComponentTracker#hierarchyChanged(HierarchyEvent)
     */
    private boolean visible = true;

    /**
     * Flag indicating whether the decoration is to be painted (shown) even when the decorated component is disabled.
     *
     * @see #updateDecorationPainterVisibility()
     */
    private boolean paintWhenDisabled = true;

    /**
     * Constructor specifying the component to be decorated and the anchor link between the decorated component and its
     * decoration.
     *
     * @param decoratedComponent Component to be decorated.
     * @param anchorLink         Anchor link between the decorated component and its decoration.
     */
    public AbstractComponentDecoration(JComponent decoratedComponent, AnchorLink anchorLink) {
        this.anchorLink = anchorLink;
        attach(decoratedComponent);
    }

    /**
     * Constructor specifying the component to be decorated and the two anchors composing the anchor link between the
     * decorated component and its decoration.
     *
     * @param decoratedComponent       Component to be decorated.
     * @param decoratedComponentAnchor Anchor on the decorated component.
     * @param decorationAnchor         Anchor on the decoration.
     *
     * @see AbstractComponentDecoration#AbstractComponentDecoration(JComponent, AnchorLink)
     */
    public AbstractComponentDecoration(JComponent decoratedComponent, Anchor decoratedComponentAnchor, Anchor
            decorationAnchor) {
        this(decoratedComponent, new AnchorLink(decoratedComponentAnchor, decorationAnchor));
    }

    /**
     * Attaches the decoration to the specified component.
     *
     * @param componentToBeDecorated Component to be decorated.
     */
    private void attach(JComponent componentToBeDecorated) {
        detach();

        decoratedComponent = componentToBeDecorated;

        if (decoratedComponent != null) {
            decoratedComponent.addComponentListener(decoratedComponentTracker);
            decoratedComponent.addAncestorListener(decoratedComponentTracker);
            decoratedComponent.addHierarchyBoundsListener(decoratedComponentTracker);
            decoratedComponent.addHierarchyListener(decoratedComponentTracker);
            decoratedComponent.addPropertyChangeListener("enabled", decoratedComponentTracker);
            decoratedComponent.addPropertyChangeListener("ancestor", decoratedComponentTracker);

            attachToLayeredPane();
        }
    }

    /**
     * Detaches the decoration from the decorated component.
     */
    private void detach() {
        // Do not call setVisible(false) here: that would make it invisible by default (detach() is called in attach())

        if (decoratedComponent != null) {
            decoratedComponent.removeComponentListener(decoratedComponentTracker);
            decoratedComponent.removeAncestorListener(decoratedComponentTracker);
            decoratedComponent.removeHierarchyBoundsListener(decoratedComponentTracker);
            decoratedComponent.removeHierarchyListener(decoratedComponentTracker);
            decoratedComponent.removePropertyChangeListener("enabled", decoratedComponentTracker);
            decoratedComponent.removePropertyChangeListener("ancestor", decoratedComponentTracker);
            decoratedComponent = null;

            detachFromLayeredPane();
        }
    }

    /**
     * Inserts the decoration to the layered pane right above the decorated component.
     */
    private void attachToLayeredPane() {
        // Get ancestor layered pane that will get the decoration holder component
        Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, decoratedComponent);
        if (ancestor instanceof JLayeredPane) {
            attachedLayeredPane = (JLayeredPane) ancestor;
            Integer layer = getDecoratedComponentLayerInLayeredPane(attachedLayeredPane);
            attachedLayeredPane.remove(decorationPainter);
            attachedLayeredPane.add(decorationPainter, layer);
        } else {
            // Remove decoration painter from previous layered pane as this could lead to memory leak
            detachFromLayeredPane();
        }
    }

    /**
     * Retrieves the layer index of the decorated component in the layered pane of the window.
     *
     * @param layeredPane Layered pane of the window.
     *
     * @return Index of the decorated component in the layered pane.
     */
    private Integer getDecoratedComponentLayerInLayeredPane(JLayeredPane layeredPane) {
        Container ancestorInLayer = decoratedComponent;
        while (!layeredPane.equals(ancestorInLayer.getParent())) {
            ancestorInLayer = ancestorInLayer.getParent();
        }

        return (layeredPane.getLayer(ancestorInLayer) + DECORATION_LAYER_OFFSET);
    }

    /**
     * Removes the decoration from the layered pane.
     */
    private void detachFromLayeredPane() {
        if (attachedLayeredPane != null) {
            attachedLayeredPane.remove(decorationPainter);
            attachedLayeredPane = null;
        }
    }

    /**
     * Gets the decorated component to which the decoration is attached.
     *
     * @return Decorated component.
     */
    public JComponent getDecoratedComponent() {
        return decoratedComponent;
    }

    /**
     * Gets the custom clipping ancestor component that will be used to clip the decoration.
     * <p>
     * If null is returned, the parent container of the decorated component is used.
     *
     * @return Clipping ancestor or null.
     */
    public JComponent getClippingAncestor() {
        return clippingAncestor;
    }

    /**
     * Sets the custom clipping ancestor component that will be used to clip the decoration.
     * <p>
     * If set to null, the parent container of the decorated component will be used.
     * <p>
     * Note that the specified clipping component shall be an ancestor of the decorated component, or the component
     * itself.
     *
     * @param decorationClippingAncestor Clipping ancestor or null to
     */
    public void setClippingAncestor(JComponent decorationClippingAncestor) {
        clippingAncestor = decorationClippingAncestor;
        followDecoratedComponent();
    }

    /**
     * Gets the effective clipping ancestor.
     * <p>
     * If no custom clipping ancestor is set, the parent container of the decorated component will be returned.
     *
     * @return Effective clipping ancestor.
     */
    private JComponent getEffectiveClippingAncestor() {
        JComponent clippingComponent = clippingAncestor;

        if ((clippingComponent == null) && (decoratedComponent != null)) {
            // No specific clipping ancestor specified by the programmer, so try to find one

            // Keep a reference to the best alternative candidate if no other proper ancestor can be found
            Container lastNonNullParent = decoratedComponent;

            // Look for the first viewport or layered pane in the component hierarchy tree
            Container parent = decoratedComponent.getParent();
            while ((parent != null) && (clippingComponent == null)) {
                lastNonNullParent = parent;
                if ((parent instanceof JViewport) || (parent instanceof JLayeredPane)) {
                    clippingComponent = (JComponent) parent;
                }
                parent = parent.getParent();
            }

            if (clippingComponent == null) {
                // This may be a normal case depending on the application logic to hide a panel (tabbed pane like)
                if (lastNonNullParent instanceof JComponent) {
                    clippingComponent = (JComponent) lastNonNullParent;
                }
            }
        }

        return clippingComponent;
    }

    /**
     * Gets the anchor link between the decorated component and its decoration.
     *
     * @return Anchor link between the component and its decoration.
     */
    public AnchorLink getAnchorLink() {
        return anchorLink;
    }

    /**
     * Sets the anchor link between the component and its decoration.
     *
     * @param anchorLink Anchor link between the component and its decoration.
     */
    public void setAnchorLink(AnchorLink anchorLink) {
        this.anchorLink = anchorLink;
        followDecoratedComponent();
    }

    /**
     * States whether the decoration is visible or not.
     *
     * @return True if the decoration is visible, false otherwise.
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets the visibility of the decoration.
     *
     * @param visible True to make the decoration visible, false to make it invisible.
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        updateDecorationPainterVisibility();
    }

    /**
     * States whether the decoration is painted (shown) even when the decorated component is disabled.
     *
     * @return True if the decoration is painted (shown) even when the decorated component is disabled, false otherwise.
     */
    public boolean getPaintWhenDisabled() {
        return paintWhenDisabled;
    }

    /**
     * States whether the decoration is to be painted (shown) even when the decorated component is disabled.
     *
     * @param paintWhenDisabled True if decoration is to be painted (shown) even when the decorated component is
     *                          disabled, false otherwise.
     */
    public void setPaintWhenDisabled(boolean paintWhenDisabled) {
        this.paintWhenDisabled = paintWhenDisabled;
        updateDecorationPainterVisibility();
    }

    /**
     * Updates the visibility of the decoration painter according to the visible state set by the programmer and the
     * state of the decorated component.
     *
     * @see DecorationPainter
     */
    private void updateDecorationPainterVisibility() {
        boolean shouldBeVisible = (decoratedComponent != null) && //
                (paintWhenDisabled || decoratedComponent.isEnabled()) && //
                decoratedComponent.isShowing() && //
                visible;
        if (shouldBeVisible != decorationPainter.isVisible()) {
            decorationPainter.setVisible(shouldBeVisible);
        }
    }

    /**
     * Updates the decoration painter with respect to the decorated component.
     * <p>
     * This method is to be called whenever changes on the decorated component have an impact on the decoration (for
     * instance, its size, location, etc.).
     * <p>
     * This method has been made protected so that it can be easily called from the implementing sub-classes.
     */
    protected void followDecoratedComponent() {
        if ((anchorLink != null) && (decoratedComponent != null)) {
            if (attachedLayeredPane == null) {
                // Try to attach to a layered pane
                attachToLayeredPane();
            }
            followDecoratedComponent(attachedLayeredPane);
        }
    }

    /**
     * Updates the decoration painter in the specified layered pane.
     *
     * @param layeredPane Layered pane containing the decoration.
     *
     * @see #followDecoratedComponent()
     */
    private void followDecoratedComponent(JLayeredPane layeredPane) {
        Point relativeLocationToOwner = anchorLink.getRelativeSlaveLocation(decoratedComponent.getWidth(),
                decoratedComponent.getHeight(), getWidth(), getHeight());

        updateDecorationPainterUnclippedBounds(layeredPane, relativeLocationToOwner);
        updateDecorationPainterClippedBounds(layeredPane, relativeLocationToOwner);

        // Repaint decoration
        if (layeredPane != null) {
            decorationPainter.revalidate();
            decorationPainter.repaint();
        }
    }

    /**
     * Calculates and updates the unclipped bounds of the decoration painter in layered pane coordinates.
     *
     * @param layeredPane             Layered pane containing the decoration painter.
     * @param relativeLocationToOwner Location of the decoration painter relatively to the decorated component.
     */
    private void updateDecorationPainterUnclippedBounds(JLayeredPane layeredPane, Point relativeLocationToOwner) {
        Rectangle decorationBoundsInLayeredPane;

        if (layeredPane == null) {
            decorationBoundsInLayeredPane = new Rectangle();
        } else {
            // Calculate location of the decorated component in the layered pane containing the decoration painter
            Point decoratedComponentLocationInLayeredPane = SwingUtilities.convertPoint(decoratedComponent.getParent
                    (), decoratedComponent.getLocation(), layeredPane);

            // Deduces the location of the decoration painter in the layered pane
            decorationBoundsInLayeredPane = new Rectangle(decoratedComponentLocationInLayeredPane.x +
                    relativeLocationToOwner.x, decoratedComponentLocationInLayeredPane.y + relativeLocationToOwner.y,
                    getWidth(), getHeight());
        }

        // Update decoration painter
        decorationPainter.setBounds(decorationBoundsInLayeredPane);
    }

    /**
     * Calculates and updates the clipped bounds of the decoration painter in layered pane coordinates.
     *
     * @param layeredPane             Layered pane containing the decoration painter.
     * @param relativeLocationToOwner Location of the decoration painter relatively to the decorated component.
     */
    private void updateDecorationPainterClippedBounds(JLayeredPane layeredPane, Point relativeLocationToOwner) {
        if (layeredPane == null) {
            decorationPainter.setClipBounds(null);
        } else {
            JComponent clippingComponent = getEffectiveClippingAncestor();
            if (clippingComponent == null) {
                LOGGER.error("No decoration clipping component can be found for decorated component: " +
                        decoratedComponent);
                decorationPainter.setClipBounds(null);
            } else if (clippingComponent.isShowing()) {
                Rectangle ownerBoundsInParent = decoratedComponent.getBounds();
                Rectangle decorationBoundsInParent = new Rectangle(ownerBoundsInParent.x + relativeLocationToOwner.x,
                        ownerBoundsInParent.y + relativeLocationToOwner.y, getWidth(), getHeight());
                Rectangle decorationBoundsInAncestor = SwingUtilities.convertRectangle(decoratedComponent.getParent()
                        , decorationBoundsInParent, clippingComponent);
                Rectangle decorationVisibleBoundsInAncestor;
                Rectangle ancestorVisibleRect = clippingComponent.getVisibleRect();
                decorationVisibleBoundsInAncestor = ancestorVisibleRect.intersection(decorationBoundsInAncestor);

                if ((decorationVisibleBoundsInAncestor.width == 0) || (decorationVisibleBoundsInAncestor.height == 0)) {
                    // No bounds, no painting
                    decorationPainter.setClipBounds(null);
                } else {
                    Rectangle decorationVisibleBoundsInLayeredPane = SwingUtilities.convertRectangle
                            (clippingComponent, decorationVisibleBoundsInAncestor, layeredPane);

                    // Clip graphics context
                    Rectangle clipBounds = SwingUtilities.convertRectangle(decorationPainter.getParent(),
                            decorationVisibleBoundsInLayeredPane, decorationPainter);
                    decorationPainter.setClipBounds(clipBounds);
                }
            } else {
                // This could happen for example when a dialog is closed, so no need to log anything
                decorationPainter.setClipBounds(null);
            }
        }
    }

    /**
     * @see Disposable
     */
    @Override
    public void dispose() {
        detach();
    }

    /**
     * Triggers a repaint of the component decoration.
     * <p>
     * Note that this will also make the decoration follow the component.
     *
     * @see #followDecoratedComponent()
     */
    public void followAndRepaint() {
        followDecoratedComponent();
    }

    /**
     * Returns the width of the decoration.
     *
     * @return Unclipped width of the decoration.
     */
    protected abstract int getWidth();

    /**
     * Returns the height of the decoration.
     *
     * @return Unclipped height of the decoration.
     */
    protected abstract int getHeight();

    /**
     * Paints the decoration in the specified graphics.
     *
     * @param g Graphics to paint the decoration to.
     */
    public abstract void paint(Graphics g);
}
