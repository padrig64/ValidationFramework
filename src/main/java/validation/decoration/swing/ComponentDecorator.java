package validation.decoration.swing;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

public class ComponentDecorator extends AbstractDecorator {

	private Component decorator;

	public ComponentDecorator(JComponent owner, Component decorator) {
		super(owner);
		this.decorator = decorator;
	}

	public ComponentDecorator(JComponent c, Component decorator, int layerOffset) {
		super(c, layerOffset);
		this.decorator = decorator;
	}

	@Override
	protected int getWidth() {
		int width = 0;
		if (decorator != null) {
			width = decorator.getWidth();
		}
		return width;
	}

	@Override
	protected int getHeight() {
		int height = 0;
		if (decorator != null) {
			height = decorator.getHeight();
		}
		return height;
	}

	@Override
	public void paint(Graphics g) {
		if (decorator != null) {
			decorator.paint(g);
		}
	}
}
