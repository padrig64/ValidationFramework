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

package com.google.code.validationframework.swing.property;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

/**
 * Readable/writable property representing the resizable state of a {@link Frame} or a {@link Dialog}.
 * <p/>
 * It is possible to control the resizable state of the component by setting the value of this property or by calling
 * the {@link Frame#setResizable(boolean)} or {@link Dialog#setResizable(boolean)} method of that window.
 * <p/>
 * Please note that a plain {@link Window} does not have this bean property. A {@link Frame}, {@link Dialog} or
 * sub-class does. However, the type {@link Window} is still used by this property for convenience.
 * <p/>
 * Finally note that null values are not supported by this property.
 *
 * @see Frame#isResizable()
 * @see Frame#setResizable(boolean)
 * @see Dialog#isResizable()
 * @see Dialog#setResizable(boolean)
 */
public class WindowResizableProperty extends AbstractComponentProperty<Window, Boolean> {

    /**
     * @see AbstractComponentProperty#AbstractComponentProperty(java.awt.Component, String)
     */
    public WindowResizableProperty(Window window) {
        super(window, "resizable");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected Boolean getPropertyValueFromComponent() {
        Boolean value = null;

        if (component instanceof Frame) {
            value = ((Frame) component).isResizable();
        } else if (component instanceof Dialog) {
            value = ((Dialog) component).isResizable();
        }

        return value;
    }

    /**
     * @see AbstractComponentProperty#setPropertyValueToComponent(Object)
     */
    @Override
    protected void setPropertyValueToComponent(Boolean value) {
        if (component instanceof Frame) {
            ((Frame) component).setResizable(value);
        } else if (component instanceof Dialog) {
            ((Dialog) component).setResizable(value);
        }
    }
}
