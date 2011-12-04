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
	private DualAnchor dualAnchor = new DualAnchor(Anchor.BOTTOM_RIGHT, Anchor.CENTER);

	public IconTipDecorator(JComponent owner) {
		this(owner, null);
	}

	public IconTipDecorator(JComponent owner, Icon icon) {
		super(owner);
		this.icon = icon;
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
			System.out.println("IconTipDecorator.getDecorationBounds: " + iconLocation);
			setDecorationBounds(iconLocation.x, iconLocation.y, icon.getIconWidth(), icon.getIconHeight());
		}
	}

	@Override
	protected Rectangle getDecorationBounds() {
		Rectangle decorationBounds = super.getDecorationBounds();

		System.out.println(decorationBounds);

		return decorationBounds;
	}

	@Override
	public void paint(Graphics g) {
		if (icon != null) {
			icon.paintIcon(getComponent(), g, 0, 0);
		}
	}
}
