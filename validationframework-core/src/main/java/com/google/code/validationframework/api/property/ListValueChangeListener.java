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

import java.util.List;

/**
 * Interface to be implemented by listener to changes in a {@link ReadableListProperty}.
 *
 * @param <R> Type of values in the list property.
 */
public interface ListValueChangeListener<R> {

    /**
     * Called whenever items have been added to the list property.
     *
     * @param listProperty List property to which the items have been added.
     * @param startIndex   Index of the first added item.
     * @param newItems     Newly added items.
     */
    void valuesAdded(ReadableListProperty<R> listProperty, int startIndex, List<R> newItems);

    /**
     * Called whenever items have been replaced in the list property.
     *
     * @param listProperty List property in which the items have been replaced.
     * @param startIndex   Index of the first replaced item.
     * @param oldItems     Previous items.
     * @param newItems     New items.
     */
    void valuesChanged(ReadableListProperty<R> listProperty, int startIndex, List<R> oldItems, List<R> newItems);

    /**
     * Called whenever items have been removed from the list property.
     *
     * @param listProperty List property from which the items have been removed.
     * @param startIndex   Index of the first removed item.
     * @param oldItems     Removed items.
     */
    void valuesRemoved(ReadableListProperty<R> listProperty, int startIndex, List<R> oldItems);
}
