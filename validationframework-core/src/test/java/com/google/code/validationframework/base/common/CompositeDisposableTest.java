/*
 * Copyright (c) 2015, ValidationFramework Authors
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

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @see CompositeDisposable
 */
public class CompositeDisposableTest {

    @Test
    public void firstConstructor() {
        CompositeDisposable composite = new CompositeDisposable(new Dummy(true));
        composite.addDisposable(new Dummy(true));
        composite.addDisposable(new Dummy(true));

        composite.dispose();

        // Should not do anything
        composite.dispose();
        composite.dispose();
    }

    @Test
    public void secondConstructor() {
        CompositeDisposable composite = new CompositeDisposable(Arrays.asList(new Dummy(true), new Dummy(true)));
        composite.addDisposable(new Dummy(true));

        composite.dispose();

        // Should not do anything
        composite.dispose();
        composite.dispose();
    }

    @Test
    public void addRemove() {
        CompositeDisposable composite = new CompositeDisposable();

        Dummy disposable = new Dummy(false);
        composite.addDisposable(disposable);
        composite.removeDisposable(disposable);

        // Should not thrown any exception
        composite.addDisposable(null);
        composite.removeDisposable(null);

        // Should not do anything
        composite.dispose();
        composite.dispose();
        composite.dispose();
    }

    @Test
    public void clear() {
        CompositeDisposable composite = new CompositeDisposable(new Dummy(false), new Dummy(false), new Dummy(false));

        composite.clear();

        // Should not do anything
        composite.dispose();
        composite.dispose();
        composite.dispose();
    }

    private static class Dummy implements Disposable {

        private final boolean disposeMethodShouldBeCalled;

        private boolean disposeMethodAlreadyCalled = false;

        public Dummy(boolean disposeMethodShouldBeCalled) {
            this.disposeMethodShouldBeCalled = disposeMethodShouldBeCalled;
        }

        @Override
        public void dispose() {
            assertTrue(disposeMethodShouldBeCalled);
            assertFalse(disposeMethodAlreadyCalled);
            disposeMethodAlreadyCalled = true;
        }
    }
}
