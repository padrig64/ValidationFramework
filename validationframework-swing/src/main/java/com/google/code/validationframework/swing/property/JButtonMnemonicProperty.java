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

import javax.swing.JButton;
import java.awt.event.KeyEvent;

/**
 * Readable/writable property representing the mnemonic bean property of a {@link JButton}.
 * <p/>
 * It is possible to control the mnemonic of the button by setting the value of this property or by calling the {@link
 * JButton#setMnemonic(char)} or {@link JButton#setMnemonic(int)} methods of that button.
 * <p/>
 * If the value of this property is set to null, the mnemonic will be set to {@link KeyEvent#VK_UNDEFINED}.
 *
 * @see JButton#getMnemonic()
 * @see JButton#setMnemonic(char)
 * @see JButton#setMnemonic(int)
 */
@Deprecated
public class JButtonMnemonicProperty extends AbstractComponentProperty<JButton, Integer> {

    /**
     * @see AbstractComponentProperty#AbstractComponentProperty(java.awt.Component, String)
     */
    public JButtonMnemonicProperty(JButton button) {
        super(button, "mnemonic");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected Integer getPropertyValueFromComponent() {
        return component.getMnemonic();
    }

    /**
     * @see AbstractComponentProperty#setPropertyValueToComponent(Object)
     */
    @Override
    protected void setPropertyValueToComponent(Integer value) {
        Integer effectiveValue = value;
        if (effectiveValue == null) {
            effectiveValue = KeyEvent.VK_UNDEFINED;
        }
        component.setMnemonic(effectiveValue);
    }
}
