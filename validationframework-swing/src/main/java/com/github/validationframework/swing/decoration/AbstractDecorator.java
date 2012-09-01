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

import com.github.validationframework.swing.decoration.utils.AnchorLink;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

/**
 * Abstract implementation of a decorator that can be attached to a component.<br>Concrete implementations will just
 * need to provide the size and do the painting on the already-computed location.
 */
public abstract class AbstractDecorator {

	/**
	 * Entity responsible of tracking the changes on the decorated component and/or its ancestors that would require to
	 * update the location and/or the clip bounds of the decoration.
	 */
	private final class ComponentTracker implements AncestorListener, HierarchyBoundsListener, ComponentListener {

		/**
		 * @see AncestorListener#ancestorAdded(AncestorEvent)
		 */
		@Override
		public void ancestorAdded(final AncestorEvent event) {
			// Nothing to be done
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
			followOwner();
		}

		@Override
		public void ancestorResized(final HierarchyEvent e) {
			followOwner();
		}

		/**
		 * @see AncestorListener#ancestorMoved(AncestorEvent)
		 */
		@Override
		public void ancestorMoved(final AncestorEvent event) {
			followOwner();
		}

		/**
		 * @see ComponentListener#componentMoved(ComponentEvent)
		 */
		@Override
		public void componentMoved(final ComponentEvent e) {
			followOwner();
		}

		/**
		 * @see ComponentListener#componentResized(ComponentEvent)
		 */
		@Override
		public void componentResized(final ComponentEvent e) {
			followOwner();
		}

		/**
		 * @see ComponentListener#componentHidden(ComponentEvent)
		 */
		@Override
		public void componentHidden(final ComponentEvent e) {
			// TODO ?
		}

		/**
		 * @see ComponentListener#componentShown(ComponentEvent)
		 */
		@Override
		public void componentShown(final ComponentEvent e) {
			// TODO ?
		}
	}

	/**
	 * Entity responsible of calling the {@link AbstractDecorator#paint(Graphics)} method.<br>It is a hook in the Swing
	 * paint mechanism.
	 */
	protected class DecorationPainter extends JComponent {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = 7573896845503780433L;

		private Rectangle clipBounds = null;

		public void setClipBounds(final Rectangle clipBounds) {
			this.clipBounds = clipBounds;
		}

		/**
		 * Default constructor.
		 */
		public DecorationPainter() {
			setFocusable(false);
		}

		/**
		 * Returns the width of the decoration.
		 *
		 * @return Decoration width.
		 */
		@Override
		public int getWidth() {
			return AbstractDecorator.this.getWidth();
		}

		/**
		 * Returns the height of the decoration.
		 *
		 * @return Decoration height.
		 */
		@Override
		public int getHeight() {
			return AbstractDecorator.this.getHeight();
		}

		/**
		 * Calls the method {@link AbstractDecorator#paint(Graphics)} of the decorator.
		 *
		 * @param g Graphics to paint on.
		 *
		 * @see AbstractDecorator#paint(Graphics)
		 */
		@Override
		public void paintComponent(final Graphics g) {
			if (owner.isVisible() && owner.isShowing() && isVisible() && isShowing() && (clipBounds != null) &&
					(clipBounds.width > 0) && (clipBounds.height > 0)) {
				// Clip graphics
				g.setClip(clipBounds);

				// Paint decorator
				AbstractDecorator.this.paint(g);
			}
		}
	}

	private JComponent owner;
	private AnchorLink anchorLink;
	private final ComponentTracker ownerTracker = new ComponentTracker();
	protected DecorationPainter decorationPainter = new DecorationPainter();

	private JLayeredPane attachedLayeredPane = null;

	public AbstractDecorator(final JComponent owner, final AnchorLink anchorLink) {
		this.anchorLink = anchorLink;
		attach(owner);
	}

	public void attach(final JComponent owner) {
		detach();

		this.owner = owner;

		if (owner != null) {
			owner.addComponentListener(ownerTracker);
			owner.addAncestorListener(ownerTracker);
			owner.addHierarchyBoundsListener(ownerTracker);

			attachToLayeredPane();
		}
	}

	public void detach() {
		if (owner != null) {
			owner.removeComponentListener(ownerTracker);
			owner.removeAncestorListener(ownerTracker);

			detachFromLayeredPane();
		}
	}

	private void attachToLayeredPane() {
		// Get ancestor layered pane that will get the decoration holder component
		final Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
		if (ancestor instanceof JLayeredPane) {
			attachedLayeredPane = (JLayeredPane) ancestor;
			attachedLayeredPane.add(decorationPainter, JLayeredPane.DRAG_LAYER);
		}
	}

	private void detachFromLayeredPane() {
		attachedLayeredPane.remove(decorationPainter);
		attachedLayeredPane = null;
	}

	public AnchorLink getAnchorLink() {
		return anchorLink;
	}

	public void setAnchorLink(final AnchorLink anchorLink) {
		this.anchorLink = anchorLink;
	}

	public boolean isVisible() {
		return ((decorationPainter != null) && decorationPainter.isVisible());
	}

	public void setVisible(final boolean visible) {
		if (decorationPainter != null) {
			decorationPainter.setVisible(visible);
		}
	}

	protected void followOwner() {
		if (decorationPainter != null) {
			if (attachedLayeredPane == null) {
				attachToLayeredPane();
			}
			final Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
			if (ancestor instanceof JLayeredPane) {
				final Point relativeLocationToOwner = anchorLink
						.getRelativeSlaveLocation(owner.getWidth(), owner.getHeight(), getWidth(), getHeight());

				// Calculate decoration painter unclipped bounds
				final Point ownerLocationInLayeredPane =
						SwingUtilities.convertPoint(owner.getParent(), owner.getLocation(), ancestor);
				final Rectangle decorationBoundsInLayeredPane =
						new Rectangle(ownerLocationInLayeredPane.x + relativeLocationToOwner.x,
								ownerLocationInLayeredPane.y + relativeLocationToOwner.y, getWidth(), getHeight());
				decorationPainter.setBounds(decorationBoundsInLayeredPane);

				// Calculate decoration painter clipped bounds
//				System.out.println("=========================================");
//				System.out.println("AbstractDecorator.getVisibleBounds: parent:" + owner.getParent());

//				System.out.println(" |_ bounds in layered pane:            " + decorationBoundsInLayeredPane);

				final Rectangle ownerBoundsInParent = owner.getBounds();
//				System.out.println(" |_ owner bounds in parent:            " + ownerBoundsInParent);

				final Rectangle decorationBoundsInParent =
						new Rectangle(ownerBoundsInParent.x + relativeLocationToOwner.x,
								ownerBoundsInParent.y + relativeLocationToOwner.y, getWidth(), getHeight());
//				System.out.println(" |_ decoration bounds in parent:       " + decorationBoundsInParent);

				final Rectangle parentVisibleRect = ((JComponent) owner.getParent()).getVisibleRect();
//				System.out.println(" |_ parent visible rect in parent:     " + parentVisibleRect);

				final Rectangle decorationVisibleBoundsInParent =
						parentVisibleRect.intersection(decorationBoundsInParent);
//				System.out.println(" |_ decoration visible rect in parent: " + decorationVisibleBoundsInParent);
				if ((decorationVisibleBoundsInParent.width != 0) && (decorationVisibleBoundsInParent.height != 0)) {
					final Rectangle decorationVisibleBoundsInLayeredPane = SwingUtilities
							.convertRectangle(owner.getParent(), decorationVisibleBoundsInParent, ancestor);
//					System.out
//							.println(" |_ visible bounds in layered pane:    " + decorationVisibleBoundsInLayeredPane);
//					decorationPainter.setBounds(decorationVisibleBoundsInLayeredPane);

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
			ancestor.repaint();
		}
	}

	protected abstract int getWidth();

	protected abstract int getHeight();

	public abstract void paint(Graphics g);
}
