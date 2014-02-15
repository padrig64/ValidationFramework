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

package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.binding.WritableProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class CompositeWritableProperty<T> implements WritableProperty<T> {


    private final Collection<WritableProperty<T>> properties = new ArrayList<WritableProperty<T>>();

    private T value = null;

    public CompositeWritableProperty() {
        super();
    }

    public CompositeWritableProperty(WritableProperty<T>... properties) {
        super();
        Collections.addAll(this.properties, properties);
        setValue(value);
    }

    public CompositeWritableProperty(Collection<WritableProperty<T>> properties) {
        super();
        this.properties.addAll(properties);
        setValue(value);
    }

    public void addProperty(WritableProperty<T> property) {
        properties.add(property);
        setValue(value);
    }

    public void removeProperty(WritableProperty<T> property) {
        properties.remove(property);
    }

    @Override
    public void setValue(T value) {
        this.value = value;

        for (WritableProperty<T> property : properties) {
            property.setValue(value);
        }
    }
}
