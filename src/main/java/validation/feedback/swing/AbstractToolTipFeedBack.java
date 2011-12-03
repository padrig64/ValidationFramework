package validation.feedback.swing;

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

import com.sun.jna.platform.WindowUtils;
import org.jdesktop.core.animation.timing.Animator;
import org.jdesktop.core.animation.timing.TimingSource;
import org.jdesktop.core.animation.timing.TimingTarget;
import org.jdesktop.core.animation.timing.interpolators.SplineInterpolator;
import org.jdesktop.swing.animation.timing.sources.SwingTimerTimingSource;
import validation.feedback.FeedBack;

public abstract class AbstractToolTipFeedBack<R> implements FeedBack<R> {

	private class LocationAdapter implements ComponentListener, AncestorListener {

		@Override
		public void componentResized(ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentMoved(ComponentEvent e) {
			followOwner();
		}

		@Override
		public void componentShown(ComponentEvent e) {
			// Nothing to be done
		}

		@Override
		public void componentHidden(ComponentEvent e) {
			// Nothing to be done
		}

		@Override
		public void ancestorAdded(AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorRemoved(AncestorEvent event) {
			// Nothing to be done
		}

		@Override
		public void ancestorMoved(AncestorEvent event) {
			followOwner();
		}
	}

	private class TransparencyAdapter extends MouseAdapter implements TimingTarget {

		private float MIN_ALPHA = 0.25f;
		private float MAX_ALPHA = 1.0f;
		private float FADE_OUT_MAX_DURATION = 100;
		private float FADE_IN_MAX_DURATION = 55;

		private float currentAlpha = MAX_ALPHA;

		private Animator animator = null;

		public TransparencyAdapter() {
			TimingSource ts = new SwingTimerTimingSource();
			Animator.setDefaultTimingSource(ts);
			ts.init();
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (isRolloverAnimated()) {
				if ((animator != null) && (animator.isRunning())) {
					animator.stop();
				}
				long duration = (long) ((currentAlpha - MIN_ALPHA) * FADE_OUT_MAX_DURATION / (MAX_ALPHA - MIN_ALPHA));
				if (duration <= 0) {
					timingEvent(null, 0.0);
				} else {
					animator = new Animator.Builder().setDuration(duration, TimeUnit.MILLISECONDS).setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(this).build();
					animator.startReverse();
				}
			} else {
				WindowUtils.setWindowAlpha(popupDialog, MIN_ALPHA);
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			if (isRolloverAnimated()) {
				if ((animator != null) && (animator.isRunning())) {
					animator.stop();
				}

				long duration = (long) ((MAX_ALPHA - currentAlpha) * FADE_IN_MAX_DURATION / (MAX_ALPHA - MIN_ALPHA));
				if (duration <= 0) {
					timingEvent(null, 1.0);
				} else {
					animator = new Animator.Builder().setDuration(duration, TimeUnit.MILLISECONDS).setInterpolator(new SplineInterpolator(0.8, 0.2, 0.2, 0.8)).addTarget(this).build();
					animator.start();
				}
			} else {
				WindowUtils.setWindowAlpha(popupDialog, MAX_ALPHA);
			}
		}

		@Override
		public void begin(Animator animator) {
			// Nothing to be done because we stop the animation manually
		}

		@Override
		public void end(Animator animator) {
			// Nothing to be done because we stop the animation manually
		}

		@Override
		public void repeat(Animator animator) {
			// Nothing to be done
		}

		@Override
		public void reverse(Animator animator) {
			// Nothing to be done
		}

		@Override
		public void timingEvent(Animator animator, double v) {
			currentAlpha = (float) (v * (MAX_ALPHA - MIN_ALPHA)) + MIN_ALPHA;
			WindowUtils.setWindowAlpha(popupDialog, currentAlpha);
		}
	}

	private JComponent owner = null;
	private JDialog popupDialog = null;
	private JToolTip toolTip = new JToolTip();
	private LocationAdapter locationAdapter = new LocationAdapter();
	private boolean rolloverAnimated;

	public AbstractToolTipFeedBack(JComponent owner) {
		this(owner, true);
	}

	public AbstractToolTipFeedBack(JComponent owner, boolean rolloverAnimated) {
		attachComponent(owner);
		this.rolloverAnimated = rolloverAnimated;
	}

	public void attachComponent(JComponent owner) {
		if (this.owner != null) {
			detachComponent(this.owner);
		}

		this.owner = owner;

		if (owner != null) {
			owner.addComponentListener(locationAdapter);

			toolTip.addMouseListener(new TransparencyAdapter());

			popupDialog = new JDialog(SwingUtilities.getWindowAncestor(owner));
			popupDialog.setUndecorated(true);
			popupDialog.getRootPane().setWindowDecorationStyle(JRootPane.NONE); // Just in case new LAF provides decorations
			popupDialog.setFocusable(false); // Just in case...
			popupDialog.setFocusableWindowState(false);
			popupDialog.setResizable(false); // Just in case...
			popupDialog.setContentPane(toolTip);
			popupDialog.pack(); // Seems to help for the very first setVisible(true) when window transparency is on

			if (WindowUtils.isWindowAlphaSupported()) {
				WindowUtils.setWindowTransparent(popupDialog, true);
				owner.addAncestorListener(locationAdapter);
			}
		}
	}

	public void detachComponent(JComponent owner) {
		popupDialog.dispose();
		popupDialog = null;
		this.owner = null;
	}

	public boolean isRolloverAnimated() {
		return rolloverAnimated;
	}

	public void setRolloverAnimated(boolean animated) {
		this.rolloverAnimated = animated;
	}

	protected String getToolTipText() {
		return toolTip.getTipText();
	}

	protected void setToolTipText(String text) {
		toolTip.setTipText(text);
	}

	protected void showToolTip() {
		popupDialog.setSize(toolTip.getPreferredSize());
		if (!popupDialog.isVisible()) {
			popupDialog.setVisible(true);
		}
	}

	protected void hideToolTip() {
		popupDialog.setVisible(false);
	}

	private void followOwner() {
		if ((owner != null) && (popupDialog != null)) {
			Point screenLocation = owner.getLocationOnScreen();
			popupDialog.setLocation(screenLocation.x + owner.getWidth(), screenLocation.y);
		}
	}
}
