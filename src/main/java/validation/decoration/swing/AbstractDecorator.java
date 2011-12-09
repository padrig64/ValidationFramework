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
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;

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
// NOTE: OSX 1.6 lacks hierarchy events that w32 sends on layer changes

// TODO: should probably do some locking on Component.getTreeLock()
// when moving the owner
// TODO: need to synch underlying cursor when decorator covers more than
// one owner; the cursor should change to match custom cursors if the
// decoration exceeds the owner's bounds (would need to add mouse motion
// listener)
// TODO: set default layer according to decorated owner's layer and z order
// (have to calculate z order on 1.4 JVMs).
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

		//private Cursor cursor;

		{
			setFocusable(false);
		}

		public boolean isShowing() {
			return getComponent().isShowing();
		}

		public JComponent getComponent() {
			return AbstractDecorator.this.getComponent();
		}

		public void setDecoratedLayer(int base) {
			this.base = base;
		}

		public int getDecoratedLayer() {
			return base;
		}

		public boolean isBackgroundDecoration() {
			return layerOffset < 0;
		}

//		/**
//		 * Set the cursor to something else.  If null, the cursor of the
//		 * decorated owner will be used.
//		 */
//		public void setCursor(Cursor cursor) {
//			Cursor oldCursor = getCursor();
//			// Make sure the cursor actually changed, otherwise
//			// we get cursor flicker (notably on w32 title bars)
//			if (oldCursor == null && cursor != null
//					|| oldCursor != null && !oldCursor.equals(cursor)) {
//				this.cursor = cursor;
//				super.setCursor(cursor);
//			}
//		}
//
//		/**
//		 * Returns the cursor of the decorated owner, or the last
//		 * cursor set by {@link #setCursor}.
//		 */
//		public Cursor getCursor() {
//			return cursor != null ? cursor : owner.getCursor();
//		}

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

		/**
		 * Provide a decorator-specific tooltip, shown when within the
		 * decorator's bounds.
		 */
		public String getToolTipText(MouseEvent e) {
			return AbstractDecorator.this.getToolTipText(e);
		}

		public String toString() {
			return "Painter for " + AbstractDecorator.this;
		}
	}

	/**
	 * Provides a shared background painting mechanism for multiple
	 * decorations.  This ensures that the background is only painted once
	 * if more than one background decorator is applied.
	 */
	private static class BackgroundPainter extends AbstractDecorator {
		private static String key(int layer) {
			return "backgroundPainter for layer " + layer;
		}

		private String key;
		private int layer;
		private int width;
		private int height;

		public BackgroundPainter(JLayeredPane p, int layer) {
			super(p, 0);
			this.layer = layer;
			key = key(layer);
			p.putClientProperty(key, this);
			attach();
		}

		// "Hide" children by temporarily setting the owner count to zero
		private int hideChildren(Container c) {
			if (c == null)
				return 0;
			int value = c.getComponentCount();
			try {
				nComponents.set(c, new Integer(0));
			} catch (Exception e) {
				return c.getComponentCount();
			}
			return value;
		}

		// Restore the child count
		private void restoreChildren(Container c, int count) {
			if (c != null) {
				try {
					nComponents.set(c, new Integer(count));
				} catch (Exception e) {
				}
			}
		}

		private void paintBackground(Graphics g, Component parent, JComponent jc) {
			int x = jc.getX();
			int y = jc.getY();
			int w = jc.getWidth();
			int h = jc.getHeight();
			paintBackground(g.create(x, y, w, h), jc);
		}

		private void paintBackground(Graphics g, JComponent jc) {
			if (!jc.isShowing()) return;
			if (jc.isOpaque()) {
				if (useSimpleBackground()) {
					g.setColor(jc.getBackground());
					g.fillRect(0, 0, jc.getWidth(), jc.getHeight());
				} else {
					int count = hideChildren(jc);
					boolean db = jc.isDoubleBuffered();
					if (db)
						jc.setDoubleBuffered(false);
					jc.paint(g);
					if (db)
						jc.setDoubleBuffered(true);
					restoreChildren(jc, count);
				}
			}
			Component[] kids = jc.getComponents();
			for (int i = 0; i < kids.length; i++) {
				if (kids[i] instanceof JComponent) {
					paintBackground(g, jc, (JComponent) kids[i]);
				}
			}
		}

		private List findOpaque(Component root) {
			List list = new ArrayList();
			if (root.isOpaque() && root instanceof JComponent) {
				list.add(root);
				((JComponent) root).setOpaque(false);
			}
			if (root instanceof Container) {
				Component[] kids = ((Container) root).getComponents();
				for (int i = 0; i < kids.length; i++) {
					list.addAll(findOpaque(kids[i]));
				}
			}
			return list;
		}

		private List findDoubleBuffered(Component root) {
			List list = new ArrayList();
			if (root.isDoubleBuffered() && root instanceof JComponent) {
				list.add(root);
				((JComponent) root).setDoubleBuffered(false);
			}
			if (root instanceof Container) {
				Component[] kids = ((Container) root).getComponents();
				for (int i = 0; i < kids.length; i++) {
					list.addAll(findDoubleBuffered(kids[i]));
				}
			}
			return list;
		}

		private void paintForeground(Graphics g, JComponent jc) {
			if (!jc.isShowing()) return;
			List opaque = findOpaque(jc);
			List db = findDoubleBuffered(jc);
			jc.paint(g);
			for (Iterator i = opaque.iterator(); i.hasNext(); ) {
				((JComponent) i.next()).setOpaque(true);
			}
			for (Iterator i = db.iterator(); i.hasNext(); ) {
				((JComponent) i.next()).setDoubleBuffered(true);
			}
		}

		@Override
		protected int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}


		@Override
		protected int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		/**
		 * Walk the list of "background" decorators and paint them.
		 */
		public void paint(Graphics g) {

			JLayeredPane lp = (JLayeredPane) getComponent();
			Component[] kids = lp.getComponents();
			// Construct an area of the intersection of all decorators
			Area area = new Area();
			List painters = new ArrayList();
			List components = new ArrayList();
			for (int i = kids.length - 1; i >= 0; i--) {
				if (kids[i] instanceof Painter) {
					Painter p = (Painter) kids[i];
					if (p.isBackgroundDecoration()
							&& p.getDecoratedLayer() == layer) {
						painters.add(p);
						if (p.isShowing()) {
							area.add(new Area(p.getBounds()));
						}
					}
				} else if (lp.getLayer(kids[i]) == layer
						&& kids[i] instanceof JComponent) {
					components.add(kids[i]);
				}
			}
			if (painters.size() == 0) {
				dispose();
				return;
			}
			if (area.isEmpty()) {
				return;
			}

			g.setClip(area);

			// Paint background for that area
			for (Iterator i = components.iterator(); i.hasNext(); ) {
				JComponent c = (JComponent) i.next();
				if (c.isShowing()) {
					paintBackground(g, lp, c);
				}
			}

			// Paint the bg decorators
			for (Iterator i = painters.iterator(); i.hasNext(); ) {
				Painter p = (Painter) i.next();
				if (p.isShowing()) {
					p.paint(g.create(p.getX(), p.getY(), p.getWidth(), p.getHeight()));
				}
			}
			// Paint foreground for the area
			for (Iterator i = components.iterator(); i.hasNext(); ) {
				JComponent c = (JComponent) i.next();
				if (c.isShowing()) {
					paintForeground(g.create(c.getX(), c.getY(),
							c.getWidth(), c.getHeight()), c);
				}
			}
		}

		public void dispose() {
			getComponent().putClientProperty(key, null);
			super.dispose();
		}

		public String toString() {
			return key + " on " + getComponent();
		}
	}

	private static final int LAYER_POSITION = 0;

	private static Field nComponents;

	static {
		try {
			nComponents = Container.class.getDeclaredField("ncomponents");
			nComponents.setAccessible(true);
		} catch (Exception e) {
			nComponents = null;
		}
	}

	/**
	 * Account for the difference between the decorator actual origin
	 * and the logical origin we want to pass to the {@link #paint} method.
	 */
	private Point originOffset = new Point(0, 0);

	protected Painter painter;
	private JComponent owner;
	private Container parent;
	private Component layerRoot;
	private Listener listener;
	private int layerOffset;
	private Rectangle bounds;

	/**
	 * Constructor specifying the decorator owner.
	 */
	public AbstractDecorator(JComponent owner) {
		this(owner, 1);
	}

	/**
	 * Create a decorator for the given owner, indicating the layer
	 * offset from the target owner.  Negative values mean the decoration
	 * is painted <em>before</em> the target owner is painted.
	 */
	public AbstractDecorator(JComponent owner, int layerOffset) {
		this.owner = owner;
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

	private static boolean useSimpleBackground() {
		return nComponents == null;
	}

	/**
	 * Set the text to be displayed when the mouse is over the decoration.
	 *
	 * @see JComponent#setToolTipText(String)
	 */
	public void setToolTipText(String text) {
		painter.setToolTipText(text);
	}

	/**
	 * Return the currently set default tooltip text.
	 *
	 * @see JComponent#setToolTipText
	 */
	public String getToolTipText() {
		return painter.getToolTipText();
	}

	/**
	 * Provide for different tool tips depending on the actual location
	 * over the decoration.  Note that if you <em>only</em> override this
	 * method, you must also invoke {@link #setToolTipText(String)} with
	 * a non-<span class="javakeyword">null</span> argument.
	 *
	 * @see JComponent#getToolTipText(MouseEvent)
	 */
	public String getToolTipText(MouseEvent e) {
		return getToolTipText();
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
			if (this instanceof BackgroundPainter) {
				layer = ((BackgroundPainter) this).layer;
				painter.setDecoratedLayer(layer);
			} else if (layeredChild == lp) {
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
				if (layerOffset < 0) {
					BackgroundPainter bp = (BackgroundPainter)
							lp.getClientProperty(BackgroundPainter.key(base));
					if (bp == null) {
						bp = new BackgroundPainter(lp, base);
					}
				}
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
	public void setDecorationBounds(Rectangle bounds) {
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
	public void setDecorationBounds(int x, int y, int w, int h) {
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

	/**
	 * Stop decorating.
	 */
	public void dispose() {
		// Disposal must occur on the EDT
		if (!SwingUtilities.isEventDispatchThread()) {
			SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					dispose();
				}
			});
			return;
		}

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
