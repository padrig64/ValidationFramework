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
import java.util.List;

/**
 * Interface to be implemented by readable list properties.
 * <p>
 * Note that most of the methods are based on the {@link java.util.List} interface.
 *
 * @param <R> Type of values in this list property.
 */
public interface ReadableListProperty<R> extends Iterable<R> {

    /**
     * Adds the specified list item change listener.
     *
     * @param listener Listener to be added.
     */
    void addValueChangeListener(ListValueChangeListener<R> listener);

    /**
     * Removes the specified list item change listener.
     *
     * @param listener Listener to be removed.
     */
    void removeValueChangeListener(ListValueChangeListener<R> listener);

    /**
     * Gets the size of the list.
     *
     * @return List size.
     * @see java.util.List#size()
     */
    int size();

    /**
     * States whether the list is empty or not.
     *
     * @return True if it is empty, false otherwise.
     * @see java.util.List#isEmpty()
     */
    boolean isEmpty();

    /**
     * Gets the element at the specified index.
     *
     * @param index Index of the element to be retrieved.
     * @return Element at the specified index.
     * @see java.util.List#get(int)
     */
    R get(int index);

    /**
     * States whether the list contains the specified element or not.
     *
     * @param item Element whose presence is to be checked.
     * @return True if the element is in the list, false otherwise.
     * @see java.util.List#contains(Object)
     */
    boolean contains(Object item);

    /**
     * States whether the list contains all the specified elements or not.
     *
     * @param items Elements whose presence is to be checked.
     * @return True if all elements are in the list, false otherwise.
     * @see java.util.List#containsAll(Collection)
     */
    boolean containsAll(Collection<?> items);

    /**
     * Returns a {@link List} containing the same elements as this list property, and that cannot be modified directly.
     * <p>
     * Unless specified otherwise in the implementing classes, the returned list always represents the contains of this
     * list property, meaning that if items are added/updated/removed from this list property, the contents of the
     * unmodifiable list will also change.
     *
     * @return Unmodifiable list containing the same elements as this list property.
     */
    List<R> asUnmodifiableList();
}
