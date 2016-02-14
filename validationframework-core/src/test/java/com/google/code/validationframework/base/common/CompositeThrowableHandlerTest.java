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

import com.google.code.validationframework.api.common.Disposable;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @see CompositeThrowableHandler
 */
public class CompositeThrowableHandlerTest {

    @Test
    public void testComposite() {
        DisposableHandler handler1 = mock(DisposableHandler.class);
        DisposableHandler handler2 = mock(DisposableHandler.class);
        DisposableHandler handler3 = mock(DisposableHandler.class);

        CompositeThrowableHandler<RuntimeException> composite = new CompositeThrowableHandler<RuntimeException>
                (handler1);
        composite.addThrowableHandler(handler2);
        composite.addThrowableHandler(handler3);

        composite.dispose();

        composite.dispose();
        composite.dispose();
        composite.dispose();

        verify(handler1).dispose();
        verifyNoMoreInteractions(handler1);
        verify(handler2).dispose();
        verifyNoMoreInteractions(handler2);
        verify(handler3).dispose();
        verifyNoMoreInteractions(handler3);
    }

    @Test
    public void testCompile() {
        CompositeThrowableHandler<Throwable> handlerT = null;
        CompositeThrowableHandler<Exception> handlerEx = null;
        CompositeThrowableHandler<RuntimeException> handlerRTEx = null;
        CompositeThrowableHandler<Error> handlerEr = null;

        CompositeThrowableHandler<RuntimeException> compositeRTEx = new
                CompositeThrowableHandler<RuntimeException>(new LogErrorUncheckedExceptionHandler(),
                handlerT, handlerEx, handlerRTEx);
        compositeRTEx.addThrowableHandler(handlerT);
        compositeRTEx.addThrowableHandler(handlerEx);
        compositeRTEx.addThrowableHandler(handlerRTEx);

        CompositeThrowableHandler<Throwable> compositeT = new
                CompositeThrowableHandler<Throwable>(new LogErrorUncheckedExceptionHandler(), handlerT);
        compositeT.addThrowableHandler(handlerT);

        CompositeThrowableHandler<Error> compositeEr = new CompositeThrowableHandler<Error>(new
                LogErrorUncheckedExceptionHandler(), handlerT, handlerEr);
        compositeEr.addThrowableHandler(handlerT);
        compositeEr.addThrowableHandler(handlerEr);

        CompositeThrowableHandler<Exception> compositeEx = new
                CompositeThrowableHandler<Exception>(new LogErrorUncheckedExceptionHandler(), handlerT, handlerEx);
        compositeEx.addThrowableHandler(handlerT);
        compositeEx.addThrowableHandler(handlerEx);
    }

    private interface DisposableHandler extends ThrowableHandler<Throwable>, Disposable {
        // Nothing to be done
    }
}
