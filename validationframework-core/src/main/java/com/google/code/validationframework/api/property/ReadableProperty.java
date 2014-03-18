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

/**
 * Interface to be implemented by readable property that can notify {@link ValueChangeListener}s.
 * <p/>
 * If you are using JavaFX, you should better use JavaFX's property binding mechanism. The binding mechanism provided by
 * the ValidationFramework is very simple and mostly meant for Swing and other frameworks that can benefit from it.
 * JavaFX has a much more furnished API to achieve similar goals and much more.
 *
 * @param <R> Type of data that can be read from this property.
 *
 * @see WritableProperty
 */
public interface ReadableProperty<R> {

    /**
     * Adds a {@link WritableProperty} as a slave.
     * <p/>
     * Anytime readable property value changes, the slaved {@link WritableProperty}s will also be set.
     *
     * @param listener {@link WritableProperty} to be slaved.
     */
    void addValueChangeListener(ValueChangeListener<R> listener);

    /**
     * Removes the {@link WritableProperty}.
     *
     * @param listener {@link WritableProperty} that should no longer be slaved.
     */
    void removeValueChangeListener(ValueChangeListener<R> listener);

    /**
     * Gets the value of the property.
     * <p/>
     * This method can be called by the programmer or a {@link WritableProperty} that is bound to it.
     *
     * @return Property value.
     */
    R getValue();
}
