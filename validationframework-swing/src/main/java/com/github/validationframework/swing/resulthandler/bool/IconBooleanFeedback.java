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

package com.github.validationframework.swing.resulthandler.bool;

import com.github.validationframework.swing.decoration.utils.IconUtils;
import com.github.validationframework.swing.resulthandler.AbstractIconFeedback;

import javax.swing.Icon;
import javax.swing.JComponent;

/**
 * Concrete implementation of an icon feedback result handler handling boolean results.
 *
 * @see AbstractIconFeedback
 */
public class IconBooleanFeedback extends AbstractIconFeedback<Boolean> {

    /**
     * Default icon to be used for valid results.
     */
    public static final Icon DEFAULT_VALID_ICON = IconUtils.loadImageIcon("/images/defaults/valid.png",
            IconBooleanFeedback.class);

    /**
     * Default icon to be used for invalid results.
     */
    public static final Icon DEFAULT_INVALID_ICON = IconUtils.loadImageIcon("/images/defaults/invalid.png",
            IconBooleanFeedback.class);

    /**
     * Icon to be used for valid results.<br>If null, valid results will not be represented.
     */
    private Icon validIcon = null;

    /**
     * Icon to be used for invalid results.<br>If null, invalid results will not be represented.
     */
    private Icon invalidIcon = null;

    /**
     * Tooltip text on the icon used for valid results.<br>If null, the valid icon will have no tooltip.
     */
    private String validText = null;

    /**
     * Tooltip text on the icon used for invalid results.<br>If null, the invalid icon will have no tooltip.
     */
    private String invalidText = null;

    /**
     * Last result processed by this result handler.
     */
    private Boolean lastResult = null;

    /**
     * Constructor specifying the component to which the decoration will be attached.<br>By default, the decoration will
     * show the default valid and invalid icons without any tooltip.
     *
     * @param owner Component to which the decoration will be attached.
     */
    public IconBooleanFeedback(final JComponent owner) {
        this(owner, DEFAULT_VALID_ICON, DEFAULT_INVALID_ICON);
    }

    /**
     * Constructor specifying the component to which the decoration will be attached and the tooltip text for the
     * invalid icon.<br>By default, the decoration will use the default invalid icon, but will have no valid icon.
     *
     * @param owner       Component to which the decoration will be attached.
     * @param invalidText Tooltip text for the invalid icon.
     */
    public IconBooleanFeedback(final JComponent owner, final String invalidText) {
        this(owner, null, null, DEFAULT_INVALID_ICON, invalidText);
    }

    /**
     * Constructor specifying the component to which the decoration will be attached, as well as the invalid icon and
     * its tooltip text.<br>By default, the decoration will have no valid icon.
     *
     * @param owner       Component to which the decoration will be attached.
     * @param invalidIcon Icon to be used for invalid results.
     * @param invalidText Tooltip text for the invalid icon.
     */
    public IconBooleanFeedback(final JComponent owner, final Icon invalidIcon, final String invalidText) {
        this(owner, null, null, invalidIcon, invalidText);
    }

    /**
     * Constructor specifying the component to which the decoration will be attached, as well as the valid and invalid
     * icons.<br>By default, the icons will have no tooltip.
     *
     * @param owner       Component to which the decoration will be attached.
     * @param validIcon   Icon to be used for valid results.
     * @param invalidIcon Icon to be used for invalid results.
     */
    public IconBooleanFeedback(final JComponent owner, final Icon validIcon, final Icon invalidIcon) {
        this(owner, validIcon, null, invalidIcon, null);
    }

    /**
     * Constructor specifying the component to which the decoration will be attached, as well as the tooltip texts for
     * the valid and invalid icons.<br>The default valid and invalid icons will be used.
     *
     * @param owner       Component to which the decoration will be attached.
     * @param validText   Tooltip text for the valid icon.
     * @param invalidText Tooltip text for the invalid icon.
     */
    public IconBooleanFeedback(final JComponent owner, final String validText, final String invalidText) {
        this(owner, DEFAULT_VALID_ICON, validText, DEFAULT_INVALID_ICON, invalidText);
    }

    /**
     * Constructor specifying the component to which the decoration will be attached, as well as the valid and invalid
     * icons and their tooltip texts.
     *
     * @param owner       Component to which the decoration will be attached.
     * @param validIcon   Icon to be used for valid results.
     * @param validText   Tooltip text for the valid icon.
     * @param invalidIcon Icon to be used for invalid results.
     * @param invalidText Tooltip text for the invalid icon.
     */
    public IconBooleanFeedback(final JComponent owner, final Icon validIcon, final String validText,
                               final Icon invalidIcon, final String invalidText) {
        super(owner);

        setValidIcon(validIcon);
        setValidText(validText);
        setInvalidIcon(invalidIcon);
        setInvalidText(invalidText);
    }

    /**
     * Gets the icon used for valid results.
     *
     * @return Icon used for valid results.
     */
    public Icon getValidIcon() {
        return validIcon;
    }

    /**
     * Sets the icon to be used for valid results.
     *
     * @param validIcon Icon to be used for valid results.
     */
    public void setValidIcon(final Icon validIcon) {
        this.validIcon = validIcon;
        updateDecoration();
    }

    /**
     * Gets the tooltip text for the valid icon.
     *
     * @return Tooltip text for the valid icon.
     */
    public String getValidText() {
        return validText;
    }

    /**
     * Sets the tooltip text for the valid icon.
     *
     * @param validText Tooltip text for the valid icon.
     */
    public void setValidText(final String validText) {
        this.validText = validText;
        updateDecoration();
    }

    /**
     * Gets the icon used for invalid results.
     *
     * @return Icon used for invalid results.
     */
    public Icon getInvalidIcon() {
        return invalidIcon;
    }

    /**
     * Sets the icon to be used for invalid results.
     *
     * @param invalidIcon Icon to be used for invalid results.
     */
    public void setInvalidIcon(final Icon invalidIcon) {
        this.invalidIcon = invalidIcon;
        updateDecoration();
    }

    /**
     * Gets the tooltip text for the invalid icon.
     *
     * @return Tooltip text for the invalid icon.
     */
    public String getInvalidText() {
        return invalidText;
    }

    /**
     * Sets the tooltip text for the invalid icon.
     *
     * @param invalidText Tooltip text for the invalid icon.
     */
    public void setInvalidText(final String invalidText) {
        this.invalidText = invalidText;
        updateDecoration();
    }

    /**
     * @see AbstractIconFeedback#handleResult(Object)
     */
    @Override
    public void handleResult(final Boolean valid) {
        lastResult = valid;

        updateDecoration();

        if ((valid && (validIcon != null)) || (!valid && (invalidIcon != null))) {
            showIcon();
        } else {
            hideIcon();
        }
    }

    /**
     * Updates the icon and tooltip text according to the last result processed by this result handler.
     */
    private void updateDecoration() {
        if (lastResult != null) {
            if (lastResult) {
                setIcon(validIcon);
                setToolTipText(validText);
            } else {
                setIcon(invalidIcon);
                setToolTipText(invalidText);
            }
        }
    }
}
