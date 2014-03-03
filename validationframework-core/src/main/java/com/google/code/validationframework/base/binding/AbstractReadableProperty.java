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
import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract implementation of a {@link ReadableProperty}.
 * <p/>
 * Sub-classes should call the {@link #notifyListeners(Object, Object)} or {@link #notifyListeners(Object, Object,
 * boolean)} methods whenever the property value changes.
 * <p/>
 * Note that this class is not thread-safe.
 *
 * @param <R> Type of data that can be read from this property.
 */
public abstract class AbstractReadableProperty<R> implements ReadableProperty<R> {

    /**
     * Writable properties to be updated.
     */
    private final List<ChangeListener<R>> listeners = new ArrayList<ChangeListener<R>>();

    /**
     * @see ReadableProperty#addChangeListener(ChangeListener)
     */
    @Override
    public void addChangeListener(ChangeListener<R> listener) {
        listeners.add(listener);
    }

    /**
     * @see ReadableProperty#removeChangeListener(ChangeListener)
     */
    @Override
    public void removeChangeListener(ChangeListener<R> listener) {
        listeners.remove(listener);
    }

    /**
     * Notifies the listeners that the property value has changed, if it has changed.
     * <p/>
     * Note that the listeners will be notified only if the old value is different than the new value.
     *
     * @param oldValue Previous value.
     * @param newValue New value.
     *
     * @see #notifyListeners(Object, Object, boolean)
     * @see #getValue()
     */
    protected void notifyListeners(R oldValue, R newValue) {
        notifyListeners(oldValue, newValue, false);
    }

    /**
     * Notifies the listeners that the property value has changed.
     *
     * @param oldValue       Previous value.
     * @param newValue       New value.
     * @param evenIfNoChange True to notify the listeners even if the new value and the old value are equal, false to
     *                       notify the listeners only if they are not equal.
     *
     * @see #notifyListeners(Object, Object)
     * @see #getValue()
     */
    protected void notifyListeners(R oldValue, R newValue, boolean evenIfNoChange) {
        if (evenIfNoChange || !ValueUtils.areEqual(oldValue, newValue)) {
            for (ChangeListener<R> listener : listeners) {
                listener.valueChanged(this, oldValue, newValue);
            }
        }
    }
}
