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

import java.util.ArrayList;
import java.util.List;

public class Bindable<T> implements Master<T>, Slave<T> {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = 6820249070710960455L;

    private final List<Slave<T>> slaves = new ArrayList<Slave<T>>();

    private T value = null;

    private boolean settingValue = false;

    public Bindable() {
        this(null);
    }

    public Bindable(T value) {
        this.value = value;
    }

    @Override
    public void addSlave(Slave<T> slave) {
        slaves.add(slave);
    }

    @Override
    public void removeSlave(Slave<T> slave) {
        slaves.remove(slave);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public void setValue(T value) {
        if (!settingValue) {
            settingValue = true;

            this.value = value;
            notifySlaves();

            settingValue = false;
        }
    }

    protected void notifySlaves() {
        for (Slave<T> slave : slaves) {
            slave.setValue(value);
        }
    }
}
