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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Abstract implementation of a {@link ReadableProperty}.
 * <p/>
 * Sub-classes should call the {@link #maybeNotifyListeners(Object, Object)} method whenever the property value changes.
 * Sub-classes that are also writable should prevent recursion by checking the result of {@link #isNotifyingListeners()}
 * when setting the new value.
 * <p/>
 * This abstract implementation allows to inhibit the firing of value change events. When the property is inhibited,
 * changing its value will not fire any value change event. When the property is un-inhibited again, one single value
 * change event will be fired only if the last property value is different than the property value of the last change
 * event fired.
 * <p/>
 * By default, the property is not inhibited.
 * <p/>
 * Note that this class is not thread-safe.
 *
 * @param <R> Type of data that can be read from this property.
 */
public abstract class AbstractReadableProperty<R> implements ReadableProperty<R>, Disposable {

    /**
     * Writable properties to be updated.
     */
    private final List<ValueChangeListener<? super R>> listeners = new ArrayList<ValueChangeListener<? super R>>();

    /**
     * Flag stating whether the inhibit the firing of value change events.
     */
    private boolean inhibited = false;

    /**
     * Number of inhibited value change events since the property was inhibited.
     */
    private int inhibitCount = 0;

    /**
     * Property value when the last (non-inhibited) value change event was fired.
     * <p/>
     * It should be updated only if {@link #inhibited} is false. It should be read read only when un-inhibiting and
     * {@link #inhibitCount} is 0.
     */
    private R lastNonInhibitedValue = null;

    /**
     * Property value when the last value change event was inhibited (not fired).
     * <p/>
     * It should be updated at least when {@link #inhibited} is true. It should be read only when un-inhibiting and
     * {@link #inhibitCount} is 0.
     */
    private R lastInhibitedValue = null;

    /**
     * Flag indicating whether the property is currently notifying its value change listeners.
     * <p/>
     * This can be used, for instance, to avoid recursion (in case of bi-directional binding).
     */
    private boolean notifyingListeners = false;

    /**
     * Disposes this readable property by removing any references to any listener.
     * <p/>
     * Sub-classes should call the dispose() method of their parent class.
     * <p/>
     * Note that the listeners will not be disposed.
     *
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        listeners.clear();
    }

    /**
     * Gets the registered value change listeners.
     * <p/>
     * Note that the returned collection is not modifiable.
     *
     * @return Value change listeners.
     */
    public Collection<ValueChangeListener<? super R>> getValueChangeListeners() {
        return Collections.unmodifiableList(listeners);
    }

    /**
     * @see ReadableProperty#addValueChangeListener(ValueChangeListener)
     */
    @Override
    public void addValueChangeListener(ValueChangeListener<? super R> listener) {
        listeners.add(listener);
    }

    /**
     * @see ReadableProperty#removeValueChangeListener(ValueChangeListener)
     */
    @Override
    public void removeValueChangeListener(ValueChangeListener<? super R> listener) {
        listeners.remove(listener);
    }

    /**
     * States whether this property is inhibited.
     *
     * @return True if this property is inhibited, false otherwise.
     */
    public boolean isInhibited() {
        return inhibited;
    }

    /**
     * States whether this property should be inhibited.
     *
     * @param inhibited True if this property should be inhibited, false otherwise.
     */
    public void setInhibited(boolean inhibited) {
        boolean wasInhibited = this.inhibited;
        this.inhibited = inhibited;

        if (wasInhibited && !inhibited) {
            if (inhibitCount > 0) {
                maybeNotifyListeners(lastNonInhibitedValue, lastInhibitedValue);
            }
            inhibitCount = 0;
        }
    }

    /**
     * Notifies the listeners that the property value has changed, if all the conditions are fulfilled (typically, if
     * the old and new values are different, and if the property is not inhibited).
     * <p/>
     * Sub-classes should typically call this method when they are ready to fire value change events.
     * <p/>
     * However, sub-classes may override this method to perform additional checks.
     *
     * @param oldValue Previous value.
     * @param newValue New value.
     * @see #notifyListenersIfUninhibited(Object, Object)
     * @see #doNotifyListeners(Object, Object)
     */
    protected void maybeNotifyListeners(R oldValue, R newValue) {
        if (!ValueUtils.areEqual(oldValue, newValue)) {
            notifyListenersIfUninhibited(oldValue, newValue);
        }
    }

    /**
     * Notifies the listeners that the property value has changed, if the property is not inhibited.
     *
     * @param oldValue Previous value.
     * @param newValue New value.
     * @see #maybeNotifyListeners(Object, Object)
     * @see #doNotifyListeners(Object, Object)
     */
    private void notifyListenersIfUninhibited(R oldValue, R newValue) {
        if (inhibited) {
            inhibitCount++;
            lastInhibitedValue = newValue;
        } else {
            lastInhibitedValue = newValue; // Just in case, even though not really necessary
            lastNonInhibitedValue = newValue;
            doNotifyListeners(oldValue, newValue);
        }
    }

    /**
     * States whether the property is currently notifying its value change listeners.
     *
     * @return True if the property is notifying its listeners, false otherwise.
     */
    protected boolean isNotifyingListeners() {
        return notifyingListeners;
    }

    /**
     * Notifies the listeners that the property value has changed, unconditionally.
     *
     * @param oldValue Previous value.
     * @param newValue New value.
     */
    private void doNotifyListeners(R oldValue, R newValue) {
        List<ValueChangeListener<? super R>> listenersCopy = new ArrayList<ValueChangeListener<? super R>>(listeners);
        notifyingListeners = true;
        for (ValueChangeListener<? super R> listener : listenersCopy) {
            listener.valueChanged(this, oldValue, newValue);
        }
        notifyingListeners = false;
    }
}
