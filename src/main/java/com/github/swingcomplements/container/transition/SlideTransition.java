package com.github.swingcomplements.container.transition;

import java.awt.Component;
import java.awt.Container;

/**
 * Interface to be implemented by transition hiding the old slide pane and showing the new slide pane upon calls to
 * {@link com.github.swingcomplements.container.SlideContainer#setSlide(Component, SlideTransition)}.
 */
public interface SlideTransition {

	/**
	 * Returns the position of the new slide in the parent container it will be put in. with respect to the old slide
	 * already present in the parent container.
	 *
	 * @return 0 if new slide should be on top of the old slide, or 1 if it should be below.
	 */
	public int getNewSlideOrderIndex();

	/**
	 * Lays out the old slide and new slide in the parent container for the given animation step.
	 *
	 * @param parent Container holding the old and new slides.
	 * @param oldSlide Old slide to be laid out in the parent container.
	 * @param newSlide New slide to be laid out in the parent container.
	 * @param transitionStep Transition step between 0.0 (beginning) and 1.0 (end).
	 */
	public void layoutContainer(Container parent, Component oldSlide, Component newSlide, double transitionStep);
}
