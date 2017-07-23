/*
 * Copyright (c) 2017, ValidationFramework Authors
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

import com.google.code.validationframework.api.property.ListValueChangeListener;
import com.google.code.validationframework.api.property.ReadableListProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of a {@link ReadableListProperty}.
 *
 * @param <R> Type of values that can be read from this list.
 */
public abstract class AbstractReadableListProperty<R> implements ReadableListProperty<R> {

    /**
     * Listeners to changes in the list property.
     */
    private final List<ListValueChangeListener<R>> listeners = new ArrayList<ListValueChangeListener<R>>();

    /**
     * Default constructor adding no listener.
     */
    public AbstractReadableListProperty() {
        // Nothing to be done
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    public AbstractReadableListProperty(ListValueChangeListener<R>... listeners) {
        super();
        for (ListValueChangeListener<R> listener : listeners) {
            addValueChangeListener(listener);
        }
    }

    /**
     * @see ReadableListProperty#addValueChangeListener(ListValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ListValueChangeListener<R> listener) {
        listeners.add(listener);
    }

    /**
     * @see ReadableListProperty#removeValueChangeListener(ListValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ListValueChangeListener<R> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the change listeners that items have been added.
     * <p>
     * Note that the specified list of items will be wrapped in an unmodifiable list before being passed to the
     * listeners.
     *
     * @param startIndex Index of the first added item.
     * @param newItems   Newly added items.
     */
    protected void doNotifyListenersOfAddedValues(int startIndex, List<R> newItems) {
        List<ListValueChangeListener<R>> listenersCopy = new ArrayList<ListValueChangeListener<R>>(listeners);
        List<R> unmodifiable = Collections.unmodifiableList(newItems);
        for (ListValueChangeListener<R> listener : listenersCopy) {
            listener.valuesAdded(this, startIndex, unmodifiable);
        }
    }

    /**
     * Notifies the change listeners that items have been replaced.
     * <p>
     * Note that the specified lists of items will be wrapped in unmodifiable lists before being passed to the
     * listeners.
     *
     * @param startIndex Index of the first replaced item.
     * @param oldItems   Previous items.
     * @param newItems   New items.
     */
    protected void doNotifyListenersOfChangedValues(int startIndex, List<R> oldItems, List<R> newItems) {
        List<ListValueChangeListener<R>> listenersCopy = new ArrayList<ListValueChangeListener<R>>(listeners);
        List<R> oldUnmodifiable = Collections.unmodifiableList(oldItems);
        List<R> newUnmodifiable = Collections.unmodifiableList(newItems);
        for (ListValueChangeListener<R> listener : listenersCopy) {
            listener.valuesChanged(this, startIndex, oldUnmodifiable, newUnmodifiable);
        }
    }

    /**
     * Notifies the change listeners that items have been removed.
     * <p>
     * Note that the specified list of items will be wrapped in an unmodifiable list before being passed to the
     * listeners.
     *
     * @param startIndex Index of the first removed item.
     * @param oldItems   Removed items.
     */
    protected void doNotifyListenersOfRemovedValues(int startIndex, List<R> oldItems) {
        List<ListValueChangeListener<R>> listenersCopy = new ArrayList<ListValueChangeListener<R>>(listeners);
        List<R> unmodifiable = Collections.unmodifiableList(oldItems);
        for (ListValueChangeListener<R> listener : listenersCopy) {
            listener.valuesRemoved(this, startIndex, unmodifiable);
        }
    }
}
