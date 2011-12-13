package com.github.validationframework.feedback.swing;

import javax.swing.Icon;
import javax.swing.JComponent;

import com.github.validationframework.decoration.swing.IconDecorator;
import com.github.validationframework.feedback.FeedBack;

public abstract class AbstractIconFeedBack<R> implements FeedBack<R> {

	private IconDecorator decorator = null;

	public AbstractIconFeedBack(JComponent owner) {
		attach(owner);
	}

	public void attach(JComponent owner) {
		detach();

		if (owner != null) {
			decorator = new IconDecorator(owner);
			decorator.setVisible(false);
		}
	}

	public void detach() {
		if (decorator != null) {
			decorator.detach();
			decorator = null;
		}
	}

	protected Icon getIcon() {
		Icon icon = null;
		if (decorator != null) {
			icon = decorator.getIcon();
		}
		return icon;
	}

	protected void setIcon(Icon icon) {
		if (decorator != null) {
			decorator.setIcon(icon);
		}
	}

	protected void showIconTip() {
		if (decorator != null) {
			decorator.setVisible(true);
		}
	}

	protected void hideIconTip() {
		if (decorator != null) {
			decorator.setVisible(false);
		}
	}
}
