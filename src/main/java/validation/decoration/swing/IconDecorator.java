package validation.decoration.swing;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;

import validation.decoration.swing.utils.DualAnchor;

public class IconDecorator extends AbstractDecorator {

	private Icon icon;

	public IconDecorator(JComponent owner, Icon icon, DualAnchor dualAnchor) {
		super(owner, dualAnchor);
		this.icon = icon;
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
		synch();
	}

	@Override
	protected int getWidth() {
		int width = 0;
		if (icon != null) {
			width = icon.getIconWidth();
		}
		return width;
	}

	@Override
	protected int getHeight() {
		int height = 0;
		if (icon != null) {
			height = icon.getIconHeight();
		}
		return height;
	}

	@Override
	public void paint(Graphics g) {
		if (icon != null) {
			icon.paintIcon(painter, g, 0, 0);
		}
	}
}
