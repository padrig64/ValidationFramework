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

package com.google.code.validationframework.base.property;

import com.google.code.validationframework.api.property.ListValueChangeListener;
import com.google.code.validationframework.api.property.ReadableListProperty;

import java.util.ArrayList;
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
     *
     * @param startIndex Index of the first added item.
     * @param newItems   Newly added items.
     */
    protected void doNotifyListenersOfAddedValues(int startIndex, List<R> newItems) {
        for (ListValueChangeListener<R> listener : listeners) {
            listener.valuesAdded(this, startIndex, newItems);
        }
    }

    /**
     * Notifies the change listeners that items have been replaced.
     *
     * @param startIndex Index of the first replaced item.
     * @param oldItems   Previous items.
     * @param newItems   New items.
     */
    protected void doNotifyListenersOfChangedValues(int startIndex, List<R> oldItems, List<R> newItems) {
        for (ListValueChangeListener<R> listener : listeners) {
            listener.valuesChanged(this, startIndex, oldItems, newItems);
        }
    }

    /**
     * Notifies the change listeners that items have been removed.
     *
     * @param startIndex Index of the first removed item.
     * @param oldItems   Removed items.
     */
    protected void doNotifyListenersOfRemovedValues(int startIndex, List<R> oldItems) {
        for (ListValueChangeListener<R> listener : listeners) {
            listener.valuesRemoved(this, startIndex, oldItems);
        }
    }
}
