package validation.feedback.swing;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComponent;

import validation.decoration.swing.IconTipDecorator;
import validation.feedback.FeedBack;

public abstract class AbstractIconTipFeedBack<R> implements FeedBack<R> {

	private JComponent owner = null;
	private IconTipDecorator decorator = null;

	public AbstractIconTipFeedBack(JComponent owner) {
		attachComponent(owner);
	}

	public void attachComponent(JComponent owner) {
		if (this.owner != null) {
			detachComponent(this.owner);
		}

		this.owner = owner;

		if (owner != null) {
			decorator = new IconTipDecorator(owner);
			decorator.setVisible(false);
			InputStream inputStream = getClass().getResourceAsStream("/icons/invalid3.png");
			try {
				BufferedImage image = ImageIO.read(inputStream);
				ImageIcon icon = new ImageIcon(image);
				decorator.setIcon(icon);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void detachComponent(JComponent owner) {
		this.owner = null;

		if (decorator != null) {
			decorator.dispose();
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

	protected String getToolTipText() {
		String tip = null;
		if (decorator != null) {
			tip = decorator.getText();
		}
		return tip;
	}

	protected void setToolTipText(String text) {
		if (decorator != null) {
			decorator.setText(text);
		}
	}

	protected void showIconTip() {
		if ((decorator != null) && !decorator.isVisible()) {
			decorator.setVisible(true);
		}
	}

	protected void hideIconTip() {
		if (decorator != null) {
			decorator.setVisible(false);
		}
	}
}
