package validation.feedback;

import java.awt.Color;

import javax.swing.JComponent;

import validation.utils.ColorUtils;

public abstract class AbstractColorFeedBack<R> implements FeedBack<R> {

	private JComponent owner = null;
	private Color origForeground = null;
	private Color origBackground = null;
	private Color resultForeground = null;
	private Color resultBackground = null;
	private boolean showing = false;

	public AbstractColorFeedBack(JComponent owner) {
		attachComponent(owner);
	}

	public void attachComponent(JComponent owner) {
		if (this.owner != null) {
			detachComponent(this.owner);
		}

		this.owner = owner;
	}

	public void detachComponent(JComponent owner) {
		this.owner = null;
	}

	protected Color getForeground() {
		return resultForeground;
	}

	protected void setForeground(Color foreground) {
		this.resultForeground = foreground;
	}

	protected Color getBackground() {
		return resultBackground;
	}

	protected void setBackground(Color background) {
		this.resultBackground = background;
	}

	protected void showColors() {
		if (!showing) {
			origForeground = owner.getForeground();
			origBackground = owner.getBackground();
		}

		if (resultForeground == null) {
			owner.setForeground(origForeground);
		} else {
			owner.setForeground(ColorUtils.alphaBlend(resultForeground, origForeground));
		}
		if (resultBackground == null) {
			owner.setBackground(origBackground);
		} else {
			owner.setBackground(ColorUtils.alphaBlend(resultBackground, origBackground));
		}
		owner.getParent().repaint();

		showing = true;
	}

	protected void hideColors() {
		if (showing) {
			owner.setForeground(origForeground);
			owner.setBackground(origBackground);
		}

		showing = false;
	}
}
