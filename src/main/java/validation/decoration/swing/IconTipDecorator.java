package validation.decoration.swing;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Icon;
import javax.swing.JComponent;

import validation.decoration.swing.utils.Anchor;
import validation.decoration.swing.utils.DualAnchor;

public class IconTipDecorator extends AbstractDecorator {

	private class ToolTipAdapter extends MouseAdapter {

		@Override
		public void mouseEntered(MouseEvent e) {
			toolTipDialog.setVisible(true);
		}

		@Override
		public void mouseExited(MouseEvent e) {
			toolTipDialog.setVisible(false);
		}
	}

	private Icon icon = null;

	private ToolTipDialog toolTipDialog = null;

	private static final DualAnchor DEFAULT_DUAL_ANCHOR_WITH_OWNER = new DualAnchor(Anchor.BOTTOM_LEFT, Anchor.CENTER);

	private DualAnchor dualAnchorWithToolTip = new DualAnchor(Anchor.CENTER, Anchor.TOP_RIGHT);

	private boolean visible = true;

	public IconTipDecorator(JComponent owner) {
		this(owner, null);
	}

	public IconTipDecorator(JComponent owner, Icon icon) {
		super(owner, DEFAULT_DUAL_ANCHOR_WITH_OWNER);
		this.icon = icon;

		toolTipDialog = new ToolTipDialog(decorationHolder, dualAnchorWithToolTip);
		decorationHolder.addMouseListener(new ToolTipAdapter());
	}

	public String getText() {
		return toolTipDialog.getText();
	}

	public void setText(String text) {
		toolTipDialog.setText(text);
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(Icon icon) {
		this.icon = icon;
		followOwner();
	}

	public DualAnchor getDualAnchorWithToolTip() {
		return dualAnchorWithToolTip;
	}

	public void setDualAnchorWithToolTip(DualAnchor dualAnchorWithToolTip) {
		this.dualAnchorWithToolTip = dualAnchorWithToolTip;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (!visible && (toolTipDialog != null)) {
			toolTipDialog.setVisible(false);
		}
	}

	@Override
	protected int getWidth() {
		int width = 0;
		if (icon != null) {
			width = icon.getIconWidth();
		}
		return width;
	}

	@Override
	protected int getHeight() {
		int height = 0;
		if (icon != null) {
			height = icon.getIconHeight();
		}
		return height;
	}

	@Override
	public void paint(Graphics g) {
		if (isVisible() && (icon != null)) {
			icon.paintIcon(decorationHolder, g, 0, 0);
		}
	}
}
