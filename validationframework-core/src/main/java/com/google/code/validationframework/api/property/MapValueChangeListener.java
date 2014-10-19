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

import java.util.Map;

/**
 * Interface to be implemented by listener to changes in a {@link ReadableMapProperty}.
 *
 * @param <K> Type of keys maintained by the map property.
 * @param <R> Type mapped values that can be read from the map property.
 */
public interface MapValueChangeListener<K, R> {

    /**
     * Called when entries have been added to the map property.
     *
     * @param mapProperty Map property to which values have been added.
     * @param newValues   Newly added values.
     */
    void valuesAdded(ReadableMapProperty<K, R> mapProperty, Map<K, R> newValues);

    /**
     * Called when entries have been replaced in map property.
     *
     * @param mapProperty Map property in which values have been replaced.
     * @param oldValues   Previous values.
     * @param newValues   New values.
     */
    void valuesChanged(ReadableMapProperty<K, R> mapProperty, Map<K, R> oldValues, Map<K, R> newValues);

    /**
     * Called when entries have been removed from the map property.
     *
     * @param mapProperty Map property from which values have been removed.
     * @param oldValues   Removed values.
     */
    void valuesRemoved(ReadableMapProperty<K, R> mapProperty, Map<K, R> oldValues);
}
