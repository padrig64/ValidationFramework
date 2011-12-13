package com.github.validationframework.feedback.swing;

import javax.swing.JComponent;

import com.github.validationframework.decoration.swing.ToolTipDialog;
import com.github.validationframework.decoration.swing.utils.Anchor;
import com.github.validationframework.decoration.swing.utils.DualAnchor;
import com.github.validationframework.feedback.FeedBack;

public abstract class AbstractToolTipFeedBack<R> implements FeedBack<R> {

	private ToolTipDialog toolTipDialog = null;

	public AbstractToolTipFeedBack(JComponent owner) {
		attach(owner);
	}

	public void attach(JComponent owner) {
		detach();
		toolTipDialog = new ToolTipDialog(owner, new DualAnchor(Anchor.TOP_RIGHT, Anchor.TOP_LEFT));
	}

	public void detach() {
		if (toolTipDialog != null) {
			toolTipDialog.dispose();
			toolTipDialog = null;
		}
	}

	protected String getToolTipText() {
		String tip = null;

		if (toolTipDialog != null) {
			tip = toolTipDialog.getText();
		}

		return tip;
	}

	protected void setToolTipText(String text) {
		if (toolTipDialog != null) {
			toolTipDialog.setText(text);
		}
	}

	protected void showToolTip() {
		toolTipDialog.setVisible(true);
	}

	protected void hideToolTip() {
		toolTipDialog.setVisible(false);
	}
}