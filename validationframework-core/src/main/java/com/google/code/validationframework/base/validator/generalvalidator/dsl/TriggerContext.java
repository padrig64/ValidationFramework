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

package com.google.code.validationframework.base.validator.generalvalidator.dsl;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.trigger.Trigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO
 */
public class TriggerContext {

    /**
     * Triggers to be added to the validator under construction.
     */
    private final Collection<Trigger> addedTriggers;

    /**
     * Constructor specifying the already known elements of the validator under construction.
     *
     * @param addedTriggers Triggers to be added.
     */
    public TriggerContext(final Collection<Trigger> addedTriggers) {
        this.addedTriggers = addedTriggers;
    }

    /**
     * Adds the specified trigger to the validator under construction.
     *
     * @param trigger Trigger to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public TriggerContext on(final Trigger trigger) {
        if (trigger != null) {
            addedTriggers.add(trigger);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    /**
     * Adds the specified triggers to the validator under construction.
     *
     * @param triggers Triggers to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public TriggerContext on(final Collection<Trigger> triggers) {
        if (triggers != null) {
            addedTriggers.addAll(triggers);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    /**
     * Adds the specified data provider to the validator under construction.
     *
     * @param dataProvider Data provider to be added.
     * @param <DPO>        Type of data provider output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <DPO> SingleDataProviderContext<DPO> read(final DataProvider<DPO> dataProvider) {
        final List<DataProvider<DPO>> addedDataProviders = new ArrayList<DataProvider<DPO>>();
        if (dataProvider != null) {
            addedDataProviders.add(dataProvider);
        }

        // Change context
        return new SingleDataProviderContext<DPO>(addedTriggers, addedDataProviders);
    }

    /**
     * Adds the specified data providers to the validator under construction.
     *
     * @param dataProviders Data providers to be added.
     * @param <DPO>         Type of data provider output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <DPO> MultipleDataProviderContext<DPO> read(final Collection<DataProvider<DPO>> dataProviders) {
        final List<DataProvider<DPO>> addedDataProviders = new ArrayList<DataProvider<DPO>>();
        if (dataProviders != null) {
            addedDataProviders.addAll(dataProviders);
        }

        // Change context
        return new MultipleDataProviderContext<DPO>(addedTriggers, addedDataProviders);
    }
}
