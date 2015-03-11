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

package com.google.code.validationframework.base.property.simple;

import com.google.code.validationframework.api.property.SetValueChangeListener;
import com.google.code.validationframework.base.property.AbstractReadableWritableSetProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Readable/writable set property backed by a {@link Set}.
 *
 * @param <T> Type of values handled by this set property and the proxied set.
 */
public class SimpleSetProperty<T> extends AbstractReadableWritableSetProperty<T, T> implements Set<T> {

    /**
     * Proxied set.
     */
    private final Set<T> internal = new HashSet<T>();

    /**
     * Read-only version of the proxied set.
     */
    private final Set<T> unmodifiable = Collections.unmodifiableSet(internal);

    /**
     * Default constructor.
     */
    public SimpleSetProperty() {
        super();
    }

    /**
     * Constructor adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    public SimpleSetProperty(SetValueChangeListener<T>... listeners) {
        super(listeners);
    }

    /**
     * Constructor specifying the initial items.
     *
     * @param items Initial items.
     */
    public SimpleSetProperty(Set<T> items) {
        super();
        internal.addAll(items);
    }

    /**
     * Constructor specifying the initial items and adding the specified listeners.
     * <p/>
     * Note that the specified listeners will not be notified for the addition of the specified initial items.
     *
     * @param items     Initial items.
     * @param listeners Listeners to be added.
     */
    public SimpleSetProperty(Set<T> items, SetValueChangeListener<T>... listeners) {
        super(); // Without listeners

        internal.addAll(items);

        for (SetValueChangeListener<T> listener : listeners) {
            addValueChangeListener(listener);
        }
    }

    /**
     * @see AbstractReadableWritableSetProperty#size()
     * @see Set#size()
     */
    @Override
    public int size() {
        return internal.size();
    }

    /**
     * @see AbstractReadableWritableSetProperty#isEmpty()
     * @see Set#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return internal.isEmpty();
    }

    /**
     * @see AbstractReadableWritableSetProperty#add(Object)
     * @see Set#add(Object)
     */
    @Override
    public boolean add(T item) {
        boolean modified = internal.add(item);

        if (modified) {
            doNotifyListenersOfAddedValues(Collections.singleton(item));
        }

        return modified;
    }

    /**
     * @see AbstractReadableWritableSetProperty#addAll(Collection)
     * @see Set#addAll(Collection)
     */
    @Override
    public boolean addAll(Collection<? extends T> items) {
        Set<T> added = new HashSet<T>();

        for (T item : items) {
            if (internal.add(item)) {
                added.add(item);
            }
        }

        if (!added.isEmpty()) {
            doNotifyListenersOfAddedValues(added);
        }

        return !added.isEmpty();
    }

    /**
     * @see AbstractReadableWritableSetProperty#remove(Object)
     * @see Set#remove(Object)
     */
    @Override
    public boolean remove(Object item) {
        boolean modified = internal.remove(item);

        if (modified) {
            doNotifyListenersOfRemovedValues(Collections.singleton((T) item));
        }

        return modified;
    }

    /**
     * @see AbstractReadableWritableSetProperty#removeAll(Collection)
     * @see Set#removeAll(Collection)
     */
    @Override
    public boolean removeAll(Collection<?> items) {
        Set<T> removed = new HashSet<T>();

        for (Object item : items) {
            if (internal.remove(item)) {
                removed.add((T) item);
            }
        }

        if (!removed.isEmpty()) {
            doNotifyListenersOfRemovedValues(removed);
        }

        return !removed.isEmpty();
    }

    /**
     * @see AbstractReadableWritableSetProperty#retainAll(Collection)
     * @see Set#retainAll(Collection)
     */
    @Override
    public boolean retainAll(Collection<?> items) {
        Set<T> toBeRemoved = new HashSet<T>();

        for (T item : internal) {
            if (!items.contains(item)) {
                toBeRemoved.add(item);
            }
        }

        if (!toBeRemoved.isEmpty()) {
            internal.removeAll(toBeRemoved);
            doNotifyListenersOfRemovedValues(toBeRemoved);
        }

        return !toBeRemoved.isEmpty();
    }

    /**
     * @see AbstractReadableWritableSetProperty#clear()
     * @see Set#clear()
     */
    @Override
    public void clear() {
        if (!internal.isEmpty()) {
            Set<T> removed = new HashSet<T>(internal);
            internal.clear();
            doNotifyListenersOfRemovedValues(removed);
        }
    }

    /**
     * @see AbstractReadableWritableSetProperty#contains(Object)
     * @see Set#contains(Object)
     */
    @Override
    public boolean contains(Object item) {
        return internal.contains(item);
    }

    /**
     * @see AbstractReadableWritableSetProperty#containsAll(Collection)
     * @see Set#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> items) {
        return internal.containsAll(items);
    }

    /**
     * @see Set#toArray()
     */
    @Override
    public Object[] toArray() {
        return internal.toArray();
    }

    /**
     * @see Set#toArray(Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <U> U[] toArray(U[] a) {
        return internal.toArray(a);
    }

    /**
     * @see AbstractReadableWritableSetProperty#iterator()
     * @see Set#iterator()
     */
    @Override
    public Iterator<T> iterator() {
        return unmodifiable.iterator();
    }

    /**
     * @see AbstractReadableWritableSetProperty#asUnmodifiableSet()
     */
    @Override
    public Set<T> asUnmodifiableSet() {
        return unmodifiable;
    }
}
