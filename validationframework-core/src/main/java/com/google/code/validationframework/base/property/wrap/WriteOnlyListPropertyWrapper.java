/*
 * Copyright (c) 2016, ValidationFramework Authors
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

package com.google.code.validationframework.base.property.wrap;

import com.google.code.validationframework.api.common.DeepDisposable;
import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.WritableListProperty;

import java.util.Collection;

/**
 * Wrapper for list properties (typically both readable/writable) to make them appear as write-only.
 * <p/>
 * This can be useful, for example, to return a write-only list property in a getter method that is actually a
 * readable/writable list property internally. The wrapper then forbids the programmer to cast the returned list
 * property to a readable list property in order to change read its values.
 *
 * @param <W> Type of data that can be written to the wrapped set property.
 */
public class WriteOnlyListPropertyWrapper<W> implements WritableListProperty<W>, DeepDisposable {

    /**
     * Wrapped list property.
     */
    private WritableListProperty<W> wrappedListProperty;

    /**
     * True to dispose the wrapped property upon {@link #dispose()}, false otherwise.
     */
    private boolean deepDispose = true;

    /**
     * Constructor specifying the list property to be wrapped, typically a list property that is both readable and
     * writable.
     *
     * @param wrappedListProperty List property to be wrapped.
     */
    public WriteOnlyListPropertyWrapper(WritableListProperty<W> wrappedListProperty) {
        this.wrappedListProperty = wrappedListProperty;
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
        if (deepDispose && (wrappedListProperty instanceof Disposable)) {
            ((Disposable) wrappedListProperty).dispose();
        }
        wrappedListProperty = null;
    }

    /**
     * @see WritableListProperty#set(int, Object)
     */
    @Override
    public W set(int index, W item) {
        W oldItem = null;

        if (wrappedListProperty != null) {
            oldItem = wrappedListProperty.set(index, item);
        }

        return oldItem;
    }

    /**
     * @see WritableListProperty#add(Object)
     */
    @Override
    public boolean add(W item) {
        if (wrappedListProperty != null) {
            wrappedListProperty.add(item);
        }

        // See List#add(Object) and Collection#add(Object)
        return true;
    }

    /**
     * @see WritableListProperty#add(int, Object)
     */
    @Override
    public void add(int index, W item) {
        if (wrappedListProperty != null) {
            wrappedListProperty.add(index, item);
        }
    }

    /**
     * @see WritableListProperty#addAll(Collection)
     */
    @Override
    public boolean addAll(Collection<? extends W> items) {
        boolean changed = false;

        if (wrappedListProperty != null) {
            changed = wrappedListProperty.addAll(items);
        }

        return changed;
    }

    /**
     * @see WritableListProperty#addAll(int, Collection)
     */
    @Override
    public boolean addAll(int index, Collection<? extends W> items) {
        boolean changed = false;

        if (wrappedListProperty != null) {
            changed = wrappedListProperty.addAll(index, items);
        }

        return changed;
    }

    /**
     * @see WritableListProperty#remove(Object)
     */
    @Override
    public boolean remove(Object item) {
        boolean changed = false;

        if (wrappedListProperty != null) {
            changed = wrappedListProperty.remove(item);
        }

        return changed;
    }

    /**
     * @see WritableListProperty#remove(int)
     */
    @Override
    public W remove(int index) {
        W oldItem = null;

        if (wrappedListProperty != null) {
            oldItem = wrappedListProperty.remove(index);
        }

        return oldItem;
    }

    /**
     * @see WritableListProperty#removeAll(Collection)
     */
    @Override
    public boolean removeAll(Collection<?> items) {
        boolean changed = false;

        if (wrappedListProperty != null) {
            changed = wrappedListProperty.removeAll(items);
        }

        return changed;
    }

    /**
     * @see WritableListProperty#retainAll(Collection)
     */
    @Override
    public boolean retainAll(Collection<?> items) {
        boolean changed = false;

        if (wrappedListProperty != null) {
            changed = wrappedListProperty.retainAll(items);
        }

        return changed;
    }

    /**
     * @see WritableListProperty#clear()
     */
    @Override
    public void clear() {
        if (wrappedListProperty != null) {
            wrappedListProperty.clear();
        }
    }
}
