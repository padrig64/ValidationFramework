/* Copyright (c) 2007 Timothy Wall, All Rights Reserved
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * <p/>
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package validation.decoration.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

import validation.decoration.swing.utils.DualAnchor;

/**
 * Provide a method for consistently augmenting the appearance of a given
 * owner by painting something on it <i>after</i> the owner itself
 * gets painted.  If not explicitly removed via {@link #dispose}, an instance
 * of this object will live as long as its target owner.<p>
 * By default, the decorator matches the location and size of the decorated
 * owner, but the bounds can be adjusted by overriding
 * {@link #getDecorationBounds()}.  The {@link #synch()} method should be
 * called whenever the bounds returned by {@link #getDecorationBounds()} would
 * change.
 * <p/>
 * The decoration is clipped to the bounds set on the decoration, which does
 * not necessarily need to be the same as the decorated owner's bounds.
 * The decoration may extend beyond the decorated owner bounds, or it may
 * be reduced to a smaller region.
 */
public abstract class AbstractDecorator {

	/**
	 * Tracks changes to owner configuration.
	 */
	private final class Listener extends ComponentAdapter
			implements HierarchyListener, HierarchyBoundsListener,
			PropertyChangeListener {
		// NOTE: OSX (1.6) doesn't generate these the same as w32
		public void hierarchyChanged(HierarchyEvent e) {
			if ((e.getChangeFlags() & HierarchyEvent.PARENT_CHANGED) != 0) {
				attach();
			}
		}

		public void ancestorResized(HierarchyEvent e) {
			synch();
		}

		public void ancestorMoved(HierarchyEvent e) {
			synch();
		}

		public void propertyChange(PropertyChangeEvent e) {
			if (JLayeredPane.LAYER_PROPERTY.equals(e.getPropertyName())) {
				attach();
			}
		}

		public void componentMoved(ComponentEvent e) {
			synch();
		}

		public void componentResized(ComponentEvent e) {
			synch();
		}

		public void componentHidden(ComponentEvent e) {
			setVisible(false);
		}

		public void componentShown(ComponentEvent e) {
			setVisible(true);
		}
	}

	/**
	 * Used to hook into the Swing painting architecture.
	 */
	protected class Painter extends JComponent {

		private int base;

		{
			setFocusable(false);
		}

		public boolean isShowing() {
			return AbstractDecorator.this.getComponent().isShowing();
		}

		public int getDecoratedLayer() {
			return base;
		}

		public void setDecoratedLayer(int base) {
			this.base = base;
		}

		/**
		 * Delegate to the containing decorator to perform the paint.
		 */
		public void paintComponent(Graphics g) {
			if (!owner.isShowing())
				return;
			Graphics g2 = g.create();
			g2.translate(-originOffset.x, -originOffset.y);
			AbstractDecorator.this.paint(g2);
			g2.dispose();
		}
	}

	private static final int LAYER_POSITION = 0;

	/**
	 * Account for the difference between the decorator actual origin
	 * and the logical origin we want to pass to the {@link #paint} method.
	 */
	private Point originOffset = new Point(0, 0);

	private JComponent owner;
	private DualAnchor dualAnchor;

	protected Painter painter;
	private Container parent;
	private Component layerRoot;
	private Listener listener;
	private int layerOffset;
	private Rectangle bounds;

	/**
	 * Constructor specifying the decorator owner.
	 */
	public AbstractDecorator(JComponent owner, DualAnchor dualAnchor) {
		this(owner, dualAnchor, 1);
	}

	/**
	 * Create a decorator for the given owner, indicating the layer
	 * offset from the target owner.  Negative values mean the decoration
	 * is painted <em>before</em> the target owner is painted.
	 */
	public AbstractDecorator(JComponent owner, DualAnchor dualAnchor, int layerOffset) {
		this.owner = owner;
		this.dualAnchor = dualAnchor;

		this.layerOffset = layerOffset;
		this.bounds = null;
		parent = owner.getParent();
		painter = new Painter();
		listener = new Listener();
		this.owner.addHierarchyListener(listener);
		this.owner.addHierarchyBoundsListener(listener);
		this.owner.addComponentListener(listener);
		attach();
	}

	public DualAnchor getDualAnchor() {
		return dualAnchor;
	}

	public void setDualAnchor(DualAnchor dualAnchor) {
		this.dualAnchor = dualAnchor;
		synch();
	}

	/**
	 * Indicate whether the decoration is visible.  The decoration
	 * may be clipped by ancestor scroll panes or by being moved outside
	 * if the visible region of its parent window.
	 */
	private boolean isVisible() {
		return painter.isVisible();
	}

	/**
	 * Use this to change the visibility of the decoration.
	 */
	private void setVisible(boolean visible) {
		painter.setVisible(visible);
	}

	protected void attach() {
		if (layerRoot != null) {
			layerRoot.removePropertyChangeListener(listener);
			layerRoot = null;
		}
		RootPaneContainer rpc = (RootPaneContainer)
				SwingUtilities.getAncestorOfClass(RootPaneContainer.class, owner);
		if (rpc != null
				&& SwingUtilities.isDescendingFrom(owner, rpc.getLayeredPane())) {
			JLayeredPane lp = rpc.getLayeredPane();
			Component layeredChild = owner;
			int layer = JLayeredPane.DRAG_LAYER.intValue();
			if (layeredChild == lp) {
				// Is the drag layer the best layer to use when decorating
				// the layered pane?
				painter.setDecoratedLayer(layer);
			} else {
				while (layeredChild.getParent() != lp) {
					layeredChild = layeredChild.getParent();
				}
				int base = lp.getLayer(layeredChild);
				// NOTE: JLayeredPane doesn't properly repaint an overlapping
				// child when an obscured child calls repaint() if the two
				// are in the same layer, so we use the next-higher layer
				// instead of simply using a different position within the
				// layer.
				layer = base + layerOffset;
				painter.setDecoratedLayer(base);
				layerRoot = layeredChild;
				layerRoot.addPropertyChangeListener(listener);
			}
			lp.add(painter, new Integer(layer), LAYER_POSITION);
		} else {
			// Always detach when the target owner's window is null
			// or is not a suitable container,
			// otherwise we might prevent GC of the owner
			Container parent = painter.getParent();
			if (parent != null) {
				parent.remove(painter);
			}
		}
		// Track size changes in the decorated owner's parent
		if (parent != null) {
			parent.removeComponentListener(listener);
		}
		parent = owner.getParent();
		if (parent != null) {
			parent.addComponentListener(listener);
		}
		synch();
	}

	/**
	 * Stop decorating.
	 */
	public void detach() {
		owner.removeHierarchyListener(listener);
		owner.removeHierarchyBoundsListener(listener);
		owner.removeComponentListener(listener);
		if (parent != null) {
			parent.removeComponentListener(listener);
			parent = null;
		}
		if (layerRoot != null) {
			layerRoot.removePropertyChangeListener(listener);
			layerRoot = null;
		}
		Container painterParent = painter.getParent();
		if (painterParent != null) {
			Rectangle bounds = painter.getBounds();
			painterParent.remove(painter);
			painterParent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
		}
		owner.repaint();
		owner = null;
	}

	/**
	 * Ensure the size of the decorator matches the current
	 * decoration bounds with appropriate clipping to viewports.
	 */
	protected void synch() {
		Container painterParent = painter.getParent();
		if (painterParent != null) {
			Rectangle decorated = getDecorationBounds();
			Rectangle clipRect = clipDecorationBounds(decorated);

			Point pt = SwingUtilities.convertPoint(owner,
					clipRect.x, clipRect.y,
					painterParent);
			if (clipRect.width <= 0 || clipRect.height <= 0) {
				setPainterBounds(-1, -1, 0, 0);
				setVisible(false);
			} else {
				setPainterBounds(pt.x, pt.y, clipRect.width, clipRect.height);
				setVisible(true);
			}
			painterParent.repaint();
		}
	}

	/**
	 * Adjust the painting offsets and size of the decoration to
	 * account for ancestor clipping.  This might be due to scroll panes
	 * or having the decoration lie outside the parent layered pane.
	 */
	protected Rectangle clipDecorationBounds(Rectangle decorated) {
		// Amount we have to translate the Graphics context
		originOffset.x = decorated.x;
		originOffset.y = decorated.y;
		// If the the owner is obscured (by a viewport or some
		// other means), use the painter bounds to clip to the visible
		// bounds.  Doing may change the actual origin, so adjust our
		// origin offset accordingly
		Rectangle visible = getClippingRect(owner, decorated);
		Rectangle clipRect = decorated.intersection(visible);
		if (decorated.x < visible.x)
			originOffset.x += visible.x - decorated.x;
		if (decorated.y < visible.y)
			originOffset.y += visible.y - decorated.y;
		return clipRect;
	}

	/**
	 * Return any clipping rectangle detected above the given owner,
	 * in the coordinate space of the given owner.  The given rectangle
	 * is desired to be visible.
	 */
	private Rectangle getClippingRect(Container component, Rectangle desired) {
		Rectangle visible = component instanceof JComponent
				? ((JComponent) component).getVisibleRect()
				: new Rectangle(0, 0, component.getWidth(), component.getHeight());
		Rectangle clip = new Rectangle(desired);
		if (desired.x >= visible.x && desired.y >= visible.y
				&& desired.x + desired.width <= visible.x + visible.width
				&& desired.y + desired.height <= visible.y + visible.height) {
			// desired rect is within the current clip rect
		} else if (component.getParent() != null) {
			// Only apply the clip if it is actually smaller than the
			// owner's visible area
			if (component != painter.getParent()
					&& (visible.x > 0 || visible.y > 0
					|| visible.width < component.getWidth()
					|| visible.height < component.getHeight())) {
				// Don't alter the original rectangle
				desired = new Rectangle(desired);
				desired.x = Math.max(desired.x, visible.x);
				desired.y = Math.max(desired.y, visible.y);
				desired.width = Math.min(desired.width,
						visible.x + visible.width - desired.x);
				desired.height = Math.min(desired.height,
						visible.y + visible.height - desired.y);

				// Check for clipping further up the hierarchy
				desired.x += component.getX();
				desired.y += component.getY();
				clip = getClippingRect(component.getParent(), desired);
				clip.x -= component.getX();
				clip.y -= component.getY();
			}
		}
		return clip;
	}

	/**
	 * Return the bounds, relative to the decorated owner, of the
	 * decoration.  The default covers the entire owner.  Note that
	 * this method will be called from the constructor, so be careful
	 * when overriding and referencing derived class state.
	 */
	protected Rectangle getDecorationBounds() {
		return bounds != null
				? bounds : new Rectangle(0, 0, owner.getWidth(), owner.getHeight());
	}

	/**
	 * Change the bounds of the decoration, relative to the decorated
	 * owner.  The special null value means the bounds
	 * will track the owner bounds.
	 */
	protected void setDecorationBounds(Rectangle bounds) {
		if (bounds == null) {
			this.bounds = bounds;
		} else {
			this.bounds = new Rectangle(bounds);
		}
		synch();
	}

	/**
	 * Change the bounds of the decoration, relative to the decorated
	 * owner.
	 */
	protected void setDecorationBounds(int x, int y, int w, int h) {
		setDecorationBounds(new Rectangle(x, y, w, h));
	}

	protected void setPainterBounds(int x, int y, int w, int h) {
		painter.setBounds(x, y, w, h);
		repaint();
	}

	/**
	 * Returns the decorated owner.
	 */
	protected JComponent getComponent() {
		return owner;
	}

	/**
	 * Force a refresh of the underlying owner and its decoration.
	 */
	public void repaint() {
		JLayeredPane p = (JLayeredPane) painter.getParent();
		if (p != null) {
			p.repaint(painter.getBounds());
		}
	}

	protected abstract int getWidth();

	protected abstract int getHeight();

	/**
	 * Define the decoration's appearance.  The point (0,0) represents
	 * the upper left corner of the decorated owner.
	 * The default clip mask will be the extents of the decoration bounds, as
	 * indicated by {@link #getDecorationBounds()}, which defaults to the
	 * decorated owner bounds.
	 */
	public abstract void paint(Graphics g);
}
