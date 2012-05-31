package com.github.validationframework.decoration.swing;

import com.github.validationframework.decoration.swing.utils.DualAnchor;
import com.github.validationframework.utils.ValueUtils;
import com.sun.jna.platform.WindowUtils;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.JToolTip;
import javax.swing.SwingUtilities;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;

public class ToolTipDialog extends JDialog {

	private class LocationAdapter implements ComponentListener, AncestorListener {

		@Override
		public void componentResized(final ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentMoved(final ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentShown(final ComponentEvent e) {
			// Nothing to be done
		}

		@Override
		public void componentHidden(final ComponentEvent e) {
			// Nothing to be done
		}

		@Override
		public void ancestorAdded(final AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorRemoved(final AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorMoved(final AncestorEvent event) {
			followOwner();
		}
	}

	private class TransparencyAdapter extends MouseAdapter implements TimingTarget {

		private final float MIN_ALPHA = 0.25f;
		private final float MAX_ALPHA = 1.0f;
		private final float FADE_OUT_MAX_DURATION = 100;
		private final float FADE_IN_MAX_DURATION = 55;

		private float currentAlpha = MAX_ALPHA;

		private Animator animator = null;

		public TransparencyAdapter() {
			final TimingSource ts = new SwingTimerTimingSource();
			Animator.setDefaultTimingSource(ts);
			ts.init();
		}

		@Override
		public void mouseEntered(final MouseEvent e) {
			if (isRolloverAnimated()) {
				if ((animator != null) && (animator.isRunning())) {
					animator.stop();
				}
				final long duration =
						(long) ((currentAlpha - MIN_ALPHA) * FADE_OUT_MAX_DURATION / (MAX_ALPHA - MIN_ALPHA));
				if (duration <= 0) {
					timingEvent(null, 0.0);
				} else {
					animator = new Animator.Builder().setDuration(duration, TimeUnit.MILLISECONDS)
							.setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(this).build();
					animator.startReverse();
				}
			} else {
				WindowUtils.setWindowAlpha(ToolTipDialog.this, MIN_ALPHA);
			}
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			if (isRolloverAnimated()) {
				if ((animator != null) && (animator.isRunning())) {
					animator.stop();
				}

				final long duration =
						(long) ((MAX_ALPHA - currentAlpha) * FADE_IN_MAX_DURATION / (MAX_ALPHA - MIN_ALPHA));
				if (duration <= 0) {
					timingEvent(null, 1.0);
				} else {
					animator = new Animator.Builder().setDuration(duration, TimeUnit.MILLISECONDS)
							.setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(this).build();
					animator.start();
				}
			} else {
				WindowUtils.setWindowAlpha(ToolTipDialog.this, MAX_ALPHA);
			}
		}

		@Override
		public void begin(final Animator animator) {
			// Nothing to be done because we stop the animation manually
		}

		@Override
		public void end(final Animator animator) {
			// Nothing to be done because we stop the animation manually
		}

		@Override
		public void repeat(final Animator animator) {
			// Nothing to be done
		}

		@Override
		public void reverse(final Animator animator) {
			// Nothing to be done
		}

		@Override
		public void timingEvent(final Animator animator, final double v) {
			currentAlpha = (float) (v * (MAX_ALPHA - MIN_ALPHA)) + MIN_ALPHA;
			WindowUtils.setWindowAlpha(ToolTipDialog.this, currentAlpha);
		}
	}

	private JComponent owner = null;

	private JToolTip toolTip = null;

	private DualAnchor dualAnchor = null;

	private final LocationAdapter locationAdapter = new LocationAdapter();

	private boolean rolloverAnimated = true;

	public ToolTipDialog(final JComponent owner, final DualAnchor dualAnchor) {
		super(SwingUtilities.getWindowAncestor(owner));
		this.owner = owner;
		this.dualAnchor = dualAnchor;
		initComponents();
	}

	private void initComponents() {
		toolTip = new JToolTip();
		toolTip.addMouseListener(new TransparencyAdapter());

		owner.addComponentListener(locationAdapter);
		owner.addAncestorListener(locationAdapter);

		setUndecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Just in case new LAF provides decorations
		setFocusable(false); // Just in case...
		setFocusableWindowState(false);
		setResizable(false); // Just in case...
		setContentPane(toolTip);
		pack(); // Seems to help for the very first setVisible(true) when window transparency is on

		if (WindowUtils.isWindowAlphaSupported()) {
			WindowUtils.setWindowTransparent(this, true);
		}
	}

	@Override
	public void setVisible(final boolean visible) {
		setSize(toolTip.getPreferredSize());
		followOwner();
		super.setVisible(visible);
	}

	@Override
	public void dispose() {
		owner.removeAncestorListener(locationAdapter);
		owner.removeComponentListener(locationAdapter);
		super.dispose();
	}

	public boolean isRolloverAnimated() {
		return rolloverAnimated;
	}

	public void setRolloverAnimated(final boolean animated) {
		this.rolloverAnimated = animated;
	}

	public String getText() {
		return toolTip.getTipText();
	}

	public void setText(final String text) {
		// Only change if different
		if (!ValueUtils.areEqual(text, toolTip.getTipText())) {

			final boolean wasVisible = isVisible();
			if (wasVisible) {
				setVisible(false);
			}

			toolTip.setTipText(text);

			if (wasVisible) {
				setVisible(wasVisible);
			}
		}
	}

	private void followOwner() {
		if (owner.isVisible()) {
			final Point screenLocation = owner.getLocationOnScreen();
			final Point relativeSlaveLocation =
					dualAnchor.getRelativeSlaveLocation(owner.getSize(), ToolTipDialog.this.getSize());
			setLocation(screenLocation.x + relativeSlaveLocation.x, screenLocation.y + relativeSlaveLocation.y);
		}
	}
}
