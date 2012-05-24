package com.github.swingcomplements.container.transition;

import java.awt.Component;
import java.awt.Container;

public class NoTransition implements SlideTransition {

	/**
	 * @see SlideTransition#getNewSlideOrderIndex()
	 */
	@Override
	public int getNewSlideOrderIndex() {
		// New slide will come on top of the old slide
		return 0;
	}

	/**
	 * @see SlideTransition#layoutContainer(Container, Component, Component, double)
	 */
	@Override
	public void layoutContainer(Container parent, Component oldSlide, Component newSlide, double transitionStep) {
		if (oldSlide != null) {
			oldSlide.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		}
		if (newSlide != null) {
			newSlide.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		}
	}
}
