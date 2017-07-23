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

package com.google.code.validationframework.base.property.simple;

import com.google.code.validationframework.api.property.ListValueChangeListener;
import com.google.code.validationframework.base.property.AbstractReadableWritableListProperty;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Readable/writable list property backed by a {@link List}.
 *
 * @param <T> Type of values handled by this list property and the proxied list.
 */
public class SimpleListProperty<T> extends AbstractReadableWritableListProperty<T, T> implements List<T> {

    /**
     * Proxied list.
     */
    private final List<T> internal = new ArrayList<T>();

    /**
     * Read-only version of the proxied list.
     */
    private final List<T> unmodifiable = Collections.unmodifiableList(internal);

    /**
     * Default constructor.
     */
    public SimpleListProperty() {
        super();
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    public SimpleListProperty(ListValueChangeListener<T>... listeners) {
        super(listeners);
    }

    /**
     * Constructor specifying the initial items.
     *
     * @param items Initial items.
     */
    public SimpleListProperty(List<T> items) {
        super();
        internal.addAll(items);
    }

    /**
     * Constructor specifying the initial items and adding the specified listeners.
     * <p>
     * Note that the specified listeners will not be notified for the addition of the specified initial items.
     *
     * @param items     Initial items.
     * @param listeners Listeners to be added.
     */
    public SimpleListProperty(List<T> items, ListValueChangeListener<T>... listeners) {
        super(); // Without listeners

        internal.addAll(items);

        for (ListValueChangeListener<T> listener : listeners) {
            addValueChangeListener(listener);
        }
    }

    /**
     * @see AbstractReadableWritableListProperty#size()
     * @see List#size()
     */
    @Override
    public int size() {
        return internal.size();
    }

    /**
     * @see AbstractReadableWritableListProperty#isEmpty()
     * @see List#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    /**
     * @see AbstractReadableWritableListProperty#get(int)
     * @see List#get(int)
     */
    @Override
    public T get(int index) {
        return internal.get(index);
    }

    /**
     * @see AbstractReadableWritableListProperty#set(int, Object)
     * @see List#set(int, Object)
     */
    @Override
    public T set(int index, T item) {
        T oldItem = internal.set(index, item);

        if (!ValueUtils.areEqual(oldItem, item)) {
            List<T> oldItems = Collections.unmodifiableList(Collections.singletonList(oldItem));
            List<T> newItems = Collections.unmodifiableList(Collections.singletonList(oldItem));
            doNotifyListenersOfChangedValues(index, oldItems, newItems);
        }

        return oldItem;
    }

    /**
     * @see AbstractReadableWritableListProperty#add(Object)
     * @see List#add(Object)
     */
    @Override
    public boolean add(T item) {
        boolean added = internal.add(item);

        if (added) {
            doNotifyListenersOfAddedValues(internal.size() - 1, Collections.unmodifiableList(Collections.singletonList
                    (item)));
        }

        return added;
    }

    /**
     * @see AbstractReadableWritableListProperty#add(int, Object)
     * @see List#add(int, Object)
     */
    @Override
    public void add(int index, T item) {
        internal.add(index, item);
        doNotifyListenersOfAddedValues(index, Collections.singletonList(item));
    }

    /**
     * @see AbstractReadableWritableListProperty#remove(Object)
     * @see List#remove(Object)
     */
    @Override
    public boolean remove(Object item) {
        int index = internal.indexOf(item);
        if (index >= 0) {
            remove(index);
        }
        return index >= 0;
    }

    /**
     * @see AbstractReadableWritableListProperty#remove(int)
     * @see List#remove(int)
     */
    @Override
    public T remove(int index) {
        T oldItem = internal.remove(index);
        doNotifyListenersOfRemovedValues(index, Collections.singletonList(oldItem));
        return oldItem;
    }

    /**
     * @see AbstractReadableWritableListProperty#addAll(Collection)
     * @see List#addAll(Collection)
     */
    @Override
    public boolean addAll(Collection<? extends T> items) {
        int firstIndex = internal.size();
        boolean added = internal.addAll(items);
        doNotifyListenersOfAddedValues(firstIndex, new ArrayList<T>(items));
        return added;
    }

    /**
     * @see AbstractReadableWritableListProperty#addAll(int, Collection)
     * @see List#addAll(int, Collection)
     */
    @Override
    public boolean addAll(int index, Collection<? extends T> items) {
        boolean added = internal.addAll(index, items);
        doNotifyListenersOfAddedValues(index, new ArrayList<T>(items));
        return added;
    }

    /**
     * @see AbstractReadableWritableListProperty#removeAll(Collection)
     * @see List#removeAll(Collection)
     */
    @Override
    public boolean removeAll(Collection<?> items) {
        boolean removed = false;
        for (Object item : items) {
            removed |= remove(item);
        }
        return removed;
    }

    /**
     * @see AbstractReadableWritableListProperty#retainAll(Collection)
     * @see List#retainAll(Collection)
     */
    @Override
    public boolean retainAll(Collection<?> items) {
        Collection<T> toBeRemoved = new ArrayList<T>();

        for (T item : internal) {
            if (!items.contains(item)) {
                toBeRemoved.add(item);
            }
        }

        return removeAll(toBeRemoved);
    }

    /**
     * @see AbstractReadableWritableListProperty#clear()
     * @see List#clear()
     */
    @Override
    public void clear() {
        if (!internal.isEmpty()) {
            List<T> removed = new ArrayList<T>(internal);
            internal.clear();
            doNotifyListenersOfRemovedValues(0, removed);
        }
    }

    /**
     * @see AbstractReadableWritableListProperty#contains(Object)
     * @see List#contains(Object)
     */
    @Override
    public boolean contains(Object item) {
        return internal.contains(item);
    }

    /**
     * @see AbstractReadableWritableListProperty#containsAll(Collection)
     * @see List#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> items) {
        return internal.containsAll(items);
    }

    /**
     * @see List#indexOf(Object)
     */
    @Override
    public int indexOf(Object item) {
        return internal.indexOf(item);
    }

    /**
     * @see List#lastIndexOf(Object)
     */
    @Override
    public int lastIndexOf(Object item) {
        return internal.lastIndexOf(item);
    }

    /**
     * @see List#toArray()
     */
    @Override
    public Object[] toArray() {
        return internal.toArray();
    }

    /**
     * @see List#toArray(Object[])
     */
    @Override
    public <U> U[] toArray(U[] a) {
        return internal.toArray(a);
    }

    /**
     * @see AbstractReadableWritableListProperty#iterator()
     * @see List#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return unmodifiable.iterator();
    }

    /**
     * @see List#listIterator()
     */
    @Override
    public ListIterator<T> listIterator() {
        return unmodifiable.listIterator();
    }

    /**
     * @see List#listIterator(int)
     */
    @Override
    public ListIterator<T> listIterator(int index) {
        return unmodifiable.listIterator(index);
    }

    /**
     * @see List#subList(int, int)
     */
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return unmodifiable.subList(fromIndex, toIndex);
    }

    /**
     * @see AbstractReadableWritableListProperty#asUnmodifiableList()
     */
    @Override
    public List<T> asUnmodifiableList() {
        return unmodifiable;
    }
}
