package validation.decoration.swing;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JComponent;

import validation.decoration.swing.utils.Anchor;
import validation.decoration.swing.utils.DualAnchor;

public class IconTipDecorator extends AbstractComponentDecorator {

	private class ToolTipAdapter extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			if (isVisible()) {
				toolTipDialog.setVisible(true);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (isVisible()) {
				toolTipDialog.setVisible(false);
			}
		}
	}

	private Icon icon = null;

	private ToolTipDialog toolTipDialog = null;

	private DualAnchor dualAnchorWithOwner = new DualAnchor(Anchor.BOTTOM_LEFT, Anchor.CENTER);
	private DualAnchor dualAnchorWithToolTip = new DualAnchor(Anchor.CENTER, Anchor.TOP_RIGHT);

	private boolean visible = true;

	public IconTipDecorator(JComponent owner) {
		this(owner, null);
	}

	public IconTipDecorator(JComponent owner, Icon icon) {
		super(owner);
		this.icon = icon;

		toolTipDialog = new ToolTipDialog(painter, dualAnchorWithToolTip);
		painter.addMouseListener(new ToolTipAdapter());
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		updateDecorationBounds();
	}

	public String getText() {
		return toolTipDialog.getText();
	}

	public void setText(String text) {
		toolTipDialog.setText(text);
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
		updateDecorationBounds();
	}

	public DualAnchor getDualAnchorWithOwner() {
		return dualAnchorWithOwner;
	}

	public void setDualAnchorWithOwner(DualAnchor dualAnchorWithOwner) {
		this.dualAnchorWithOwner = dualAnchorWithOwner;
		updateDecorationBounds();
	}

	public DualAnchor getDualAnchorWithToolTip() {
		return dualAnchorWithToolTip;
	}

	public void setDualAnchorWithToolTip(DualAnchor dualAnchorWithToolTip) {
		this.dualAnchorWithToolTip = dualAnchorWithToolTip;
	}

	private void updateDecorationBounds() {
		if ((dualAnchorWithOwner == null) || (icon == null)) {
			setDecorationBounds(null);
		} else {
			Point iconLocation = dualAnchorWithOwner.getRelativeSlaveLocation(getComponent(), icon);
			setDecorationBounds(iconLocation.x, iconLocation.y, icon.getIconWidth(), icon.getIconHeight());
		}
	}

	@Override
	public void paint(Graphics g) {
		if (isVisible() && (icon != null)) {
			Rectangle decorationBounds = getDecorationBounds();
			icon.paintIcon(getComponent(), g, (int) decorationBounds.getX(), (int) decorationBounds.getY());
		}
	}
}
