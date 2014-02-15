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

import com.google.code.validationframework.api.binding.ChangeListener;
import com.google.code.validationframework.api.binding.ReadableProperty;
import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CompositeReadableProperty<I, O> extends AbstractReadableProperty<O> {

    private class ChangeAdapter implements ChangeListener<I> {

        private final Transformer<Object, O> lastTransformer = new CastTransformer<Object, O>();

        @Override
        public void propertyChanged(ReadableProperty<I> property, I oldValue, I newValue) {
            // Get value from all properties
            List<I> values = new ArrayList<I>();
            for (ReadableProperty<I> master : properties) {
                values.add(master.getValue());
            }

            // Transform value
            Object transformedValue = values;
            for (Transformer transformer : transformers) {
                transformedValue = transformer.transform(transformedValue);
            }
            O slaveInputValue = lastTransformer.transform(transformedValue);

            // Notify slaves
            setValue(slaveInputValue);
        }
    }

    private final Collection<ReadableProperty<I>> properties = new ArrayList<ReadableProperty<I>>();

    private final Collection<Transformer<?, ?>> transformers = new ArrayList<Transformer<?, ?>>();

    private O value = null;

    public CompositeReadableProperty() {
        super();
    }

    public CompositeReadableProperty(ReadableProperty<I>... properties) {
        super();
        Collections.addAll(this.properties, properties);
        // TODO Update composition value + notify
    }

    public CompositeReadableProperty(Collection<ReadableProperty<I>> properties) {
        super();
        this.properties.addAll(properties);
        // TODO Update composition value + notify
    }

    public CompositeReadableProperty(Collection<ReadableProperty<I>> properties, Collection<Transformer<?,
            ?>> transformers) {
        super();
        this.properties.addAll(properties);
        this.transformers.addAll(transformers);
        // TODO Update composition value + notify
    }

    public void addProperty(ReadableProperty<I> property) {
        properties.add(property);
        // TODO Update composition value + notify
    }

    public void removeProperty(ReadableProperty property) {
        properties.remove(property);
        // TODO Update composition value + notify
    }

    @Override
    public O getValue() {
        // TODO
        return value;
    }

    private void setValue(O value) {
        O oldValue = this.value;
        this.value = value;
        notifyListeners(oldValue, value);
    }
}
