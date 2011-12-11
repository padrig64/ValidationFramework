package validation.decoration.swing;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;

import validation.decoration.swing.utils.DualAnchor;

public class ComponentDecorator extends AbstractDecorator {

	private Component decorator;

	public ComponentDecorator(JComponent owner, Component decorator, DualAnchor dualAnchor) {
		super(owner, dualAnchor);
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
