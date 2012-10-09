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

package com.github.validationframework.swing.resulthandler.bool;

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.swing.decoration.anchor.Anchor;
import com.github.validationframework.swing.decoration.anchor.AnchorLink;
import com.github.validationframework.swing.decoration.support.ToolTipDialog;
import com.github.validationframework.swing.decoration.utils.IconUtils;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Result handler showing an icon in the tab of a specific index inside a tabbed pane.<br>Note that the tab to which it
 * is attached must already be present in the tabbed pane. This restriction will be lifted out in the
 * future.<br>Finally, note that any icon set using the method {@link JTabbedPane#setIconAt(int, Icon)} will not be
 * shown.
 */
public class TabIconBooleanFeedback implements ResultHandler<Boolean>, Disposable {

	/**
	 * Component in charge of rendering the title of the tab, containing the tab title text and the decoration icon.
	 */
	private class TitleRenderer extends JPanel {

		/**
		 * Generated serial UID.
		 */
		private static final long serialVersionUID = -8023773742352528637L;

		private final JLabel textLabel = new JLabel();

		private final JLabel iconLabel = new JLabel();

		private final String iconPositionInLayout;

		private final IconToolTipAdapter toolTipAdapter;

		/**
		 * Flag indicating the last known enabled state of the tab.<br>It is used to check if the enabled state changed when
		 * painting because the {@link JTabbedPane} does not trigger anything when the enabled state of the tabs is changed by
		 * calling {@link JTabbedPane#setEnabledAt(int, boolean)}.
		 *
		 * @see JTabbedPane#setEnabledAt(int, boolean)
		 * @see #paint(Graphics)
		 */
		private boolean titleEnabled = tabbedPane.isEnabledAt(tabIndex);

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
				// Add icon label if not already there, and only if title is enabled
				if ((getComponentCount() < 2) && isEnabled()) {
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

		/**
		 * @see JPanel#setEnabled(boolean)
		 */
		@Override
		public void setEnabled(final boolean enabled) {
			super.setEnabled(enabled);
			iconLabel.setEnabled(enabled);
			textLabel.setEnabled(enabled);

			if (enabled) {
				// Re-add icon label if not already there and if there is an icon to show
				if ((getComponentCount() < 2) && (iconLabel.getIcon() != null)) {
					add(iconLabel, iconPositionInLayout);
				}
			} else {
				// Remove the icon label if it is there
				if (getComponentCount() > 1) {
					remove(iconLabel);
				}
			}
		}

		/**
		 * @see JPanel#paint(Graphics)
		 * @see #titleEnabled
		 */
		@Override
		public void paint(final Graphics g) {
			if (tabbedPane != null) {
				// Check if tab enabled state changed since last paint
				final boolean tabEnabled = tabbedPane.isEnabledAt(tabIndex);
				if (tabEnabled == titleEnabled) {
					// No change in tab enabled state
					super.paint(g);
				} else {
					titleEnabled = tabEnabled;
					setEnabled(titleEnabled);
					// Will paint later
				}
			}
		}
	}

	private static class IconToolTipAdapter implements MouseListener {

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
		 * @see MouseListener#mouseEntered(MouseEvent)
		 */
		@Override
		public void mouseEntered(final MouseEvent e) {
			if (toolTipDialog == null) {
				toolTipDialog = new ToolTipDialog(owner, anchorLinkWithToolTip);
				toolTipDialog.setText(toolTipText);
			}
			if (owner.isEnabled()) { // TODO Listen to owner property changes
				toolTipDialog.setVisible(true);
			}
		}

		/**
		 * @see MouseListener#mouseExited(MouseEvent)
		 */
		@Override
		public void mouseExited(final MouseEvent e) {
			toolTipDialog.setVisible(false);
		}

		/**
		 * @see MouseListener#mousePressed(MouseEvent)
		 */
		@Override
		public void mousePressed(final MouseEvent e) {
			forwardToParent(e);
		}

		/**
		 * @see MouseListener#mouseReleased(MouseEvent)
		 */
		@Override
		public void mouseReleased(final MouseEvent e) {
			forwardToParent(e);
		}

		/**
		 * @see MouseListener#mouseClicked(MouseEvent)
		 */
		@Override
		public void mouseClicked(final MouseEvent e) {
			forwardToParent(e);
		}

		private void forwardToParent(final MouseEvent e) {
			// Find parent tabbed pane
			Container parent = owner.getParent();
			while ((parent != null) && !(parent instanceof JTabbedPane)) {
				parent = parent.getParent();
			}

			// Forward event to tabbed pane if found
			if (parent != null) {
				final MouseEvent transformedEvent = SwingUtilities.convertMouseEvent(owner, e, parent);
				parent.dispatchEvent(transformedEvent);
			}
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
			if (tabbedPane != null) {
				if ("indexForTitle".equals(evt.getPropertyName())) {
					// Update text label with the new title set on the tabbed pane
					final Component title = tabbedPane.getTabComponentAt(tabIndex);
					if (title instanceof TitleRenderer) {
						((TitleRenderer) title).setTitle(tabbedPane.getTitleAt(tabIndex));
					}
				} else if ("enabled".equals(evt.getPropertyName())) {
					// Update title renderers
					for (int i = 0; i < tabbedPane.getTabCount(); i++) {
						final Component title = tabbedPane.getTabComponentAt(i);
						title.setEnabled(tabbedPane.isEnabled());
					}
				}
			}
		}
	}

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(TabIconBooleanFeedback.class);

	/**
	 * Default icon to represent valid results.
	 */
	public static final Icon DEFAULT_VALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/valid.png", TabIconBooleanFeedback.class);

	/**
	 * Default icon to represent invalid results.
	 */
	public static final Icon DEFAULT_INVALID_ICON =
			IconUtils.loadImageIcon("/images/defaults/invalid.png", TabIconBooleanFeedback.class);

	/**
	 * Default icon position with respect to the tab title.
	 */
	public static final int DEFAULT_ICON_POSITION = SwingConstants.LEADING;

	/**
	 * Default spacing between the icon and the tab title.
	 */
	public static final int DEFAULT_ICON_TEXT_GAP = 3;

	/**
	 * Icon representing valid results, or null.
	 */
	private final Icon validIcon;

	/**
	 * Tooltip text on the valid icon explaining the valid results, or null.
	 */
	private final String validText;

	/**
	 * Icon representing invalid results, or null.
	 */
	private final Icon invalidIcon;

	/**
	 * Tooltip text on the invalid icon explaining the invalid results, or null.
	 */
	private final String invalidText;

	/**
	 * Tabbed pane to show the icon tip feedback on.
	 */
	private JTabbedPane tabbedPane = null;

	/**
	 * Index of the tab to show the icon tip feedback on.
	 */
	private final int tabIndex;

	/**
	 * Listener to changes of tab titles.
	 */
	private final PropertyChangeListener tabPropertyAdapter = new TabPropertyAdapter();

	/**
	 * Constructor specifying the tabbed pane and the index of the tab to show the decoration on.
	 *
	 * @param tabbedPane Tabbed pane to show the icon tip feedback on.
	 * @param tabIndex Index of the tab to show the icon tip feedback on.
	 */
	public TabIconBooleanFeedback(final JTabbedPane tabbedPane, final int tabIndex) {
		this(tabbedPane, tabIndex, DEFAULT_VALID_ICON, null, DEFAULT_INVALID_ICON, null);
	}

	/**
	 * Constructor specifying the tabbed pane and the index of the tab to show the decoration on, as well as the icon and
	 * tooltip text representing valid and invalid results.
	 *
	 * @param tabbedPane Tabbed pane to show the icon tip feedback on.
	 * @param tabIndex Index of the tab to show the icon tip feedback on.
	 * @param validIcon Icon representing valid results, or null.
	 * @param validText Tooltip text on the valid icon explaining the valid results, or null.
	 * @param invalidIcon Icon representing invalid results, or null.
	 * @param invalidText Tooltip text on the invalid icon explaining the invalid results, or null.
	 */
	public TabIconBooleanFeedback(final JTabbedPane tabbedPane, final int tabIndex, final Icon validIcon,
								  final String validText, final Icon invalidIcon, final String invalidText) {
		this(tabbedPane, tabIndex, validIcon, validText, invalidIcon, invalidText, DEFAULT_ICON_POSITION,
				DEFAULT_ICON_TEXT_GAP);
	}

	/**
	 * Constructor specifying the tabbed pane and the index of the tab to show the decoration on, as well as the icon and
	 * tooltip text representing valid and invalid results, and the position and spacing of the decoration icon with
	 * respect to the tab title.
	 *
	 * @param tabbedPane Tabbed pane to show the icon tip feedback on.
	 * @param tabIndex Index of the tab to show the icon tip feedback on.
	 * @param validIcon Icon representing valid results, or null.
	 * @param validText Tooltip text on the valid icon explaining the valid results, or null.
	 * @param invalidIcon Icon representing invalid results, or null.
	 * @param invalidText Tooltip text on the invalid icon explaining the invalid results, or null.
	 * @param iconPosition Position of the icon with respect to the tab title.<br>It can be {@link SwingConstants#LEADING},
	 * {@link SwingConstants#LEFT}, {@link SwingConstants#TRAILING} or {@link SwingConstants#RIGHT}.
	 * @param iconTextGap Spacing between the icon and the text.
	 */
	public TabIconBooleanFeedback(final JTabbedPane tabbedPane, final int tabIndex, final Icon validIcon,
								  final String validText, final Icon invalidIcon, final String invalidText,
								  final int iconPosition, final int iconTextGap) {
		this.tabbedPane = tabbedPane;
		this.tabIndex = tabIndex;
		this.validIcon = validIcon;
		this.validText = validText;
		this.invalidIcon = invalidIcon;
		this.invalidText = invalidText;

		this.tabbedPane.addPropertyChangeListener("indexForTitle", tabPropertyAdapter);
		this.tabbedPane.addPropertyChangeListener("enabled", tabPropertyAdapter);

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
				showResult(invalidIcon, invalidText);
			} else {
				showResult(validIcon, validText);
			}
		}
	}

	/**
	 * Shows the result on the tab title renderer.
	 *
	 * @param icon Icon representing the result.
	 * @param toolTipText Tooltip text on the icon.
	 */
	private void showResult(final Icon icon, final String toolTipText) {
		if (tabbedPane != null) {
			final Component title = tabbedPane.getTabComponentAt(tabIndex);
			if (title instanceof TitleRenderer) {
				// Set icon on the custom tab title renderer
				((TitleRenderer) title).setIcon(icon);
				((TitleRenderer) title).setIconToolTipText(toolTipText);
			} else {
				LOGGER.error("Nothing to set the icon on the tabbed pane: " + tabbedPane, icon);
			}
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
