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

package com.google.code.validationframework.api.property;

import java.util.Collection;

/**
 * Interface to be implemented by writable set properties.
 * <p>
 * Note that most of the methods are based on the {@link java.util.Set} interface.
 *
 * @param <W> Type of values that can be written to this set property.
 */
public interface WritableSetProperty<W> {

    /**
     * Adds the specified element to the set.
     *
     * @param item Element to be added.
     * @return True if the set was changed, false otherwise.
     * @see java.util.Set#add(Object)
     */
    boolean add(W item);

    /**
     * Adds all the specified elements to the set.
     *
     * @param items Elements to be added.
     * @return True if the set was changed, false otherwise.
     * @see java.util.Set#addAll(Collection)
     */
    boolean addAll(Collection<? extends W> items);

    /**
     * Removes the specified element from the set.
     *
     * @param item Element to be removed.
     * @return True if the set was changed, false otherwise.
     * @see java.util.Set#remove(Object)
     */
    boolean remove(Object item);

    /**
     * Removes all the specified elements from the set.
     *
     * @param items Elements to be removed.
     * @return True if the set was changed, false otherwise.
     * @see java.util.Set#removeAll(Collection)
     * @see #retainAll(java.util.Collection)
     */
    boolean removeAll(Collection<?> items);

    /**
     * Removes all the elements that are not in the specified collection.
     *
     * @param items Elements to be kept.
     * @return True if the set was changed, false otherwise.
     * @see java.util.Set#retainAll(java.util.Collection)
     */
    boolean retainAll(Collection<?> items);

    /**
     * Removes all elements from the set.
     *
     * @see java.util.Set#clear()
     */
    void clear();
}
