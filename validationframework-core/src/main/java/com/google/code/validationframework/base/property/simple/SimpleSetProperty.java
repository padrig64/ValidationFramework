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
    private final Set<T> set;

    /**
     * Read-only version of the proxied set.
     */
    private final Set<T> readOnlySet;

    /**
     * Default constructor using an {@link HashSet}.
     */
    public SimpleSetProperty() {
        this(new HashSet<T>());
    }

    /**
     * Constructor using a {@link HashSet} and adding the specified listeners.
     *
     * @param listeners Listeners to be added.
     */
    public SimpleSetProperty(SetValueChangeListener<T>... listeners) {
        this(new HashSet<T>(), listeners);
    }

    /**
     * Constructor specifying the set to be proxied.
     *
     * @param set Set to be proxied.
     */
    public SimpleSetProperty(Set<T> set) {
        this.set = set;
        this.readOnlySet = Collections.unmodifiableSet(set);
    }

    /**
     * Constructor specifying the set to be proxied and adding the specified listeners.
     *
     * @param set       Proxied set.
     * @param listeners Listeners to be added.
     */
    public SimpleSetProperty(Set<T> set, SetValueChangeListener<T>... listeners) {
        this.set = set;
        this.readOnlySet = Collections.unmodifiableSet(set);

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
        return set.size();
    }

    /**
     * @see AbstractReadableWritableSetProperty#isEmpty()
     * @see Set#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * @see AbstractReadableWritableSetProperty#add(Object)
     * @see Set#add(Object)
     */
    @Override
    public boolean add(T item) {
        // TODO
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends T> items) {
        // TODO
        return false;
    }

    @Override
    public boolean remove(Object item) {
        // TODO
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> items) {
        // TODO
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        // TODO
        return false;
    }

    @Override
    public void clear() {
        // TODO
    }

    /**
     * @see AbstractReadableWritableSetProperty#contains(Object)
     * @see Set#contains(Object)
     */
    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    /**
     * @see AbstractReadableWritableSetProperty#containsAll(Collection)
     * @see Set#containsAll(Collection)
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    /**
     * @see Set#toArray()
     */
    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    /**
     * @see Set#toArray(Object[])
     */
    @SuppressWarnings("unchecked")
    @Override
    public <U> U[] toArray(U[] a) {
        return set.toArray(a);
    }

    @Override
    public Iterator<T> iterator() {
        return readOnlySet.iterator();
    }
}
