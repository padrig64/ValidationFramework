/*
 * Copyright (c) 2014, Patrick Moawad
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

package com.google.code.validationframework.swing.resulthandler.bool;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import com.google.code.validationframework.base.property.wrap.NegateBooleanPropertyWrapper;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;
import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
import com.google.code.validationframework.swing.decoration.support.ToolTipDialog;
import com.google.code.validationframework.swing.decoration.utils.IconUtils;
import com.google.code.validationframework.swing.property.ComponentEnabledProperty;
import com.google.code.validationframework.swing.property.ComponentVisibleProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import static com.google.code.validationframework.base.binding.Binder.read;

/**
 * Result handler showing an icon in the tab of a specific index inside a tabbed pane.
 * <p/>
 * Note that the tab to which it is attached must already be present in the tabbed pane. This restriction will be lifted
 * out in the future.
 * <p/>
 * Finally, note that any icon set using the method {@link JTabbedPane#setIconAt(int, Icon)} will not be shown.
 */
public class TabIconBooleanFeedback implements ResultHandler<Boolean>, Disposable {

    /**
     * Panel holding the validation icons (valid and invalid).
     * <p/>
     * It is used to easily add the extra spacing between the displayed icon and the tab title.
     */
    private static class ValidationIconPanel extends JPanel {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = -8668703592473240174L;

        /**
         * Extra space to be added in the preferred width.
         */
        private final int extraWidth;

        /**
         * Constructor specifying the extra space to be added in the preferred width, and all components holding the
         * validation icons to be added as children.
         *
         * @param extraWidth Extra space to be added in the preferred width.
         * @param children   Components holding the validation icons.
         */
        public ValidationIconPanel(int extraWidth, JComponent... children) {
            super(new FlowLayout(FlowLayout.LEADING, 0, 0));

            this.extraWidth = extraWidth;
            setOpaque(false);

            for (JComponent child : children) {
                child.setOpaque(false);
                add(child);
            }
        }

        /**
         * @see JPanel#getPreferredSize()
         */
        @Override
        public Dimension getPreferredSize() {
            Dimension size = new Dimension();

            for (Component component : getComponents()) {
                Dimension componentSize = component.getPreferredSize();
                if (component.isVisible()) {
                    size.width += componentSize.width;
                }
                size.height = Math.max(size.height, componentSize.height);
            }

            if (size.width > 0) {
                size.width += extraWidth;
            }

            return size;
        }
    }

    /**
     * Component in charge of rendering the title of the tab, containing the tab title text and the decoration icon.
     */
    private class TitleRenderer extends JPanel {

        /**
         * Generated serial UID.
         */
        private static final long serialVersionUID = -8023773742352528637L;

        /**
         * Label that will hold the tab title.
         */
        private final JLabel titleLabel;

        /**
         * Label that will hold the valid result icon.
         */
        private final JLabel validIconLabel;

        /**
         * Label that will hold the invalid result icon.
         */
        private final JLabel invalidIconLabel;

        /**
         * Flag indicating the last known enabled state of the tab.
         * <p/>
         * It is used to check if the enabled state changed when painting because the {@link JTabbedPane} does not
         * trigger anything when the enabled state of the tabs is changed by calling {@link JTabbedPane#setEnabledAt
         * (int, boolean)}.
         *
         * @see JTabbedPane#setEnabledAt(int, boolean)
         * @see #paint(Graphics)
         */
        private boolean paintTitleAsEnabled;

        /**
         * Property holding the validation result to be displayed.
         */
        private final ReadableWritableProperty<Boolean, Boolean> lastResultProperty = new SimpleBooleanProperty();

        /**
         * Constructor specifying the layout constraints of the icon and title text.
         *
         * @param iconPosition Position of the icon with respect to the text.<br>
         *                     It can be either {@link SwingConstants#LEADING}, {@link SwingConstants#TRAILING},
         *                     {@link SwingConstants#LEFT} or {@link SwingConstants#RIGHT}.
         * @param iconTextGap  Gap between the icon and the text.
         */
        public TitleRenderer(int tabIndex, int iconPosition, int iconTextGap, Icon validIcon, String validText,
                             Icon invalidIcon, String invalidText) {
            super(new BorderLayout(0, 0));
            setOpaque(false);

            paintTitleAsEnabled = tabbedPane.isEnabled() && tabbedPane.isEnabledAt(tabIndex);

            ReadableProperty<Boolean> tabEnabledProperty = new ComponentEnabledProperty(this);

            // Initialize the label showing the title
            titleLabel = new JLabel(tabbedPane.getTitleAt(tabIndex));
            read(tabEnabledProperty).write(new ComponentEnabledProperty(titleLabel));
            add(titleLabel, BorderLayout.CENTER);

            // Initialize the label showing the validation icons
            validIconLabel = new JLabel();
            validIconLabel.setIcon(validIcon);
            read(tabEnabledProperty).write(new ComponentEnabledProperty(validIconLabel));
            invalidIconLabel = new JLabel();
            invalidIconLabel.setIcon(invalidIcon);
            read(tabEnabledProperty).write(new ComponentEnabledProperty(invalidIconLabel));

            // Initialize the panel holding the validation icons
            ValidationIconPanel iconPanel = new ValidationIconPanel(iconTextGap, validIconLabel, invalidIconLabel);
            read(tabEnabledProperty).write(new ComponentEnabledProperty(iconPanel));
            add(iconPanel, getBorderLayoutConstraint(iconPosition));

            // Handle the visibility of the validation icons
            ComponentVisibleProperty validIconVisibleProperty = new ComponentVisibleProperty(validIconLabel);
            read(lastResultProperty, tabEnabledProperty).transform(new AndBooleanAggregator()).write
                    (validIconVisibleProperty);
            ComponentVisibleProperty invalidIconVisibleProperty = new ComponentVisibleProperty(invalidIconLabel);
            read(new NegateBooleanPropertyWrapper(lastResultProperty), tabEnabledProperty) //
                    .transform(new AndBooleanAggregator()) //
                    .write(invalidIconVisibleProperty);

            // Initialize validation result tooltips
            IconToolTipAdapter toolTipAdapter = new IconToolTipAdapter(validIconLabel, validText,
                    new AnchorLink(Anchor.BOTTOM_RIGHT, Anchor.TOP_LEFT));
            validIconLabel.addMouseListener(toolTipAdapter);
            toolTipAdapter = new IconToolTipAdapter(invalidIconLabel, invalidText,
                    new AnchorLink(Anchor.BOTTOM_RIGHT, Anchor.TOP_LEFT));
            invalidIconLabel.addMouseListener(toolTipAdapter);
        }

        /**
         * Gets a constraint to be used in a border layout from the specified {@link SwingConstants} position.
         *
         * @param position Position constant.
         *
         * @return Border layout constraint.
         */
        private String getBorderLayoutConstraint(int position) {
            String constraint;

            switch (position) {
                case SwingConstants.LEADING:
                case SwingConstants.LEFT:
                    constraint = BorderLayout.WEST;
                    break;

                case SwingConstants.TRAILING:
                case SwingConstants.RIGHT:
                    constraint = BorderLayout.EAST;
                    break;

                default:
                    LOGGER.error("Invalid icon position: " + position);
                    constraint = BorderLayout.WEST;
            }

            return constraint;
        }

        /**
         * Sets the tab title.
         *
         * @param text Tab title.
         */
        public void setTitle(String text) {
            // Update text label
            titleLabel.setText(text);
        }

        /**
         * @see JPanel#paint(Graphics)
         * @see #paintTitleAsEnabled
         */
        @Override
        public void paint(Graphics g) {
            if (tabbedPane != null) {
                // Check if tab enabled state changed since last paint
                boolean tabEnabled = tabbedPane.isEnabled() && tabbedPane.isEnabledAt(tabIndex);
                if (tabEnabled == paintTitleAsEnabled) {
                    // No change in tab enabled state since last time
                    super.paint(g);
                } else {
                    // Change in tab enabled state
                    paintTitleAsEnabled = tabEnabled;
                    if (paintTitleAsEnabled == isEnabled()) {
                        super.paint(g);
                    } else {
                        setEnabled(paintTitleAsEnabled);
                        // Will repaint later anyway
                    }
                }
            }
        }

        /**
         * Shows the result on the tab title renderer.
         *
         * @param result Result to be shown.
         */
        public void setResult(Boolean result) {
            lastResultProperty.setValue((result != null) && result);
        }
    }

    /**
     * Entity handling the tooltip display for the validation icon.
     * <p/>
     * It adjust the text to be displayed, shows the tooltip whenever the mouse enters the validation icon, and forward
     * mouse events to the parent component (the title renderer).
     */
    private static class IconToolTipAdapter implements MouseListener {

        /**
         * Tooltip component.
         */
        private ToolTipDialog toolTipDialog = null;

        /**
         * Component owning the tooltip.
         */
        private final JComponent owner;

        /**
         * Tooltip text.
         */
        private String toolTipText = "";

        /**
         * Anchor between the tooltip and its owner.
         */
        private final AnchorLink anchorLinkWithToolTip;

        /**
         * Constructor.
         *
         * @param owner                 Component owning the tooltip.
         * @param toolTipText           Tooltip text.
         * @param anchorLinkWithToolTip Anchor between the tooltip and its owner.
         */
        public IconToolTipAdapter(JComponent owner, String toolTipText, AnchorLink anchorLinkWithToolTip) {
            super();
            this.owner = owner;
            this.anchorLinkWithToolTip = anchorLinkWithToolTip;
            setToolTipText(toolTipText);
        }

        /**
         * @see MouseListener#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
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
        public void mouseExited(MouseEvent e) {
            toolTipDialog.setVisible(false);
        }

        /**
         * @see MouseListener#mousePressed(MouseEvent)
         */
        @Override
        public void mousePressed(MouseEvent e) {
            forwardToParent(e);
        }

        /**
         * @see MouseListener#mouseReleased(MouseEvent)
         */
        @Override
        public void mouseReleased(MouseEvent e) {
            forwardToParent(e);
        }

        /**
         * @see MouseListener#mouseClicked(MouseEvent)
         */
        @Override
        public void mouseClicked(MouseEvent e) {
            forwardToParent(e);
        }

        /**
         * Forwards the specified mouse events to the parent tabbed pane.
         *
         * @param e Mouse event to be forwarded.
         */
        private void forwardToParent(MouseEvent e) {
            // Find parent tabbed pane
            Container parent = owner.getParent();
            while ((parent != null) && !(parent instanceof JTabbedPane)) {
                parent = parent.getParent();
            }

            // Forward event to tabbed pane if found
            if (parent != null) {
                MouseEvent transformedEvent = SwingUtilities.convertMouseEvent(owner, e, parent);
                parent.dispatchEvent(transformedEvent);
            }
        }

        /**
         * Sets the tooltip text to be displayed for the icon.
         *
         * @param toolTipText Tooltip text to be displayed for the icon.
         */
        public void setToolTipText(String toolTipText) {
            this.toolTipText = toolTipText;
            if (toolTipDialog != null) {
                toolTipDialog.setText(toolTipText);
            }
        }
    }

    /**
     * Entity tracking the properties of the tab and updates its title renderer accordingly.
     */
    private class TabPropertyAdapter implements PropertyChangeListener {

        /**
         * @see PropertyChangeListener#propertyChange(PropertyChangeEvent)
         */
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            if (tabbedPane != null) {
                if ("indexForTitle".equals(evt.getPropertyName())) {
                    // Update text label with the new title set on the tabbed pane
                    Component title = tabbedPane.getTabComponentAt(tabIndex);
                    if (title instanceof TitleRenderer) {
                        ((TitleRenderer) title).setTitle(tabbedPane.getTitleAt(tabIndex));
                    }
                } else if ("enabled".equals(evt.getPropertyName())) {
                    // Enable/disabled title renderer as well to make it look like enabled/disabled
                    if (tabIndex < tabbedPane.getTabCount()) {
                        Component title = tabbedPane.getTabComponentAt(tabIndex);
                        if (title instanceof TitleRenderer) {
                            title.setEnabled(tabbedPane.isEnabled() && tabbedPane.isEnabledAt(tabIndex));
                        }
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
    public static final Icon DEFAULT_VALID_ICON = IconUtils.loadImageIcon("/images/defaults/valid.png",
            TabIconBooleanFeedback.class);

    /**
     * Default icon to represent invalid results.
     */
    public static final Icon DEFAULT_INVALID_ICON = IconUtils.loadImageIcon("/images/defaults/invalid.png",
            TabIconBooleanFeedback.class);

    /**
     * Default icon position with respect to the tab title.
     */
    public static final int DEFAULT_ICON_POSITION = SwingConstants.LEADING;

    /**
     * Default spacing between the icon and the tab title.
     */
    public static final int DEFAULT_ICON_TEXT_GAP = 3;

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
     * <p/>
     * The default valid icon and default invalid icons will be used. No tooltip will be shown for these icons.
     *
     * @param tabbedPane Tabbed pane to show the icon tip feedback on.
     * @param tabIndex   Index of the tab to show the icon tip feedback on.
     */
    public TabIconBooleanFeedback(JTabbedPane tabbedPane, int tabIndex) {
        this(tabbedPane, tabIndex, DEFAULT_VALID_ICON, null, DEFAULT_INVALID_ICON, null);
    }

    /**
     * Constructor specifying the tabbed pane, the index of the tab to show the decoration on, and the tooltip text to
     * be shown for the invalid icon.
     * <p/>
     * No valid icon will be shown, and the default invalid icon will be used.
     *
     * @param tabbedPane  Tabbed pane to show the icon tip feedback on.
     * @param tabIndex    Index of the tab to show the icon tip feedback on.
     * @param invalidText Tooltip text to be shown for the invalid icon, or null.
     */
    public TabIconBooleanFeedback(JTabbedPane tabbedPane, int tabIndex, String invalidText) {
        this(tabbedPane, tabIndex, null, null, DEFAULT_INVALID_ICON, invalidText, DEFAULT_ICON_POSITION,
                DEFAULT_ICON_TEXT_GAP);
    }

    /**
     * Constructor specifying the tabbed pane, the index of the tab to show the decoration on, the invalid icon and the
     * tooltip text to be shown for the invalid icon.
     * <p/>
     * No valid icon will be shown.
     *
     * @param tabbedPane  Tabbed pane to show the icon tip feedback on.
     * @param tabIndex    Index of the tab to show the icon tip feedback on.
     * @param invalidIcon Icon representing invalid results, or null.
     * @param invalidText Tooltip text to be shown for the invalid icon, or null.
     */
    public TabIconBooleanFeedback(JTabbedPane tabbedPane, int tabIndex, Icon invalidIcon, String invalidText) {
        this(tabbedPane, tabIndex, null, null, invalidIcon, invalidText, DEFAULT_ICON_POSITION, DEFAULT_ICON_TEXT_GAP);
    }

    /**
     * Constructor specifying the tabbed pane and the index of the tab to show the decoration on, as well as the icon
     * and tooltip text representing valid and invalid results.
     *
     * @param tabbedPane  Tabbed pane to show the icon tip feedback on.
     * @param tabIndex    Index of the tab to show the icon tip feedback on.
     * @param validIcon   Icon representing valid results, or null.
     * @param validText   Tooltip text on the valid icon explaining the valid results, or null.
     * @param invalidIcon Icon representing invalid results, or null.
     * @param invalidText Tooltip text on the invalid icon explaining the invalid results, or null.
     */
    public TabIconBooleanFeedback(JTabbedPane tabbedPane, int tabIndex, Icon validIcon, String validText,
                                  Icon invalidIcon, String invalidText) {
        this(tabbedPane, tabIndex, validIcon, validText, invalidIcon, invalidText, DEFAULT_ICON_POSITION,
                DEFAULT_ICON_TEXT_GAP);
    }

    /**
     * Constructor specifying the tabbed pane and the index of the tab to show the decoration on, as well as the icon
     * and tooltip text representing valid and invalid results, and the position and spacing of the decoration icon with
     * respect to the tab title.
     *
     * @param tabbedPane   Tabbed pane to show the icon tip feedback on.
     * @param tabIndex     Index of the tab to show the icon tip feedback on.
     * @param validIcon    Icon representing valid results, or null.
     * @param validText    Tooltip text on the valid icon explaining the valid results, or null.
     * @param invalidIcon  Icon representing invalid results, or null.
     * @param invalidText  Tooltip text on the invalid icon explaining the invalid results, or null.
     * @param iconPosition Position of the icon with respect to the tab title.<br>
     *                     It can be {@link SwingConstants#LEADING}, {@link SwingConstants#LEFT},
     *                     {@link SwingConstants#TRAILING} or {@link SwingConstants#RIGHT}.
     * @param iconTextGap  Spacing between the icon and the text.
     */
    public TabIconBooleanFeedback(JTabbedPane tabbedPane, int tabIndex, Icon validIcon, String validText,
                                  Icon invalidIcon, String invalidText, int iconPosition, int iconTextGap) {
        this.tabbedPane = tabbedPane;
        this.tabIndex = tabIndex;

        this.tabbedPane.addPropertyChangeListener("indexForTitle", tabPropertyAdapter);
        this.tabbedPane.addPropertyChangeListener("enabled", tabPropertyAdapter);

        // Create tab title renderer
        TitleRenderer customTitleRenderer = new TitleRenderer(tabIndex, iconPosition, iconTextGap, validIcon,
                validText, invalidIcon, invalidText);

        // Set title renderer initial state, what will update its preferred size
        customTitleRenderer.setEnabled(tabbedPane.isEnabled() && tabbedPane.isEnabledAt(tabIndex));
        customTitleRenderer.setTitle(tabbedPane.getTitleAt(tabIndex));

        // Apply title component to tab
        tabbedPane.setTabComponentAt(tabIndex, customTitleRenderer);
    }

    /**
     * @see ResultHandler#handleResult(Object)
     */
    @Override
    public void handleResult(Boolean result) {
        if (tabbedPane != null) {
            Component title = tabbedPane.getTabComponentAt(tabIndex);
            if (title instanceof TitleRenderer) {
                ((TitleRenderer) title).setResult(result);
            } else {
                LOGGER.error("No tabbed pane title renderer to show the result");
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
