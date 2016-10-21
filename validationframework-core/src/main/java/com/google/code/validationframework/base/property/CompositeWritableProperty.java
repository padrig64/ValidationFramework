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
import com.google.code.validationframework.api.property.WritableProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * {@link WritableProperty} gathering several other writable properties of the same type.
 * <p/>
 * Whenever the value of this composite property is set, it will set the same value on all the sub-properties.
 *
 * @param <W> Type of data that can be set on the sub-properties.
 */
public class CompositeWritableProperty<W> implements WritableProperty<W>, DeepDisposable {

    /**
     * Sub-properties.
     */
    private final Collection<WritableProperty<? super W>> properties = new ArrayList<WritableProperty<? super W>>();

    /**
     * Last value set.
     */
    private W value = null;

    /**
     * True to dispose all sub-properties upon {@link #dispose()}, false otherwise.
     */
    private boolean deepDispose;

    /**
     * Constructor setting the initial value to null.
     */
    public CompositeWritableProperty() {
        this(true);
    }

    /**
     * Constructor.
     * <p/>
     * The default value of this property will be an empty list. It will not be null.
     *
     * @param deepDispose True to dispose the sub-properties whenever this property is disposed, false otherwise.
     */
    public CompositeWritableProperty(boolean deepDispose) {
        super();
        this.deepDispose = deepDispose;
    }

    /**
     * Constructor specifying the initial value.
     *
     * @param value Initial value.
     */
    public CompositeWritableProperty(W value) {
        this(value, true);
    }

    /**
     * Constructor specifying the initial value.
     *
     * @param value       Initial value.
     * @param deepDispose True to dispose the sub-properties whenever this property is disposed, false otherwise.
     */
    public CompositeWritableProperty(W value, boolean deepDispose) {
        super();
        this.deepDispose = deepDispose;
        setValue(value);
    }

    /**
     * Constructor specifying the sub-properties to be added, and setting the initial value to null.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeWritableProperty(Collection<WritableProperty<? super W>> properties) {
        this(properties, true);
    }

    /**
     * Constructor specifying the sub-properties to be added, and setting the initial value to null.
     *
     * @param properties  Sub-properties to be added.
     * @param deepDispose True to dispose the sub-properties whenever this property is disposed, false otherwise.
     */
    public CompositeWritableProperty(Collection<WritableProperty<? super W>> properties, boolean deepDispose) {
        super();
        this.deepDispose = deepDispose;
        this.properties.addAll(properties);
        setValue(value);
    }

    /**
     * Constructor specifying the initial value and the sub-properties to be added.
     *
     * @param value      Initial value.
     * @param properties Sub-properties to be added.
     */
    public CompositeWritableProperty(W value, Collection<WritableProperty<W>> properties) {
        this(value, properties, true);
    }

    /**
     * Constructor specifying the initial value and the sub-properties to be added.
     *
     * @param value       Initial value.
     * @param properties  Sub-properties to be added.
     * @param deepDispose True to dispose the sub-properties whenever this property is disposed, false otherwise.
     */
    public CompositeWritableProperty(W value, Collection<WritableProperty<W>> properties, boolean deepDispose) {
        super();
        this.deepDispose = deepDispose;
        this.properties.addAll(properties);
        setValue(value);
    }

    /**
     * Constructor specifying the sub-properties to be added.
     *
     * @param properties Sub-properties to be added.
     */
    public CompositeWritableProperty(WritableProperty<? super W>... properties) {
        super();
        deepDispose = true;
        Collections.addAll(this.properties, properties);
        setValue(value);
    }

    /**
     * Constructor specifying the initial value and the sub-properties to be added.
     *
     * @param value      Initial value.
     * @param properties Sub-properties to be added.
     */
    public CompositeWritableProperty(W value, WritableProperty<? super W>... properties) {
        super();
        deepDispose = true;
        Collections.addAll(this.properties, properties);
        setValue(value);
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
     * @see DeepDisposable#dispose()
     */
    @Override
    public void dispose() {
        if (deepDispose) {
            for (WritableProperty<? super W> property : properties) {
                if (property instanceof Disposable) {
                    ((Disposable) property).dispose();
                }
            }
        }
        properties.clear();
    }

    /**
     * Gets all sub-properties.
     *
     * @return Collection containing all sub-properties.
     */
    public Collection<WritableProperty<? super W>> getProperties() {
        return new ArrayList<WritableProperty<? super W>>(properties);
    }

    /**
     * Adds the specified sub-property.
     *
     * @param property Sub-property to be added.
     */
    public void addProperty(WritableProperty<? super W> property) {
        properties.add(property);
        setValue(value);
    }

    /**
     * Removes the specified sub-property.
     *
     * @param property Sub-property to be removed.
     */
    public void removeProperty(WritableProperty<? super W> property) {
        properties.remove(property);
    }

    /**
     * Removes all sub-properties.
     *
     * @see #removeProperty(WritableProperty)
     */
    public void clear() {
        properties.clear();
    }

    /**
     * @see WritableProperty#setValue(Object)
     */
    @Override
    public void setValue(W value) {
        this.value = value;

        for (WritableProperty<? super W> property : properties) {
            property.setValue(value);
        }
    }
}
