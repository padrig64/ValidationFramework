/*
 * Copyright (c) 2012, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.validationframework.swing.resulthandler;

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.swing.decoration.ToolTipDialog;
import com.github.validationframework.swing.decoration.utils.Anchor;
import com.github.validationframework.swing.decoration.utils.AnchorLink;
import com.github.validationframework.swing.decoration.utils.IconUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Result handler showing an icon in the tab of a specific index inside a tabbed pane.<br>Note that the tab to which it
 * is attached must already be present in the tabbed pane. This restriction will be lifted out in the
 * future.<br>Finally, note that any icon set using the method {@link JTabbedPane#setIconAt(int, Icon)} will not be
 * shown.
 */
public class BooleanTabIconTipFeedback implements ResultHandler<Boolean>, Disposable {

	private static class TitleRenderer extends JPanel {

		private static final long serialVersionUID = -8023773742352528637L;

		private final JLabel textLabel = new JLabel();

		private final JLabel iconLabel = new JLabel();

		private final String iconPositionInLayout;

		private final IconToolTipAdapter toolTipAdapter;

		public TitleRenderer(final int iconPosition, final int iconTextGap) {
			super();

			switch (iconPosition) {
				case SwingConstants.LEADING:
				case SwingConstants.LEFT:
					this.iconPositionInLayout = BorderLayout.WEST;
					break;

				case SwingConstants.TRAILING:
				case SwingConstants.RIGHT:
					this.iconPositionInLayout = BorderLayout.EAST;
					break;

				default:
					LOGGER.error("Invalid icon position: " + iconPosition);
					this.iconPositionInLayout = BorderLayout.WEST;
			}

			// Set up background
			setOpaque(false);

			// Set up layout
			final BorderLayout layout = new BorderLayout();
			layout.setHgap(iconTextGap);
			setLayout(layout);

			// Set up text label
			add(textLabel, BorderLayout.CENTER);

			// Set up icon label
			toolTipAdapter =
					new IconToolTipAdapter(iconLabel, null, new AnchorLink(Anchor.BOTTOM_RIGHT, Anchor.TOP_LEFT));
			iconLabel.addMouseListener(toolTipAdapter);
		}

		public void setIcon(final Icon icon) {
			if (icon == null) {
				// Remove icon label completely so that we do not have any unused space and mis-alignments
				if (getComponentCount() > 1) {
					remove(iconLabel);
				}
			} else {
				// Add icon label if not already there
				if (getComponentCount() < 2) {
					add(iconLabel, iconPositionInLayout);
				}
			}

			// Update icon label
			iconLabel.setIcon(icon);
		}

		public void setIconToolTipText(final String text) {
			toolTipAdapter.setToolTipText(text);
		}

		public void setTitle(final String text) {
			// Update text label
			textLabel.setText(text);
		}
	}

	private static class IconToolTipAdapter extends MouseAdapter {

		private ToolTipDialog toolTipDialog = null;

		private final JComponent owner;

		private String toolTipText = "";

		private final AnchorLink anchorLinkWithToolTip;

		public IconToolTipAdapter(final JComponent owner, final String toolTipText,
								  final AnchorLink anchorLinkWithToolTip) {
			super();
			this.owner = owner;
			this.anchorLinkWithToolTip = anchorLinkWithToolTip;
			setToolTipText(toolTipText);
		}

		/**
		 * @see java.awt.event.MouseListener#mouseEntered(MouseEvent)
		 */
		@Override
		public void mouseEntered(final MouseEvent e) {
			if (toolTipDialog == null) {
				toolTipDialog = new ToolTipDialog(owner, anchorLinkWithToolTip);
				toolTipDialog.setText(toolTipText);
			}
			toolTipDialog.setVisible(true);
		}

		/**
		 * @see java.awt.event.MouseListener#mouseExited(MouseEvent)
		 */
		@Override
		public void mouseExited(final MouseEvent e) {
			toolTipDialog.setVisible(false);
		}

		public void setToolTipText(final String toolTipText) {
			this.toolTipText = toolTipText;
			if (toolTipDialog != null) {
				toolTipDialog.setText(toolTipText);
			}
		}
	}

	private class TabPropertyAdapter implements PropertyChangeListener {

		/**
		 * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
		 */
		@Override
		public void propertyChange(final PropertyChangeEvent evt) {
			if ("indexForTitle".equals(evt.getPropertyName())) {
				// Update text label with the new title set on the tabbed pane
				final Component title = tabbedPane.getTabComponentAt(tabIndex);
				if (title instanceof TitleRenderer) {
					((TitleRenderer) title).setTitle(tabbedPane.getTitleAt(tabIndex));
				}
			}
		}
	}

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(BooleanTabIconTipFeedback.class);

	public static final Icon DEFAULT_VALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/valid.png", BooleanTabIconTipFeedback.class);

	public static final Icon DEFAULT_INVALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/invalid.png", BooleanTabIconTipFeedback.class);

	public static final int DEFAULT_ICON_POSITION = SwingConstants.LEADING;

	public static final int DEFAULT_ICON_TEXT_GAP = 5;

	private final Icon validIcon;

	private final String validText;

	private final Icon invalidIcon;

	private final String invalidText;

	private JTabbedPane tabbedPane = null;

	private final int tabIndex;

	private final PropertyChangeListener tabPropertyAdapter = new TabPropertyAdapter();

	public BooleanTabIconTipFeedback(final JTabbedPane tabbedPane, final int tabIndex) {
		this(tabbedPane, tabIndex, DEFAULT_VALID_ICON, null, DEFAULT_INVALID_ICON, null);
	}

	public BooleanTabIconTipFeedback(final JTabbedPane tabbedPane, final int tabIndex, final Icon validIcon,
									 final String validText, final Icon invalidIcon, final String invalidText) {
		this(tabbedPane, tabIndex, validIcon, validText, invalidIcon, invalidText, DEFAULT_ICON_POSITION,
				DEFAULT_ICON_TEXT_GAP);
	}

	public BooleanTabIconTipFeedback(final JTabbedPane tabbedPane, final int tabIndex, final Icon validIcon,
									 final String validText, final Icon invalidIcon, final String invalidText,
									 final int iconPosition, final int iconTextGap) {
		this.tabbedPane = tabbedPane;
		this.tabIndex = tabIndex;
		this.validIcon = validIcon;
		this.validText = validText;
		this.invalidIcon = invalidIcon;
		this.invalidText = invalidText;

		this.tabbedPane.addPropertyChangeListener(tabPropertyAdapter);

		// Create tab title renderer
		final TitleRenderer customTitleRenderer = new TitleRenderer(iconPosition, iconTextGap);
		customTitleRenderer.setIcon(tabbedPane.getIconAt(tabIndex));
		customTitleRenderer.setTitle(tabbedPane.getTitleAt(tabIndex));

		// Apply title component to tab
		tabbedPane.setTabComponentAt(tabIndex, customTitleRenderer);
	}

	/**
	 * @see ResultHandler#handleResult(Object)
	 */
	@Override
	public void handleResult(final Boolean result) {
		if (tabbedPane != null) {
			if ((result == null) || !result) {
				applyResult(invalidIcon, invalidText);
			} else {
				applyResult(validIcon, validText);
			}
		}
	}

	private void applyResult(final Icon icon, final String toolTipText) {
		final Component title = tabbedPane.getTabComponentAt(tabIndex);
		if (title instanceof TitleRenderer) {
			// Set icon on the custom tab title renderer
			((TitleRenderer) title).setIcon(icon);
			((TitleRenderer) title).setIconToolTipText(toolTipText);
		} else {
			LOGGER.error("Nothing to set the icon on the tabbed pane: " + tabbedPane, icon);
		}
	}

	/**
	 * @see Disposable#dispose()
	 */
	@Override
	public void dispose() {
		if (tabbedPane != null) {
			tabbedPane.removePropertyChangeListener(tabPropertyAdapter);
			tabbedPane.setTabComponentAt(tabIndex, null);
			tabbedPane = null;
		}
	}
}
