package com.github.swingcomplements.container.transition;

import java.awt.Component;
import java.awt.Container;

import javax.swing.SwingConstants;

/**
 * Transition making the new slide appearing on top of the old slide.
 */
public class NewSlideInTransition extends AbstractSlideTransition {

	public NewSlideInTransition() {
		this(SwingConstants.EAST);
	}

	public NewSlideInTransition(int side) {
		super(side);
	}

	/**
	 * @see SlideTransition#getNewSlideOrderIndex()
	 */
	@Override
	public int getNewSlideOrderIndex() {
		// New slide will come on top
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
			switch (getSide()) {

			case SwingConstants.NORTH:
				newSlide.setBounds(0, (int) (parent.getHeight() * (transitionStep - 1.0)), parent.getWidth(),
								   parent.getHeight());
				break;

			case SwingConstants.EAST:
				newSlide.setBounds((int) (parent.getWidth() * (1.0 - transitionStep)), 0, parent.getWidth(),
								   parent.getHeight());
				break;

			case SwingConstants.SOUTH:
				newSlide.setBounds(0, (int) (parent.getHeight() * (1.0 - transitionStep)), parent.getWidth(),
								   parent.getHeight());
				break;

			case SwingConstants.WEST:
				newSlide.setBounds((int) (parent.getWidth() * (transitionStep - 1.0)), 0, parent.getWidth(),
								   parent.getHeight());
				break;

			default:
				// TODO Log unsupported side
				newSlide.setBounds(0, 0, parent.getWidth(), parent.getHeight());
			}
		}
	}
}
