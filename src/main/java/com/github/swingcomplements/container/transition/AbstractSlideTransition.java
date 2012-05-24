package com.github.swingcomplements.container.transition;

public abstract class AbstractSlideTransition implements SlideTransition {

	private final int side;

	public AbstractSlideTransition(int side) {
		this.side = side;
	}

	public int getSide() {
		return side;
	}
}
