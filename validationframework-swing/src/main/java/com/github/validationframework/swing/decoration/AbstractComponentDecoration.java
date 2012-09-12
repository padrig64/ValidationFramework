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

package com.github.validationframework.swing.decoration;

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Abstract implementation of a decoration that can be attached to a component.<br>Concrete implementations will just
 * need to provide the size and do the painting on the already-computed location.
 */
public abstract class AbstractComponentDecoration implements Disposable {

	/**
	 * Entity responsible of tracking the changes on the decorated component and/or its ancestors that would require to
	 * update the location and/or the clip bounds of the decoration.
	 */
	private final class ComponentTracker implements AncestorListener, HierarchyBoundsListener, ComponentListener,
			HierarchyListener {

		/**
		 * @see AncestorListener#ancestorAdded(AncestorEvent)
		 */
		@Override
		public void ancestorAdded(final AncestorEvent event) {
			followDecoratedComponent();
		}

		/**
		 * @see AncestorListener#ancestorRemoved(AncestorEvent)
		 */
		@Override
		public void ancestorRemoved(final AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorMoved(final HierarchyEvent e) {
			followDecoratedComponent();
		}

		@Override
		public void ancestorResized(final HierarchyEvent e) {
			followDecoratedComponent();
		}

		/**
		 * @see AncestorListener#ancestorMoved(AncestorEvent)
		 */
		@Override
		public void ancestorMoved(final AncestorEvent event) {
			followDecoratedComponent();
		}

		/**
		 * @see ComponentListener#componentMoved(ComponentEvent)
		 */
		@Override
		public void componentMoved(final ComponentEvent e) {
			followDecoratedComponent();
		}

		/**
		 * @see ComponentListener#componentResized(ComponentEvent)
		 */
		@Override
		public void componentResized(final ComponentEvent e) {
			followDecoratedComponent();
		}

		/**
		 * @see ComponentListener#componentShown(ComponentEvent)
		 */
		@Override
		public void componentShown(final ComponentEvent e) {
			followDecoratedComponent();
		}

		/**
		 * @see ComponentListener#componentHidden(ComponentEvent)
		 */
		@Override
		public void componentHidden(final ComponentEvent e) {
			// Nothing to be done
		}

		@Override
		public void hierarchyChanged(final HierarchyEvent e) {
			if ((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0 ) {
				decorationPainter.setVisible(getDecoratedComponent().isShowing());
			}
		}
	}

	/**
	 * Entity responsible of calling the {@link AbstractComponentDecoration#paint(Graphics)} method.<br>It is a hook in the
	 * Swing paint mechanism.
	 */
	protected class DecorationPainter extends JComponent {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = 7573896845503780433L;

		private Rectangle clipBounds = null;

		/**
		 * Default constructor.
		 */
		public DecorationPainter() {
			setFocusable(false);
		}

		public Rectangle getClipBounds() {
			return clipBounds;
		}

		public void setClipBounds(final Rectangle clipBounds) {
			this.clipBounds = clipBounds;
		}

		/**
		 * Returns the width of the decoration.
		 *
		 * @return Decoration width.
		 */
		@Override
		public int getWidth() {
			return AbstractComponentDecoration.this.getWidth();
		}

		/**
		 * Returns the height of the decoration.
		 *
		 * @return Decoration height.
		 */
		@Override
		public int getHeight() {
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
		public void paintComponent(final Graphics g) {
			if (decoratedComponent.isShowing() && isVisible() &&
					(clipBounds != null) &&
					(clipBounds.width > 0) && (clipBounds.height > 0)) {
				// Clip graphics
				g.setClip(clipBounds);

				// Paint decoration
				AbstractComponentDecoration.this.paint(g);
			}
		}
	}

	/**
	 * Offset in the layer of the layered pane where the decoration is to be put.<br>The decoration will be added to the
	 * layered pane at the same layer index as the decorated component, incremented by this offset.
	 *
	 * @see #getDecoratedComponentLayerInLayeredPane
	 */
	private static final int DECORATION_LAYER_OFFSET = 1;

	/**
	 * Decorated component on which the decoration is to be attached.
	 */
	private JComponent decoratedComponent;

	/**
	 * Ancestor component that will be used to determine the clipping bounds of the decoration.<br>If the default null
	 * value is specified, the parent component of the decorated component will be used.
	 */
	private JComponent decorationClippingAncestor = null;

	/**
	 * Anchor link between the decorated component and its decoration.
	 */
	private AnchorLink anchorLink;

	/**
	 * Listener to decorated component changes that would affect properties of the decoration attached to it (for instance,
	 * size, location, etc.).
	 */
	private final ComponentTracker decoratedComponentTracker = new ComponentTracker();

	/**
	 * Decoration painter component.<br>It is merely a hook into the Swing painting mechanism.<br>This is the component
	 * that is actually added to the layered pane.
	 *
	 * @see #DECORATION_LAYER_OFFSET
	 * @see #attach(JComponent)
	 * @see #getDecoratedComponentLayerInLayeredPane
	 * @see #decorationPainter
	 */
	protected DecorationPainter decorationPainter = new DecorationPainter();

	/**
	 * Layered pane to which the decoration painter will be added.<br>The decoration will actually contain a component
	 * responsible of painting the decoration.
	 *
	 * @see #decorationPainter
	 */
	private JLayeredPane attachedLayeredPane = null;

	/**
	 * Constructor specifying the component to be decorated and the anchor link between the decorated component and its
	 * decoration.
	 *
	 * @param decoratedComponent Component to be decorated.
	 * @param anchorLink Anchor link between the decorated component and its decoration.
	 */
	public AbstractComponentDecoration(final JComponent decoratedComponent, final AnchorLink anchorLink) {
		this.anchorLink = anchorLink;
		attach(decoratedComponent);
	}

	/**
	 * Attaches the decoration to the specified component.
	 *
	 * @param decoratedComponent Component to be decorated.
	 */
	private void attach(final JComponent decoratedComponent) {
		detach();

		this.decoratedComponent = decoratedComponent;

		if (decoratedComponent != null) {
			decoratedComponent.addComponentListener(decoratedComponentTracker);
			decoratedComponent.addAncestorListener(decoratedComponentTracker);
			decoratedComponent.addHierarchyBoundsListener(decoratedComponentTracker);
			decoratedComponent.addHierarchyListener(decoratedComponentTracker);

			attachToLayeredPane();
		}
	}

	/**
	 * Detaches the decoration from the decorated component.
	 */
	private void detach() {
		setVisible(false);
		if (decoratedComponent != null) {
			decoratedComponent.removeComponentListener(decoratedComponentTracker);
			decoratedComponent.removeAncestorListener(decoratedComponentTracker);
			decoratedComponent.removeHierarchyBoundsListener(decoratedComponentTracker);
			decoratedComponent.removeHierarchyListener(decoratedComponentTracker);

			detachFromLayeredPane();
		}
	}

	/**
	 * Inserts the decoration to the layered pane right above the decorated component.
	 */
	private void attachToLayeredPane() {
		// Get ancestor layered pane that will get the decoration holder component
		final Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, decoratedComponent);
		if (ancestor instanceof JLayeredPane) {
			attachedLayeredPane = (JLayeredPane) ancestor;
			attachedLayeredPane.add(decorationPainter, getDecoratedComponentLayerInLayeredPane(attachedLayeredPane));
		}
	}

	/**
	 * Retrieves the layer index of the decorated component in the layered pane of the window.
	 *
	 * @param layeredPane Layered pane of the window.
	 *
	 * @return Index of the decorated component in the layered pane.
	 */
	private Integer getDecoratedComponentLayerInLayeredPane(final JLayeredPane layeredPane) {
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
	 * Gets the custom clipping ancestor component that will be used to clip the decoration.<br>If null is returned, the
	 * parent container of the decorated component is used.
	 *
	 * @return Clipping ancestor or null.
	 */
	public JComponent getClippingAncestor() {
		return decorationClippingAncestor;
	}

	/**
	 * Sets the custom clipping ancestor component that will be used to clip the decoration.<br>If set to null, the parent
	 * container of the decorated component will be used.<br>Note that the specified clipping component shall be an
	 * ancestor of the decorated component, or the component itself.
	 *
	 * @param decorationClippingAncestor Clipping ancestor or null to
	 */
	public void setClippingAncestor(final JComponent decorationClippingAncestor) {
		this.decorationClippingAncestor = decorationClippingAncestor;
		followDecoratedComponent();
	}

	/**
	 * Gets the effective clipping ancestor.<br>If no custom clipping ancestor is set, the parent container of the
	 * decorated component will be returned.
	 *
	 * @return Effective clipping ancester.
	 */
	private JComponent getEffectiveClippingAncestor() {
		JComponent clippingComponent = decorationClippingAncestor;

		if ((clippingComponent == null) && (decoratedComponent != null) &&
				(decoratedComponent.getParent() instanceof JComponent)) {
			clippingComponent = (JComponent) decoratedComponent.getParent();
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
	public void setAnchorLink(final AnchorLink anchorLink) {
		this.anchorLink = anchorLink;
		followDecoratedComponent();
	}

	/**
	 * States whether the decoration is visible or not.
	 *
	 * @return True if the decoration is visible, false otherwise.
	 */
	public boolean isVisible() {
		return ((decorationPainter != null) && decorationPainter.isVisible());
	}

	/**
	 * Sets the visibility of the decoration.
	 *
	 * @param visible True to make the decoration visible, false to make it invisible.
	 */
	public void setVisible(final boolean visible) {
		if (decorationPainter != null) {
			decorationPainter.setVisible(visible);
		}
	}

	/**
	 * Updates the decoration painter with respect to the decorated component.<br>This method is to be called whenever
	 * changes on the decorated component have an impact on the decoration (for instance, its size, location,
	 * etc.).<br>This method has been made protected so that it can be easily called from the implementating sub-classes.
	 */
	protected void followDecoratedComponent() {
		if ((decorationPainter != null) && (anchorLink != null)) {
			if (attachedLayeredPane == null) {
				attachToLayeredPane();
			}
			final Container layeredPane = SwingUtilities.getAncestorOfClass(JLayeredPane.class, decoratedComponent);
			if (layeredPane instanceof JLayeredPane) {
				final Point relativeLocationToOwner = anchorLink
						.getRelativeSlaveLocation(decoratedComponent.getWidth(), decoratedComponent.getHeight(),
								getWidth(), getHeight());

				// Calculate decoration painter unclipped bounds
				final Point ownerLocationInLayeredPane = SwingUtilities
						.convertPoint(decoratedComponent.getParent(), decoratedComponent.getLocation(), layeredPane);
				final Rectangle decorationBoundsInLayeredPane =
						new Rectangle(ownerLocationInLayeredPane.x + relativeLocationToOwner.x,
								ownerLocationInLayeredPane.y + relativeLocationToOwner.y, getWidth(), getHeight());
				decorationPainter.setBounds(decorationBoundsInLayeredPane);

				// Calculate decoration painter clipped bounds
				final JComponent clippingComponent = getEffectiveClippingAncestor();
				final Rectangle ownerBoundsInParent = decoratedComponent.getBounds();
				final Rectangle decorationBoundsInParent =
						new Rectangle(ownerBoundsInParent.x + relativeLocationToOwner.x,
								ownerBoundsInParent.y + relativeLocationToOwner.y, getWidth(), getHeight());
				final Rectangle decorationBoundsInAncestor = SwingUtilities
						.convertRectangle(decoratedComponent.getParent(), decorationBoundsInParent, clippingComponent);
				final Rectangle ancestorVisibleRect = clippingComponent.getVisibleRect();
				final Rectangle decorationVisibleBoundsInAncestor =
						ancestorVisibleRect.intersection(decorationBoundsInAncestor);

				if ((decorationVisibleBoundsInAncestor.width != 0) && (decorationVisibleBoundsInAncestor.height != 0)) {
					final Rectangle decorationVisibleBoundsInLayeredPane = SwingUtilities
							.convertRectangle(clippingComponent, decorationVisibleBoundsInAncestor, layeredPane);

					// Clip graphics context
					final Rectangle clipBounds = SwingUtilities
							.convertRectangle(decorationPainter.getParent(), decorationVisibleBoundsInLayeredPane,
									decorationPainter);
					decorationPainter.setClipBounds(clipBounds);
				} else {
					decorationPainter.setClipBounds(null);
				}
			} else {
				decorationPainter.setClipBounds(null);
			}

			// Repaint
			decorationPainter.repaint();
			if (layeredPane != null) {
				layeredPane.repaint();
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
