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

package com.google.code.validationframework.base.property.wrap;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ListValueChangeListener;
import com.google.code.validationframework.api.property.ReadableListProperty;
import com.google.code.validationframework.base.property.AbstractReadableListProperty;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Wrapper for list properties (typically both readable/writable) to make them appear as read-only.
 * <p/>
 * This can be useful, for example, to return a read-only list property in a getter method that is actually a
 * readable/writable list property internally. The wrapper then forbids the programmer to cast the returned list
 * property to a writable list property in order to change its contents.
 *
 * @param <R> Type of data that can be read from the wrapped list property.
 */
public class ReadOnlyListPropertyWrapper<R> extends AbstractReadableListProperty<R> implements Disposable {

    /**
     * Entity responsible for forwarding the change events from the wrapped list property to the listeners of the
     * read-only wrapper.
     */
    private class ListValueChangeForwarder implements ListValueChangeListener<R> {

        /**
         * @see ListValueChangeListener#valuesAdded(ReadableListProperty, int, List)
         */
        @Override
        public void valuesAdded(ReadableListProperty<R> listProperty, int startIndex, List<R> newValues) {
            doNotifyListenersOfAddedValues(startIndex, newValues);
        }

        /**
         * @see ListValueChangeListener#valuesChanged(ReadableListProperty, int, List, List)
         */
        @Override
        public void valuesChanged(ReadableListProperty<R> listProperty, int startIndex, List<R> oldValues, List<R>
                newValues) {
            doNotifyListenersOfChangedValues(startIndex, oldValues, newValues);
        }

        /**
         * @see ListValueChangeListener#valuesRemoved(ReadableListProperty, int, List)
         */
        @Override
        public void valuesRemoved(ReadableListProperty<R> listProperty, int startIndex, List<R> oldValues) {
            doNotifyListenersOfRemovedValues(startIndex, oldValues);
        }
    }

    /**
     * Wrapped list property.
     */
    private final ReadableListProperty<R> wrappedListProperty;

    /**
     * Listener to changes on the wrapped property.
     */
    private final ListValueChangeListener<R> changeAdapter = new ListValueChangeForwarder();

    /**
     * Constructor specifying the list property to be wrapped, typically a list property that is both readable and
     * writable.
     *
     * @param wrappedListProperty List property to be wrapped.
     */
    public ReadOnlyListPropertyWrapper(ReadableListProperty<R> wrappedListProperty) {
        this.wrappedListProperty = wrappedListProperty;
        this.wrappedListProperty.addValueChangeListener(changeAdapter);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        wrappedListProperty.removeValueChangeListener(changeAdapter);
    }

    /**
     * @see ReadableListProperty#size()
     */
    @Override
    public int size() {
        return wrappedListProperty.size();
    }

    /**
     * @see ReadableListProperty#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return wrappedListProperty.isEmpty();
    }

    /**
     * @see ReadableListProperty#get(int)
     */
    @Override
    public R get(int index) {
        return wrappedListProperty.get(index);
    }

    /**
     * @see ReadableListProperty#contains(Object)
     */
    @Override
    public boolean contains(Object item) {
        return wrappedListProperty.contains(item);
    }

    /**
     * @see ReadableListProperty#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> items) {
        return wrappedListProperty.containsAll(items);
    }

    /**
     * @see ReadableListProperty#asUnmodifiableList()
     */
    @Override
    public List<R> asUnmodifiableList() {
        return wrappedListProperty.asUnmodifiableList();
    }

    /**
     * @see ReadableListProperty#iterator()
     */
    @Override
    public Iterator<R> iterator() {
        return asUnmodifiableList().iterator();
    }
}
