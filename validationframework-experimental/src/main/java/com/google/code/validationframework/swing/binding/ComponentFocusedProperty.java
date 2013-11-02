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

package com.google.code.validationframework.swing.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.base.binding.AbstractReadableProperty;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class ComponentFocusedProperty extends AbstractReadableProperty<Boolean> implements Disposable {

    private class FocusAdapter implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            setValue(true);
        }

        @Override
        public void focusLost(FocusEvent e) {
            setValue(false);
        }
    }

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = -2940271817151485560L;

    private boolean focused = false;

    private final Component component;

    private final FocusListener focusAdapter = new FocusAdapter();

    public ComponentFocusedProperty(Component component) {
        this.component = component;
        this.component.addFocusListener(focusAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        component.removeFocusListener(focusAdapter);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return focused;
    }

    private void setValue(boolean focused) {
        if (!ValueUtils.areEqual(this.focused, focused)) {
            this.focused = focused;
            updateSlaves();
        }
    }
}
