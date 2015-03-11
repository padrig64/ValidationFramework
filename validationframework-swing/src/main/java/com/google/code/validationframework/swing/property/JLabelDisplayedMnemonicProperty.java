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

import javax.swing.JLabel;
import java.awt.event.KeyEvent;

/**
 * Readable/writable property representing the displayed mnemonic bean property of a {@link JLabel}.
 * <p/>
 * It is possible to control the displayed mnemonic of the label by setting the value of this property or by calling the
 * {@link JLabel#setDisplayedMnemonic(char)} or {@link JLabel#setDisplayedMnemonic(int)} methods of that label.
 * <p/>
 * If the value of this property is set to null, the displayed mnemonic will be set to {@link KeyEvent#VK_UNDEFINED}.
 *
 * @see JLabel#getDisplayedMnemonic()
 * @see JLabel#setDisplayedMnemonic(char)
 * @see JLabel#setDisplayedMnemonic(int)
 */
public class JLabelDisplayedMnemonicProperty extends AbstractComponentProperty<JLabel, Integer> {

    /**
     * @see AbstractComponentProperty#AbstractComponentProperty(java.awt.Component, String)
     */
    public JLabelDisplayedMnemonicProperty(JLabel label) {
        super(label, "displayedMnemonic");
    }

    /**
     * @see AbstractComponentProperty#getPropertyValueFromComponent()
     */
    @Override
    protected Integer getPropertyValueFromComponent() {
        return component.getDisplayedMnemonic();
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
        component.setDisplayedMnemonic(effectiveValue);
    }
}
