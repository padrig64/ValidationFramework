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

package com.github.validationframework.decoration.swing;

import com.github.validationframework.decoration.swing.utils.AnchorLink;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

public abstract class AbstractDecorator {

	private final class ComponentTracker implements ComponentListener, AncestorListener {

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

		@Override
		public void componentMoved(final ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentResized(final ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentHidden(final ComponentEvent e) {
			// TODO
		}

		@Override
		public void componentShown(final ComponentEvent e) {
			// TODO
		}
	}

	protected class DecorationHolder extends JComponent {

		private static final long serialVersionUID = 7573896845503780433L;

		public DecorationHolder() {
			setFocusable(false);
		}

		@Override
		public int getWidth() {
			return AbstractDecorator.this.getWidth();
		}

		@Override
		public int getHeight() {
			return AbstractDecorator.this.getHeight();
		}

		@Override
		public void paintComponent(final Graphics g) {
			if (isShowing()) {
				AbstractDecorator.this.paint(g);
			}
		}
	}

	private JComponent owner;
	private AnchorLink anchorLink;
	private final ComponentTracker ownerTracker = new ComponentTracker();
	protected DecorationHolder decorationHolder = new DecorationHolder();

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

			// Get ancestor layered pane that will get the decoration holder component
			final Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
			if (ancestor instanceof JLayeredPane) {
				ancestor.add(decorationHolder, JLayeredPane.DRAG_LAYER);
			}
		}
	}

	public void detach() {
		if (owner != null) {
			owner.removeComponentListener(ownerTracker);
			owner.removeAncestorListener(ownerTracker);

			// Get ancestor layered pane that contained the decoration holder component
			final Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
			if (ancestor instanceof JLayeredPane) {
				ancestor.remove(decorationHolder);
			}
		}
	}

	public AnchorLink getAnchorLink() {
		return anchorLink;
	}

	public void setAnchorLink(final AnchorLink anchorLink) {
		this.anchorLink = anchorLink;
	}

	public boolean isVisible() {
		return ((decorationHolder != null) && decorationHolder.isVisible());
	}

	public void setVisible(final boolean visible) {
		if (decorationHolder != null) {
			decorationHolder.setVisible(visible);
		}
	}

	protected void followOwner() {
		if (decorationHolder != null) {
			final Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
			if (ancestor instanceof JLayeredPane) {
				final Point locationInLayeredPane =
						SwingUtilities.convertPoint(owner.getParent(), owner.getLocation(), ancestor);
				final Point relativeLocation = anchorLink
						.getRelativeSlaveLocation(owner.getWidth(), owner.getHeight(), getWidth(), getHeight());
				decorationHolder.setBounds(locationInLayeredPane.x + relativeLocation.x,
						locationInLayeredPane.y + relativeLocation.y, getWidth(), getHeight());
			} else {
				decorationHolder.setBounds(0, 0, 0, 0);
			}

			// Repaint
			decorationHolder.repaint();
		}
	}

	protected abstract int getWidth();

	protected abstract int getHeight();

	public abstract void paint(Graphics g);
}
