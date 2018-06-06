/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.swing.property;

import javax.swing.JDialog;

/**
 * Readable/writable property representing the title bean property of a {@link JDialog}.
 * <p/>
 * It is possible to control the title of the dialog by setting the value of this property or by calling the {@link
 * JDialog#setTitle(String)} method of that dialog.
 * <p/>
 * Note that in conformance with the behavior of the {@link JDialog}, unlike the {@link javax.swing.JFrame}, a null
 * value will not be replaced by an empty string.
 *
 * @see JDialog#getTitle()
 * @see JDialog#setTitle(String)
 */
@Deprecated
public class JDialogTitleProperty extends AbstractComponentProperty<JDialog, String> {

    /**
     * @see AbstractComponentProperty#AbstractComponentProperty(java.awt.Component, String)
     */
    public JDialogTitleProperty(JDialog dialog) {
        super(dialog, "title");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected String getPropertyValueFromComponent() {
        return component.getTitle();
    }

    /**
     * @see AbstractComponentProperty#setPropertyValueToComponent(Object)
     */
    @Override
    protected void setPropertyValueToComponent(String value) {
        component.setTitle(value);
    }
}
