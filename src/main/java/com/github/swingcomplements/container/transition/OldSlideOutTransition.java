package com.github.swingcomplements.container.transition;

import java.awt.Component;
import java.awt.Container;

import javax.swing.SwingConstants;

/**
 * Transition making the old slide disappearing, leaving the new slide behind.
 */
public class OldSlideOutTransition extends AbstractSlideTransition {

	public OldSlideOutTransition() {
		this(SwingConstants.WEST);
	}

	public OldSlideOutTransition(int side) {
		super(side);
	}

	/**
	 * @see SlideTransition#getNewSlideOrderIndex
	 */
	@Override
	public int getNewSlideOrderIndex() {
		// Old slide will still be on top until it is gone
		return 1;
	}

	/**
	 * @see SlideTransition#layoutContainer(Container, Component, Component, double)
	 */
	@Override
	public void layoutContainer(Container parent, Component oldSlide, Component newSlide, double transitionStep) {
		if (newSlide != null) {
			newSlide.setBounds(0, 0, parent.getWidth(), parent.getHeight());
		}

		if (oldSlide != null) {
			switch (getSide()) {

			case SwingConstants.NORTH:
				oldSlide.setBounds(0, (int) (parent.getHeight() * (-transitionStep)), parent.getWidth(),
								   parent.getHeight());
				break;

			case SwingConstants.EAST:
				oldSlide.setBounds((int) (parent.getWidth() * transitionStep), 0, parent.getWidth(),
								   parent.getHeight());
				break;

			case SwingConstants.SOUTH:
				oldSlide.setBounds(0, (int) (parent.getHeight() * transitionStep), parent.getWidth(),
								   parent.getHeight());
				break;

			case SwingConstants.WEST:
				oldSlide.setBounds((int) (parent.getWidth() * (-transitionStep)), 0, parent.getWidth(),
								   parent.getHeight());
				break;

			default:
				// TODO Log unsupported side
				oldSlide.setBounds(0, 0, parent.getWidth(), parent.getHeight());
			}
		}
	}
}
