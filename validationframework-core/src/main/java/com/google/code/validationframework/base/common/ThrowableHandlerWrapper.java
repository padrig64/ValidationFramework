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

/**
 * Convenient wrapper for {@link UncheckedExceptionHandler}s that implements the {@link ThrowableHandler} interface.
 * <p/>
 * All unchecked exceptions will be forwarded to the wrapped {@link UncheckedExceptionHandler}.
 */
public class ThrowableHandlerWrapper implements ThrowableHandler<Throwable> {

    /**
     * Logger for this class.
     */
    private static final ThrowableHandler<Throwable> FALLBACK_HANDLER = new RethrowUncheckedExceptionHandler();

    /**
     * Wrapped unchecked exception handler.
     */
    private final UncheckedExceptionHandler wrapped;

    /**
     * Constructor.
     *
     * @param wrapped Unchecked exception handler to be wrapped.
     */
    public ThrowableHandlerWrapper(UncheckedExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    /**
     * @see ThrowableHandler#handleThrowable(Throwable)
     */
    @Override
    public void handleThrowable(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            wrapped.handleException((RuntimeException) throwable);
        } else if (throwable instanceof Error) {
            wrapped.handleError((Error) throwable);
        } else {
            FALLBACK_HANDLER.handleThrowable(throwable);
        }
    }
}
