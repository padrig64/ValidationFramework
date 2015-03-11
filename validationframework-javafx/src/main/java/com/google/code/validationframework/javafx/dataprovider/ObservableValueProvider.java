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

package com.google.code.validationframework.javafx.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import javafx.beans.value.ObservableValue;

/**
 * Data provider giving the data from a specified {@link ObservableValue}.
 *
 * @param <DPO> Type of value provided.
 */
public class ObservableValueProvider<DPO> implements DataProvider<DPO> {

    /**
     * Observable value to get the data from.
     */
    private final ObservableValue<DPO> observableValue;

    /**
     * Constructor specifying the {@link ObservableValue} to get the data from.
     *
     * @param observableValue Observable value to get the data from.
     */
    public ObservableValueProvider(ObservableValue<DPO> observableValue) {
        this.observableValue = observableValue;
    }

    /**
     * @see DataProvider#getData()
     * @see ObservableValue#getValue()
     */
    @Override
    public DPO getData() {
        DPO value = null;

        if (observableValue != null) {
            value = observableValue.getValue();
        }

        return value;
    }
}
