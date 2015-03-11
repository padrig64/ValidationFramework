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

package com.google.code.validationframework.base.property;

import com.google.code.validationframework.api.property.ReadableSetProperty;
import com.google.code.validationframework.api.property.SetValueChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Abstract implementation of a {@link ReadableSetProperty}.
 *
 * @param <R> Type of values that can be read from this set.
 */
public abstract class AbstractReadableSetProperty<R> implements ReadableSetProperty<R> {

    /**
     * Listeners to changes in the list property.
     */
    private final List<SetValueChangeListener<R>> listeners = new ArrayList<SetValueChangeListener<R>>();

    /**
     * Default constructor adding no listener.
     */
    public AbstractReadableSetProperty() {
        // Nothing to be done
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    public AbstractReadableSetProperty(SetValueChangeListener<R>... listeners) {
        super();
        for (SetValueChangeListener<R> listener : listeners) {
            addValueChangeListener(listener);
        }
    }

    /**
     * @see ReadableSetProperty#addValueChangeListener(SetValueChangeListener)
     */
    @Override
    public void addValueChangeListener(SetValueChangeListener<R> listener) {
        listeners.add(listener);
    }

    /**
     * @see ReadableSetProperty#removeValueChangeListener(SetValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(SetValueChangeListener<R> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the change listeners that items have been added.
     *
     * @param newItems Newly added items.
     */
    protected void doNotifyListenersOfAddedValues(Set<R> newItems) {
        Set<R> unmodifiable = Collections.unmodifiableSet(newItems);
        for (SetValueChangeListener<R> listener : listeners) {
            listener.valuesAdded(this, unmodifiable);
        }
    }

    /**
     * Notifies the change listeners that items have been removed.
     *
     * @param oldItems Removed items.
     */
    protected void doNotifyListenersOfRemovedValues(Set<R> oldItems) {
        Set<R> unmodifiable = Collections.unmodifiableSet(oldItems);
        for (SetValueChangeListener<R> listener : listeners) {
            listener.valuesRemoved(this, unmodifiable);
        }
    }
}
