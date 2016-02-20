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

package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.base.property.simple.SimpleProperty;
import com.google.code.validationframework.base.property.wrap.AbstractReadablePropertyWrapper;

public abstract class AbstractBoundProperty<R> extends AbstractReadablePropertyWrapper<R> implements BoundProperty<R> {

    private Disposable bond;

    public AbstractBoundProperty() {
        super(new SimpleProperty<R>());
        bind((ReadableWritableProperty<R, R>) wrappedProperty);
    }

    public AbstractBoundProperty(boolean deepDispose) {
        super(new SimpleProperty<R>(), deepDispose);
        bind((ReadableWritableProperty<R, R>) wrappedProperty);
    }

    @Override
    protected void wrappedPropertyValueChanged(ReadableProperty<R> property, R oldValue, R newValue) {
        maybeNotifyListeners(oldValue, newValue);
    }

    @Override
    public R getValue() {
        return wrappedProperty.getValue();
    }

    @Override
    public void dispose() {
        // Unbind
        if (bond != null) {
            bond.dispose();
            bond = null;
        }
        super.dispose();
    }

    protected abstract Disposable bind(WritableProperty<R> targetProperty);
}