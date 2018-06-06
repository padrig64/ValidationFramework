/*
 * Copyright (c) 2016, ValidationFramework Authors
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

package com.google.code.validationframework.base.common;

import com.google.code.validationframework.api.common.DeepDisposable;
import com.google.code.validationframework.api.common.Disposable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Composite throwable handler.
 *
 * @param <T> Type of throwable that can be handled by all handlers.
 */
@Deprecated
public class CompositeThrowableHandler<T extends Throwable> implements ThrowableHandler<T>, DeepDisposable {

    /**
     * Sub-handlers.
     */
    private final List<ThrowableHandler<? super T>> handlers = new ArrayList<ThrowableHandler<? super T>>();

    /**
     * True to dispose all sub-handlers upon {@link #dispose()}, false otherwise.
     */
    private boolean deepDispose = true;

    /**
     * Constructor.
     */
    public CompositeThrowableHandler() {
        // Nothing to be done
    }

    /**
     * Constructor.
     *
     * @param handlers Sub-handlers.
     */
    public CompositeThrowableHandler(Collection<? extends ThrowableHandler<? super T>> handlers) {
        this.handlers.addAll(handlers);
    }

    /**
     * Constructor.
     *
     * @param handlers Sub-handlers.
     */
    public CompositeThrowableHandler(ThrowableHandler<? super T>... handlers) {
        Collections.addAll(this.handlers, handlers);
    }

    /**
     * @see DeepDisposable#getDeepDispose()
     */
    @Override
    public boolean getDeepDispose() {
        return deepDispose;
    }

    /**
     * @see DeepDisposable#setDeepDispose(boolean)
     */
    @Override
    public void setDeepDispose(boolean deepDispose) {
        this.deepDispose = deepDispose;
    }

    /**
     * @see DeepDisposable#dispose()
     */
    @Override
    public void dispose() {
        for (ThrowableHandler<? super T> handler : handlers) {
            if (handler instanceof Disposable) {
                ((Disposable) handler).dispose();
            }
        }
        handlers.clear();
    }

    /**
     * Gets the sub-handlers.
     *
     * @return Sub-handlers.
     */
    public Collection<ThrowableHandler<? super T>> getHandlers() {
        return Collections.unmodifiableList(handlers);
    }

    /**
     * Adds the specified throwable handler.
     *
     * @param handler Throwable handler to be added.
     */
    public void addThrowableHandler(ThrowableHandler<? super T> handler) {
        handlers.add(handler);
    }

    /**
     * Removes the specified throwable handler.
     *
     * @param handler Throwable handler to be removed.
     */
    public void removeThrowableHandler(ThrowableHandler<? super T> handler) {
        handlers.remove(handler);
    }

    /**
     * Removes all sub-handlers without disposing them.
     */
    public void clear() {
        handlers.clear();
    }

    /**
     * @see ThrowableHandler#handleThrowable(Throwable)
     */
    @Override
    public void handleThrowable(T throwable) {
        for (ThrowableHandler<? super T> handler : handlers) {
            handler.handleThrowable(throwable);
        }
    }
}
