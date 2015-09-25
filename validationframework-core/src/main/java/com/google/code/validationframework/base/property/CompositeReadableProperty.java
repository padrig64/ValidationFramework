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

import com.google.code.validationframework.api.common.DeepDisposable;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * {@link ReadableProperty} gathering several other readable properties of the same type.
 * <p/>
 * Whenever a sub-property changes, this composite property will trigger its change listeners. Also, reading the value
 * from this property will return the collection of values from all sub-properties.
 * <p/>
 * The value returned by {@link #getValue()} will never be null, but it may very well be an empty collection.
 *
 * @param <R> Type of data that can be read from the sub-properties.
 */
public class CompositeReadableProperty<R> extends AbstractReadableProperty<Collection<R>> implements DeepDisposable {

    /**
     * Sub-properties.
     */
    private final Collection<ReadableProperty<R>> properties = new ArrayList<ReadableProperty<R>>();

    /**
     * Listener to changes in the sub-properties.
     */
    private final ValueChangeListener<R> changeAdapter = new ValueChangeAdapter();

    /**
     * True to dispose all sub-properties upon {@link #dispose()}, false otherwise.
     */
    private boolean deepDispose;

    /**
     * Collection of current values of the sub-properties.
     */
    private Collection<R> values = Collections.emptyList();

    /**
     * Constructor.
     * <p/>
     * The default value of this property will be an empty list. It will not be null. Also, the sub-properties will be
     * disposed whenever this property is disposed.
     */
    public CompositeReadableProperty() {
        this(true);
    }

    /**
     * Constructor.
     * <p/>
     * The default value of this property will be an empty list. It will not be null.
     *
     * @param deepDispose True to dispose the sub-properties whenever this property is disposed, false otherwise.
     */
    public CompositeReadableProperty(boolean deepDispose) {
        super();
        this.deepDispose = deepDispose;
    }

    /**
     * Constructor specifying the sub-properties to be added.
     * <p/>
     * The sub-properties will be disposed whenever this property is disposed.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeReadableProperty(Collection<ReadableProperty<R>> properties) {
        this(properties, true);
    }

    /**
     * Constructor specifying the sub-properties to be added.
     *
     * @param properties  Sub-properties to be added.
     * @param deepDispose True to dispose the sub-properties whenever this property is disposed, false otherwise.
     */
    public CompositeReadableProperty(Collection<ReadableProperty<R>> properties, boolean deepDispose) {
        super();
        this.deepDispose = deepDispose;
        for (ReadableProperty<R> property : properties) {
            addProperty(property);
        }
    }

    /**
     * Constructor specifying the sub-properties to be added.
     * <p/>
     * The sub-properties will be disposed whenever this property is disposed.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeReadableProperty(ReadableProperty<R>... properties) {
        super();
        this.deepDispose = true;
        for (ReadableProperty<R> property : properties) {
            addProperty(property);
        }
    }

    /**
     * @see DeepDisposable#getDeepDispose()
     */
    @Override
    public boolean getDeepDispose() {
        return deepDispose;
    }

    /**
     * @see DeepDisposable#setDeepDispose(boolean)
     */
    @Override
    public void setDeepDispose(boolean deepDispose) {
        this.deepDispose = deepDispose;
    }

    /**
     * @see AbstractReadableProperty#dispose()
     * @see DeepDisposable#dispose()
     */
    @Override
    public void dispose() {
        super.dispose();
        if (deepDispose) {
            for (ReadableProperty<R> property : properties) {
                if (property instanceof Disposable) {
                    ((Disposable) property).dispose();
                }
            }
            properties.clear();
        }
    }

    /**
     * Gets all sub-properties.
     *
     * @return Collection containing all sub-properties.
     */
    public Collection<ReadableProperty<R>> getProperties() {
        return new ArrayList<ReadableProperty<R>>(properties);
    }

    /**
     * Adds the specified sub-property.
     * <p/>
     * This will trigger the change listeners.
     *
     * @param property Sub-property to be added.
     */
    public void addProperty(ReadableProperty<R> property) {
        property.addValueChangeListener(changeAdapter);
        properties.add(property);
        updateFromProperties();
    }

    /**
     * Removes the specified sub-properties.
     * <p/>
     * This will trigger the change listeners.
     *
     * @param property Sub-property to be removed.
     * @see #clear()
     */
    public void removeProperty(ReadableProperty<R> property) {
        property.removeValueChangeListener(changeAdapter);
        properties.remove(property);
        updateFromProperties();
    }

    /**
     * Removes all sub-properties.
     *
     * @see #removeProperty(ReadableProperty)
     */
    public void clear() {
        properties.clear();
        updateFromProperties();
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Collection<R> getValue() {
        return values;
    }

    /**
     * Sets the new collection of values to be returned by {@link #getValue()}.
     *
     * @param values New collection of values.
     */
    private void setValue(Collection<R> values) {
        Collection<R> oldValues = this.values;
        this.values = values;
        maybeNotifyListeners(oldValues, values);
    }

    /**
     * Updates the current collection of values from the sub-properties and notifies the listeners.
     */
    private void updateFromProperties() {
        // Get value from all properties: use a new collection so that equals() returns false
        List<R> newValues = new ArrayList<R>();
        for (ReadableProperty<R> master : properties) {
            newValues.add(master.getValue());
        }

        // Notify slaves
        setValue(newValues);
    }

    /**
     * Listener to changes in the sub-properties.
     */
    private class ValueChangeAdapter implements ValueChangeListener<R> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<R> property, R oldValue, R newValue) {
            if (!ValueUtils.areEqual(oldValue, newValue)) {
                updateFromProperties();
            }
        }
    }
}
