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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link ReadableProperty} gathering several other readable properties of the same type.
 * <p/>
 * Whenever a sub-property changes, this composite property will trigger its listeners. Also, reading the value from
 * this property will return the collection of values from all sub-properties.
 *
 * @param <R> Type of data that can be read from the sub-properties.
 */
public class CompositeReadableProperty<R> extends AbstractReadableProperty<Collection<R>> {

    private class ChangeAdapter implements ChangeListener<R> {

        @Override
        public void propertyChanged(ReadableProperty<R> property, R oldValue, R newValue) {
            updateFromProperties();
        }
    }

    private final Collection<ReadableProperty<R>> properties = new ArrayList<ReadableProperty<R>>();

    private Collection<R> values = null;

    public CompositeReadableProperty() {
        super();
    }

    public CompositeReadableProperty(Collection<ReadableProperty<R>> properties) {
        super();
        this.properties.addAll(properties);
        updateFromProperties();
    }

    public CompositeReadableProperty(ReadableProperty<R>... properties) {
        super();
        Collections.addAll(this.properties, properties);
        updateFromProperties();
    }

    public void addProperty(ReadableProperty<R> property) {
        properties.add(property);
        updateFromProperties();
    }

    public void removeProperty(ReadableProperty<R> property) {
        properties.remove(property);
        updateFromProperties();
    }

    @Override
    public Collection<R> getValue() {
        return values;
    }

    private void updateFromProperties() {
        // Get value from all properties
        // TODO Update only the one that was modified instead of re-getting all the values
        List<R> values = new ArrayList<R>();
        for (ReadableProperty<R> master : properties) {
            values.add(master.getValue());
        }

        // Notify slaves
        setValue(values);
    }

    private void setValue(Collection<R> values) {
        Collection<R> oldValues = this.values;
        this.values = values;
        notifyListeners(oldValues, values);
    }
}
