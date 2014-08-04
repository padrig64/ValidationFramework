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

package com.google.code.validationframework.experimental.swing.property.wrap;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.AbstractReadableProperty;

import javax.swing.SwingUtilities;

public class InvokeLaterPropertyWrapper<R> extends AbstractReadableProperty<R> implements Disposable {

    private class ValueChangeAdapter implements ValueChangeListener<R>, Runnable {

        @Override
        public void valueChanged(ReadableProperty<R> property, R oldValue, R newValue) {
            SwingUtilities.invokeLater(this);
        }

        @Override
        public void run() {
            R oldValue = value;
            value = getValue();
            maybeNotifyListeners(oldValue, value);
        }
    }

    private ReadableProperty<R> property = null;

    private final ValueChangeListener<R> valueChangeAdapter = new ValueChangeAdapter();

    private R value = null;

    public InvokeLaterPropertyWrapper(ReadableProperty<R> property) {
        this.property = property;
        this.property.addValueChangeListener(valueChangeAdapter);
        this.value = property.getValue();
    }

    @Override
    public void dispose() {
        if (property != null) {
            property.removeValueChangeListener(valueChangeAdapter);
            property = null;
        }

    }

    @Override
    public R getValue() {
        return property.getValue();
    }
}
