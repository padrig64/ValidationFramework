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

package com.google.code.validationframework.api.property;

import java.util.Collection;

/**
 * Interface to be implemented by writable list properties.
 * <p/>
 * Note that most of the methods are based on the {@link java.util.List} interface.
 *
 * @param <W> Type of values that can be written to this list property.
 */
public interface WritableListProperty<W> {

    /**
     * @see java.util.List#set(int, Object)
     */
    W set(int index, W item);

    /**
     * @see java.util.List#add(Object)
     */
    boolean add(W item);

    /**
     * @see java.util.List#add(int, Object)
     */
    void add(int index, W item);

    /**
     * @see java.util.List#addAll(Collection)
     */
    boolean addAll(Collection<? extends W> items);

    /**
     * @see java.util.List#addAll(int, Collection)
     */
    boolean addAll(int index, Collection<? extends W> items);

    /**
     * @see java.util.List#remove(Object)
     */
    boolean remove(Object item);

    /**
     * @see java.util.List#remove(int)
     */
    W remove(int index);

    /**
     * @see java.util.List#removeAll(Collection)
     */
    boolean removeAll(Collection<?> items);

    /**
     * @see java.util.List#clear()
     */
    void clear();
}
