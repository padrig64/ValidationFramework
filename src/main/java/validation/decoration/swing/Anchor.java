package validation.decoration.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

public class Anchor {

	public static final Anchor TOP_LEFT = new Anchor(AnchorPosition.BEGIN, AnchorPosition.BEGIN);
	public static final Anchor TOP_RIGHT = new Anchor(AnchorPosition.BEGIN, AnchorPosition.END);
	public static final Anchor BOTTOM_LEFT = new Anchor(AnchorPosition.END, AnchorPosition.BEGIN);
	public static final Anchor BOTTOM_RIGHT = new Anchor(AnchorPosition.END, AnchorPosition.END);
	public static final Anchor CENTER = new Anchor(AnchorPosition.CENTER, AnchorPosition.CENTER);

	private final AnchorPosition positionX;
	private final AnchorPosition positionY;

	private final int offsetX;
	private final int offsetY;

	public Anchor(AnchorPosition positionX, AnchorPosition positionY) {
		this(positionX, 0, positionY, 0);
	}

	public Anchor(AnchorPosition positionX, int offsetX, AnchorPosition positionY, int offsetY) {
		this.positionX = positionX;
		this.offsetX = offsetX;
		this.positionY = positionY;
		this.offsetY = offsetY;
	}

	public AnchorPosition getPositionX() {
		return positionX;
	}

	public AnchorPosition getPositionY() {
		return positionY;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public Point getAnchorPoint(Component comp) {
		Point point;

		if (comp == null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			point = new Point(getLength(positionX, screenSize.width), getLength(positionY, screenSize.height));
		} else {
			point = new Point(getLength(positionX, comp.getWidth()), getLength(positionY, comp.getHeight()));
		}

		return point;
	}

	public Point getComponentOrigin(Component comp) {
		Point point;

		if (comp == null) {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			point = new Point(getLength(positionX, screenSize.width), getLength(positionY, screenSize.height));
		} else {
			point = new Point(getLength(positionX, comp.getWidth()), getLength(positionY, comp.getHeight()));
		}

		return point;
	}

	private static int getLength(AnchorPosition position, int totalLength) {
		int length = 0;

		switch (position) {
			case BEGIN:
				length = 0;
				break;
			case ONE_QUARTER:
				length = totalLength / 4;
				break;
			case ONE_THIRD:
				length = totalLength / 3;
				break;
			case CENTER:
				length = totalLength / 2;
				break;
			case TWO_THIRD:
				length = totalLength * 2 / 3;
				break;
			case THREE_QUARTERS:
				length = totalLength * 3 / 4;
				break;
			case END:
				length = totalLength;
				break;
			default:
				// TODO
		}

		return length;
	}
}
