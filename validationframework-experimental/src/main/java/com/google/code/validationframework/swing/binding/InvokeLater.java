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

package com.google.code.validationframework.swing.binding;

import com.google.code.validationframework.api.binding.ValueChangeListener;
import com.google.code.validationframework.base.binding.AbstractReadableProperty;
import com.google.code.validationframework.api.binding.WritableProperty;

import javax.swing.SwingUtilities;

public class InvokeLater<T> extends AbstractReadableProperty<T> implements WritableProperty<T> {

    private class Postponer implements Runnable {

        private final T oldValue;

        public Postponer(T oldValue) {
            this.oldValue = oldValue;
        }

        @Override
        public void run() {
            notifyListeners(oldValue, getValue());
        }
    }

    private T value = null;

    public InvokeLater() {
        // Nothing to be done
    }

    public InvokeLater(T initialValue) {
        value = initialValue;
    }

    public InvokeLater(ValueChangeListener<T>... listeners) {
        if (listeners != null) {
            for (ValueChangeListener<T> listener : listeners) {
                addValueChangeListener(listener);
            }
        }
    }

    public InvokeLater(T initialValue, ValueChangeListener<T>... listeners) {
        value = initialValue;

        if (listeners != null) {
            for (ValueChangeListener<T> listener : listeners) {
                addValueChangeListener(listener);
            }
        }
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        T oldValue = this.value;
        this.value = value;
        SwingUtilities.invokeLater(new Postponer(oldValue));
    }
}
