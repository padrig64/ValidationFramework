package validation.decoration.swing;

import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;

public class IconDecorator extends AbstractDecorator {

	private Icon decorator;

	public IconDecorator(JComponent owner, Icon decorator) {
		super(owner);
		this.decorator = decorator;
	}

	public IconDecorator(JComponent c, Icon decorator, int layerOffset) {
		super(c, layerOffset);
		this.decorator = decorator;
	}

	@Override
	protected int getWidth() {
		int width = 0;
		if (decorator != null) {
			width = decorator.getIconWidth();
		}
		return width;
	}

	@Override
	protected int getHeight() {
		int height = 0;
		if (decorator != null) {
			height = decorator.getIconHeight();
		}
		return height;
	}

	@Override
	public void paint(Graphics g) {
		if (decorator != null) {
			decorator.paintIcon(painter, g, 0, 0);
		}
	}
}
