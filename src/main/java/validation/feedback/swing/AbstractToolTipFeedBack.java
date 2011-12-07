package validation.feedback.swing;

import javax.swing.JComponent;

import validation.decoration.swing.ToolTipDialog;
import validation.decoration.swing.utils.Anchor;
import validation.decoration.swing.utils.DualAnchor;
import validation.feedback.FeedBack;

public abstract class AbstractToolTipFeedBack<R> implements FeedBack<R> {

	private ToolTipDialog toolTipDialog = null;

	public AbstractToolTipFeedBack(JComponent owner) {
		attachComponent(owner);
	}

	public void attachComponent(JComponent owner) {
		if (toolTipDialog != null) {
			toolTipDialog.dispose();
		}
		toolTipDialog = new ToolTipDialog(owner, new DualAnchor(Anchor.TOP_RIGHT, Anchor.TOP_LEFT));
	}

	public void detachComponent(JComponent owner) {
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
