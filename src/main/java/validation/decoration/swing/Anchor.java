package validation.decoration.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class Anchor {

	public static final Anchor TOP_LEFT = new Anchor(0.0f, 0.0f);
	public static final Anchor TOP_RIGHT = new Anchor(0.0f, 1.0f);
	public static final Anchor BOTTOM_LEFT = new Anchor(1.0f, 0.0f);
	public static final Anchor BOTTOM_RIGHT = new Anchor(1.0f, 1.0f);
	public static final Anchor CENTER = new Anchor(0.5F, 0.5f);

	private final float relativeX;
	private final float relativeY;

	private final int offsetX;
	private final int offsetY;

	public Anchor(float relativeX, float relativeY) {
		this(relativeX, 0, relativeY, 0);
	}

	public Anchor(float relativeX, int offsetX, float relativeY, int offsetY) {
		this.relativeX = relativeX;
		this.offsetX = offsetX;
		this.relativeY = relativeY;
		this.offsetY = offsetY;
	}

	public float getRelativeX() {
		return relativeX;
	}

	public float getRelativeY() {
		return relativeY;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public Point getComponentAnchorPoint(Component comp) {
		Point point;

		if (comp == null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			point = new Point((int) (relativeX * screenSize.width + offsetX), (int) (relativeY * screenSize.height + offsetY));
		} else {
			point = new Point((int) (relativeX * comp.getWidth() + offsetX), (int) (relativeY * comp.getHeight() + offsetY));
		}

		return point;
	}
}
