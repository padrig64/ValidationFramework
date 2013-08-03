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

package com.google.code.validationframework.binding;

import com.google.code.validationframework.base.transform.ToStringTransformer;
import org.junit.Test;

import static com.google.code.validationframework.binding.Binder.bind;
import static org.junit.Assert.assertEquals;

public class BinderTest {

    @Test
    public void testMasterToSlave() {
        Bindable<Integer> first = new Bindable<Integer>(5);
        Bindable<Integer> second = new Bindable<Integer>(0);
        bind(first).to(second);

        assertEquals(Integer.valueOf(5), first.getValue());
        assertEquals(first.getValue(), second.getValue());

        first.setValue(8);

        assertEquals(Integer.valueOf(8), first.getValue());
        assertEquals(first.getValue(), second.getValue());
    }

    @Test
    public void testMasterToSlaveWithTransformation() {
        Bindable<Integer> first = new Bindable<Integer>(5);
        Bindable<String> second = new Bindable<String>("0");
        bind(first).transform(new ToStringTransformer<Integer>()).to(second);

        assertEquals(Integer.valueOf(5), first.getValue());
        assertEquals("5", second.getValue());

        first.setValue(8);

        assertEquals(Integer.valueOf(8), first.getValue());
        assertEquals("8", second.getValue());
    }
}
