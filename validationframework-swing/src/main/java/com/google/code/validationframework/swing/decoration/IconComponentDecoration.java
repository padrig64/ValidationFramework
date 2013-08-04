/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.google.code.validationframework.swing.decoration;

import com.google.code.validationframework.swing.decoration.anchor.Anchor;
import com.google.code.validationframework.swing.decoration.anchor.AnchorLink;
import com.google.code.validationframework.swing.decoration.support.ToolTipDialog;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Component decoration showing an icon, possibly with a tooltip.
 */
public class IconComponentDecoration extends AbstractComponentDecoration {

    /**
     * Listener to mouse events on the icon and showing/hiding the associated tooltip.
     */
    private class DecorationPainterTracker extends MouseAdapter implements ComponentListener {

        /**
         * @see MouseAdapter#mouseEntered(MouseEvent)
         */
        @Override
        public void mouseEntered(MouseEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see MouseAdapter#mouseMoved(MouseEvent)
         */
        @Override
        public void mouseMoved(MouseEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see MouseAdapter#mouseDragged(MouseEvent)
         */
        @Override
        public void mouseDragged(MouseEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see MouseAdapter#mouseExited(MouseEvent)
         */
        @Override
        public void mouseExited(MouseEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see ComponentListener#componentShown(ComponentEvent)
         */
        @Override
        public void componentShown(ComponentEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see ComponentListener#componentHidden(ComponentEvent)
         */
        @Override
        public void componentHidden(ComponentEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see ComponentListener#componentMoved(ComponentEvent)
         */
        @Override
        public void componentMoved(ComponentEvent e) {
            updateToolTipDialogVisibility();
        }

        /**
         * @see ComponentListener#componentResized(ComponentEvent)
         */
        @Override
        public void componentResized(ComponentEvent e) {
            updateToolTipDialogVisibility();
        }
    }

    /**
     * Default anchor link with the owner component on which the decorator will be attached.
     */
    public static final AnchorLink DEFAULT_ANCHOR_LINK_WITH_OWNER = new AnchorLink(Anchor.BOTTOM_LEFT, Anchor.CENTER);

    /**
     * Icon to be displayed as decoration on the owner component.
     */
    private Icon icon = null;

    /**
     * Dialog representing the tooltip.
     * <p/>
     * The tooltip is not based on the general tooltip mechanism to make so that it does not get influenced by the
     * different timings and tricky mouse behavior (sometimes hard to make a real tooltip appear).
     * <p/>
     * It is lazy-initialized to make sure we will have a parent and a window ancestor (owner of the dialog).
     *
     * @see DecorationPainterTracker#createToolTipDialogIfNeeded()
     */
    private ToolTipDialog toolTipDialog = null;

    /**
     * Listener to mouse events on the icon and showing/hiding the associated tooltip.
     */
    private final DecorationPainterTracker decorationPainterTracker = new DecorationPainterTracker();

    /**
     * Tooltip text to appear on the decoration icon.
     */
    private String toolTipText = null;

    /**
     * Anchor link between the decoration icon and the tooltip.
     */
    private AnchorLink anchorLinkWithToolTip = new AnchorLink(Anchor.BOTTOM_RIGHT, Anchor.TOP_LEFT);

    /**
     * Constructor specifying the component to be decorated.
     *
     * @param owner Component to be decorated.
     */
    public IconComponentDecoration(JComponent owner) {
        this(owner, DEFAULT_ANCHOR_LINK_WITH_OWNER, null);
    }

    /**
     * Constructor specifying the component to be decorated and the anchor link with its owner (the decorated
     * component).
     *
     * @param owner               Component to be decorated.
     * @param anchorLinkWithOwner Anchor link between the decorated component and the decoration icon.
     */
    public IconComponentDecoration(JComponent owner, AnchorLink anchorLinkWithOwner) {
        this(owner, anchorLinkWithOwner, null);
    }

    /**
     * Constructor specifying the component to be decorated and the anchor link with its owner (the decorated
     * component).
     *
     * @param owner Component to be decorated.
     * @param icon  Decoration icon.
     */
    public IconComponentDecoration(JComponent owner, Icon icon) {
        this(owner, DEFAULT_ANCHOR_LINK_WITH_OWNER, icon);
    }

    /**
     * Constructor specifying the component to be decorated and the anchor link with its owner (the decorated
     * component).
     *
     * @param owner               Component to be decorated.
     * @param anchorLinkWithOwner Anchor link between the decorated component and the decoration icon.
     * @param icon                Decoration icon.
     */
    public IconComponentDecoration(JComponent owner, AnchorLink anchorLinkWithOwner, Icon icon) {
        super(owner, anchorLinkWithOwner);
        this.icon = icon;

        decorationPainter.addMouseListener(decorationPainterTracker);
        decorationPainter.addMouseMotionListener(decorationPainterTracker);
        decorationPainter.addComponentListener(decorationPainterTracker);
    }

    /**
     * @see AbstractComponentDecoration#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        decorationPainter.removeMouseListener(decorationPainterTracker);
        decorationPainter.removeMouseMotionListener(decorationPainterTracker);
        decorationPainter.removeComponentListener(decorationPainterTracker);
    }

    /**
     * Gets the decoration icon.
     *
     * @return Decoration icon attached to the owner component.
     */
    public Icon getIcon() {
        return icon;
    }

    /**
     * Sets the decoration.
     *
     * @param icon Decoration icon to be attached to the owner component.
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
        followDecoratedComponent();
    }

    /**
     * Gets the text for the tooltip to be used on this decoration.
     *
     * @return Tooltip text for this decoration, or null.
     */
    public String getToolTipText() {
        return toolTipText;
    }

    /**
     * Sets the text for the tooltip to be used on this decoration.
     *
     * @param text Tooltip text for this decoration, or null if this decoration should have no tooltip.
     */
    public void setToolTipText(String text) {
        this.toolTipText = text;
        if (toolTipDialog != null) {
            toolTipDialog.setText(text);
        }
    }

    /**
     * Gets the anchor link used to slave the tooltip to this decoration.
     *
     * @return Anchor link used to slave the tooltip to this decoration.
     */
    public AnchorLink getAnchorLinkWithToolTip() {
        return anchorLinkWithToolTip;
    }

    /**
     * Sets the anchor link used to slave the tooltip to this decoration.
     *
     * @param anchorLinkWithToolTip Anchor link to be used to slave the tooltip to this decoration.
     */
    public void setAnchorLinkWithToolTip(AnchorLink anchorLinkWithToolTip) {
        this.anchorLinkWithToolTip = anchorLinkWithToolTip;
    }

    /**
     * @see AbstractComponentDecoration#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        updateToolTipDialogVisibility();
    }

    /**
     * Updates the visibility of the tooltip dialog according to the mouse pointer location, the visibility of the
     * decoration painter and the bounds of the decoration painter.
     *
     * @see AbstractComponentDecoration#updateDecorationPainterVisibility()
     */
    private void updateToolTipDialogVisibility() {
        // Get mouse location
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        SwingUtilities.convertPointFromScreen(mouseLocation, decorationPainter);

        // Determine if tooltip dialog should be visible or not
        boolean shouldBeVisible = decorationPainter.isVisible() && decorationPainter.getClipBounds().contains
                (mouseLocation);

        // Create tooltip dialog if needed
        if (shouldBeVisible) {
            createToolTipDialogIfNeeded();
        }

        // Update tooltip dialog visibility if changed
        if (toolTipDialog != null && (toolTipDialog.isVisible() != shouldBeVisible)) {
            toolTipDialog.setVisible(shouldBeVisible);
        }
    }

    /**
     * Creates the dialog showing the tooltip if it is not created yet.
     * <p/>
     * We do this only here to make sure that we have a parent and to make sure that we actually have a window
     * ancestor.
     * <p/>
     * If we create the dialog before having a window ancestor, it will have no owner (see {@link
     * ToolTipDialog#ToolTipDialog(JComponent, AnchorLink)} and that will result in having the tooltip behind the
     * other windows of the application.
     */
    private void createToolTipDialogIfNeeded() {
        if (toolTipDialog == null) {
            toolTipDialog = new ToolTipDialog(decorationPainter, anchorLinkWithToolTip);
        }
        toolTipDialog.setText(toolTipText);
    }

    /**
     * @see AbstractComponentDecoration#getWidth()
     */
    @Override
    protected int getWidth() {
        int width = 0;
        if (icon != null) {
            width = icon.getIconWidth();
        }
        return width;
    }

    /**
     * @see AbstractComponentDecoration#getHeight()
     */
    @Override
    protected int getHeight() {
        int height = 0;
        if (icon != null) {
            height = icon.getIconHeight();
        }
        return height;
    }

    /**
     * @see AbstractComponentDecoration#paint(Graphics)
     */
    @Override
    public void paint(Graphics g) {
        if (icon != null) {
            icon.paintIcon(decorationPainter, g, 0, 0);
        }
    }
}
