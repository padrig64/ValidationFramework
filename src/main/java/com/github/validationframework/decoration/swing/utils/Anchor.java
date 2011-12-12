package com.github.validationframework.decoration.swing.utils;

import java.awt.Dimension;
import java.awt.Point;

public class Anchor {

	public static final Anchor TOP_LEFT = new Anchor(0.0f, 0.0f);
	public static final Anchor TOP_RIGHT = new Anchor(1.0f, 0.0f);
	public static final Anchor BOTTOM_LEFT = new Anchor(0.0f, 1.0f);
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

	public Point getAnchorPoint(Dimension size) {
		return getAnchorPoint(size.width, size.height);
	}

	public Point getAnchorPoint(int width, int height) {
		return new Point((int) (relativeX * width + offsetX), (int) (relativeY * height + offsetY));
	}
}
