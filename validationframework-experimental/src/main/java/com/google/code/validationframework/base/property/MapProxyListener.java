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

package com.google.code.validationframework.base.property;

import java.util.Map;

/**
 * Interface to be implemented by listeners to changes made on a {@link Map} using a {@link MapProxy}.
 *
 * @param <K> Type of keys maintained by the proxied map.
 * @param <V> Type mapped values.
 *
 * @see MapProxy
 */
public interface MapProxyListener<K, V> {

    /**
     * Called when entries have been added to the proxied map.
     *
     * @param mapProxy Map proxy holding the modified map.
     * @param added    Entries added to the proxied map.
     */
    void entriesAdded(MapProxy<K, V> mapProxy, Map<K, V> added);

    /**
     * Called when entries have been changed in the proxied map.
     *
     * @param mapProxy Map proxy holding the modified map.
     * @param changed  Entries changed in the proxied map.
     */
    void entriesChanged(MapProxy<K, V> mapProxy, Map<K, V> changed);

    /**
     * Called when entries have been removed from the proxied map.
     *
     * @param mapProxy Map proxy holding the modified map.
     * @param removed  Entries removed from the proxied map.
     */
    void entriesRemoved(MapProxy<K, V> mapProxy, Map<K, V> removed);
}
