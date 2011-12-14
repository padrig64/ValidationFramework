package com.github.validationframework.decoration.swing;

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

import com.github.validationframework.decoration.swing.utils.DualAnchor;

public abstract class AbstractDecorator {

	private final class ComponentTracker implements ComponentListener, AncestorListener {

		@Override
		public void ancestorAdded(AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorRemoved(AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorMoved(AncestorEvent event) {
			followOwner();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentResized(ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// TODO
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// TODO
		}
	}

	protected class DecorationHolder extends JComponent {

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
		public void paintComponent(Graphics g) {
			if (isShowing()) {
				AbstractDecorator.this.paint(g);
			}
		}
	}

	private JComponent owner;
	private DualAnchor dualAnchor;
	private ComponentTracker ownerTracker = new ComponentTracker();
	protected DecorationHolder decorationHolder = new DecorationHolder();

	public AbstractDecorator(JComponent owner, DualAnchor dualAnchor) {
		this.dualAnchor = dualAnchor;
		attach(owner);
	}

	public void attach(JComponent owner) {
		detach();

		this.owner = owner;

		if (owner != null) {
			owner.addComponentListener(ownerTracker);
			owner.addAncestorListener(ownerTracker);

			// Get ancestor layered pane that will get the decoration holder component
			Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
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
			Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
			if (ancestor instanceof JLayeredPane) {
				ancestor.remove(decorationHolder);
			}
		}
	}

	public DualAnchor getDualAnchor() {
		return dualAnchor;
	}

	public void setDualAnchor(DualAnchor dualAnchor) {
		this.dualAnchor = dualAnchor;
	}

	public boolean isVisible() {
		return ((decorationHolder != null) && decorationHolder.isVisible());
	}

	public void setVisible(boolean visible) {
		if (decorationHolder != null) {
			decorationHolder.setVisible(visible);
		}
	}

	protected void followOwner() {
		if (decorationHolder != null) {
			Container ancestor = SwingUtilities.getAncestorOfClass(JLayeredPane.class, owner);
			if (ancestor instanceof JLayeredPane) {
				Point locationInLayeredPane = SwingUtilities.convertPoint(owner.getParent(), owner.getLocation(),
																		  ancestor);
				Point relativeLocation = dualAnchor.getRelativeSlaveLocation(owner.getWidth(), owner.getHeight(),
																			 getWidth(), getHeight());
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
