package validation.decoration.swing;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JComponent;

import validation.decoration.swing.utils.Anchor;
import validation.decoration.swing.utils.DualAnchor;

public class IconTipDecorator extends AbstractComponentDecorator {

	private Icon icon = null;
	private DualAnchor dualAnchor = new DualAnchor(Anchor.BOTTOM_LEFT, Anchor.CENTER);

	private boolean visible = true;

	public IconTipDecorator(JComponent owner) {
		this(owner, null);
	}

	public IconTipDecorator(JComponent owner, Icon icon) {
		super(owner);
		this.icon = icon;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
		updateDecorationBounds();
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
		updateDecorationBounds();
	}

	public DualAnchor getDualAnchor() {
		return dualAnchor;
	}

	public void setDualAnchor(DualAnchor dualAnchor) {
		this.dualAnchor = dualAnchor;
		updateDecorationBounds();
	}

	public void updateDecorationBounds() {
		if ((dualAnchor == null) || (icon == null)) {
			setDecorationBounds(null);
		} else {
			Point iconLocation = dualAnchor.getRelativeSlaveLocation(getComponent(), icon);
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
