/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.google.code.validationframework.base.binding;

import java.io.Serializable;

/**
 * Interface to be implemented by readable property whose value can be gotten by the programmer and which can be used to
 * slave writable properties.
 * <p/>
 * If you are using JavaFX, you should better use JavaFX's property binding mechanism. The binding mechanism provided by
 * the ValidationFramework is mostly meant for Swing and other frameworks that can benefit from it. JavaFX has a much
 * more furnished API to achieve similar goals.
 *
 * @param <T> Type of property value
 *
 * @see WritableProperty
 */
public interface ReadableProperty<T> extends Serializable {

    /**
     * Adds a {@link WritableProperty} as a slave.
     * <p/>
     * Anytime readable property value changes, the slaved {@link WritableProperty}s will also be set.
     *
     * @param slave {@link WritableProperty} to be slaved.
     */
    void addSlave(WritableProperty<T> slave);

    /**
     * Removes the {@link WritableProperty}.
     *
     * @param slave {@link WritableProperty} that should no longer be slaved.
     */
    void removeSlave(WritableProperty<T> slave);

    /**
     * Gets the value of the property.
     * <p/>
     * This method can be called by the programmer or a {@link WritableProperty} that is bound to it.
     *
     * @return Property value.
     */
    T getValue();
}
