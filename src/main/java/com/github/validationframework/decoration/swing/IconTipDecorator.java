package com.github.validationframework.decoration.swing;

import com.github.validationframework.decoration.swing.utils.Anchor;
import com.github.validationframework.decoration.swing.utils.DualAnchor;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Icon;
import javax.swing.JComponent;

public class IconTipDecorator extends AbstractDecorator {

	private class ToolTipAdapter extends MouseAdapter {

		@Override
		public void mouseEntered(final MouseEvent e) {
			toolTipDialog.setVisible(true);
		}

		@Override
		public void mouseExited(final MouseEvent e) {
			toolTipDialog.setVisible(false);
		}
	}

	private Icon icon = null;

	private ToolTipDialog toolTipDialog = null;

	private static final DualAnchor DEFAULT_DUAL_ANCHOR_WITH_OWNER = new DualAnchor(Anchor.BOTTOM_LEFT, Anchor.CENTER);

	private DualAnchor dualAnchorWithToolTip = new DualAnchor(Anchor.CENTER, Anchor.TOP_RIGHT);

	public IconTipDecorator(final JComponent owner) {
		this(owner, null);
	}

	public IconTipDecorator(final JComponent owner, final Icon icon) {
		super(owner, DEFAULT_DUAL_ANCHOR_WITH_OWNER);
		this.icon = icon;

		toolTipDialog = new ToolTipDialog(decorationHolder, dualAnchorWithToolTip);
		decorationHolder.addMouseListener(new ToolTipAdapter());
	}

	public String getText() {
		return toolTipDialog.getText();
	}

	public void setText(final String text) {
		toolTipDialog.setText(text);
	}

	public Icon getIcon() {
		return icon;
	}

	public void setIcon(final Icon icon) {
		this.icon = icon;
		followOwner();
	}

	public DualAnchor getDualAnchorWithToolTip() {
		return dualAnchorWithToolTip;
	}

	public void setDualAnchorWithToolTip(final DualAnchor dualAnchorWithToolTip) {
		this.dualAnchorWithToolTip = dualAnchorWithToolTip;
	}

	@Override
	public void setVisible(final boolean visible) {
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
	public void paint(final Graphics g) {
		if (isVisible() && (icon != null)) {
			icon.paintIcon(decorationHolder, g, 0, 0);
		}
	}
}
