package com.github.swingcomplements.container;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.util.concurrent.TimeUnit;

import javax.swing.JPanel;

import com.github.swingcomplements.container.transition.NoTransition;
import com.github.swingcomplements.container.transition.SlideTransition;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

/**
 * Container allowing to replace its contents using transitions.<br>Note that the contents (slide) will use the whole
 * container when it is laid out.
 */
public class SlideContainer extends JPanel {

	/**
	 * Layout manager laying out the new and old slides according to the animation step.<br>The layout is triggered by
	 * regular Swing mechanisms, but also by the animation target during the transition animation.
	 */
	private class SlideLayout implements LayoutManager {

		/**
		 * @see LayoutManager#addLayoutComponent(String, Component)
		 */
		@Override
		public void addLayoutComponent(String s, Component component) {
			// Nothing to be done
		}

		/**
		 * @see LayoutManager#removeLayoutComponent(Component)
		 */
		@Override
		public void removeLayoutComponent(Component component) {
			// Nothing to be done
		}

		/**
		 * @see LayoutManager#preferredLayoutSize(Container)
		 */
		@Override
		public Dimension preferredLayoutSize(Container container) {
			Dimension size;

			if (currentSlide == null) {
				size = new Dimension(0, 0);
			} else {
				size = currentSlide.getPreferredSize();
			}

			return size;
		}

		/**
		 * @see LayoutManager#minimumLayoutSize(Container)
		 */
		@Override
		public Dimension minimumLayoutSize(Container container) {
			Dimension size;

			if (currentSlide == null) {
				size = new Dimension(0, 0);
			} else {
				size = currentSlide.getMinimumSize();
			}

			return size;
		}

		/**
		 * @see LayoutManager#layoutContainer(Container)
		 */
		@Override
		public void layoutContainer(Container container) {
			transition.layoutContainer(container, oldSlide, currentSlide, transitionStep);
		}
	}

	/**
	 * Animation target used by the animator.<br>It triggers the layout on each animation step.
	 */
	private class SlideAnimationTarget implements TimingTarget {

		/**
		 * @see TimingTarget#begin(Animator)
		 */
		@Override
		public void begin(Animator animator) {
			transitionStep = 0.0;

			revalidate();
			repaint();
		}

		/**
		 * @see TimingTarget#end(Animator)
		 */
		@Override
		public void end(Animator animator) {
			transitionStep = 1.0;

			if (oldSlide != null) {
				remove(oldSlide);
				oldSlide = null;
			}

			revalidate();
			repaint();
		}

		/**
		 * @see TimingTarget#repeat(Animator)
		 */
		@Override
		public void repeat(Animator animator) {
			// Nothing to be done
		}

		/**
		 * @see TimingTarget#reverse(Animator)
		 */
		@Override
		public void reverse(Animator animator) {
			// Nothing to be done
		}

		/**
		 * @see TimingTarget#timingEvent(Animator, double)
		 */
		@Override
		public void timingEvent(Animator animator, double v) {
			transitionStep = v;

			// Trigger layout
			revalidate();
			repaint();
		}
	}

	private static final int DEFAULT_ANIMATION_DURATION = 350;


	/**
	 * Animator orchestrating the transition from the old slide to the new slide.<br>It is started upon calls to {@link
	 * #setSlide(Component, SlideTransition)}.
	 */
	private Animator animator;

	/**
	 * Slide that will be totally replaced by the new slide at the end of the transition.
	 */
	private Component oldSlide = null;

	/**
	 * Slide that is currently being displayed.<br>At the beginning of the transition, it already corresponds to the final
	 * slide.
	 */
	private Component currentSlide = null;

	/**
	 * Transition to remove the old slide and show the new slide.
	 */
	private SlideTransition transition = new NoTransition();

	/**
	 * Current step in the transition animation.<br>0.0 corresponds to the beginning and 1.0 corresponds to the end of the
	 * transition animation.
	 */
	private double transitionStep = 0.0;

	/**
	 * Default constructor creating the animator.
	 */
	public SlideContainer() {
		setLayout(new SlideLayout());

		// Create animator
		TimingSource ts = new SwingTimerTimingSource();
		Animator.setDefaultTimingSource(ts);
		ts.init();
		animator = new Animator.Builder().setDuration(DEFAULT_ANIMATION_DURATION, TimeUnit.MILLISECONDS)
										 .setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(
						new SlideAnimationTarget()).build();
	}

	/**
	 * Retrieves the current slide.
	 *
	 * @return Current slide.
	 */
	public Component getSlide() {
		return currentSlide;
	}

	/**
	 * Sets the new slide to be displayed.
	 *
	 * @param newSlide Slide to be fully shown at the end of the transition animation.
	 * @param transition Transition to be used to hide the old slide and show the new slide.
	 */
	public void setSlide(Component newSlide, SlideTransition transition) {

		// Finish any current transition
		if ((animator != null) && animator.isRunning()) {
			animator.stop();
		}

		// Create default transition if needed
		if (transition == null) {
			this.transition = new NoTransition();
		} else {
			this.transition = transition;
		}

		// Add new slide to component hierarchy
		if (newSlide != null) {
			add(newSlide, this.transition.getNewSlideOrderIndex());
		}

		// Current slide replaces old slide (will be removed)
		oldSlide = currentSlide;

		// New slide will replace current slide
		currentSlide = newSlide;

		// Animate transition to new slide
		animator.start();
	}
}
