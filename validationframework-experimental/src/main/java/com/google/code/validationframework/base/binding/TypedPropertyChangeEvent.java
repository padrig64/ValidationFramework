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

import java.beans.PropertyChangeEvent;

public class TypedPropertyChangeEvent<S, T> extends PropertyChangeEvent {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = 7714148045056915812L;

    public TypedPropertyChangeEvent(S source, String propertyName, T oldValue, T newValue) {
        super(source, propertyName, oldValue, newValue);
    }

    /**
     * @see PropertyChangeEvent#getSource()
     */
    @Override
    public S getSource() {
        return (S) super.getSource();
    }

    /**
     * @see PropertyChangeEvent#getOldValue()
     */
    @Override
    public T getOldValue() {
        return (T) super.getOldValue();
    }

    /**
     * @see PropertyChangeEvent#getNewValue()
     */
    @Override
    public T getNewValue() {
        return (T) super.getNewValue();
    }
}
