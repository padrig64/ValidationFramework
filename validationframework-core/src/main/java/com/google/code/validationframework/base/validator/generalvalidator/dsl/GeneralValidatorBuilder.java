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

package com.google.code.validationframework.base.validator.generalvalidator.dsl;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.api.validator.SimpleValidator;
import com.google.code.validationframework.base.dataprovider.PropertyValueProvider;
import com.google.code.validationframework.base.resulthandler.ResultCollector;
import com.google.code.validationframework.base.resulthandler.SimpleResultCollector;
import com.google.code.validationframework.base.trigger.PropertyValueChangeTrigger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * General validator builder that can be used to construct a {@link com.google.code.validationframework.base
 * .validator.generalvalidator.GeneralValidator} using a DSL.
 * <p/>
 * Note that the {@link com.google.code.validationframework.base.validator.generalvalidator.GeneralValidator} will be
 * constructed and effective only after the addition of the first result handler.
 *
 * @see com.google.code.validationframework.base.validator.generalvalidator.GeneralValidator
 */
public final class GeneralValidatorBuilder {

    /**
     * Private constructor for utility class.
     */
    private GeneralValidatorBuilder() {
        // Nothing to be done
    }

    /**
     * Adds the specified trigger to the validator under construction.
     *
     * @param trigger Trigger to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public static TriggerContext on(Trigger trigger) {
        List<Trigger> addedTriggers = new ArrayList<Trigger>();
        if (trigger != null) {
            addedTriggers.add(trigger);
        }
        return new TriggerContext(addedTriggers);
    }

    public static TriggerContext on(ReadableProperty<?> property) {
        return on(new PropertyValueChangeTrigger(property));
    }

    /**
     * Adds the specified triggers to the validator under construction.
     *
     * @param triggers Triggers to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public static TriggerContext on(Collection<Trigger> triggers) {
        List<Trigger> addedTriggers = new ArrayList<Trigger>();
        if (triggers != null) {
            addedTriggers.addAll(triggers);
        }
        return new TriggerContext(addedTriggers);
    }

    /**
     * Adds the specified result collector as trigger and data provider to the validator under construction.
     *
     * @param resultCollector Result collector to be added.
     * @param <DPO>           Type of data provider output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public static <DPO> SingleResultCollectorContext<DPO> collect(ResultCollector<?, DPO> resultCollector) {
        List<Trigger> addedTriggers = new ArrayList<Trigger>();
        List<DataProvider<DPO>> addedDataProviders = new ArrayList<DataProvider<DPO>>();
        if (resultCollector != null) {
            addedTriggers.add(resultCollector);
            addedDataProviders.add(resultCollector);
        }

        return new SingleResultCollectorContext<DPO>(addedTriggers, addedDataProviders);
    }

    public static <DPO> SingleResultCollectorContext<DPO> collect(ReadableProperty<DPO> property) {
        List<Trigger> addedTriggers = new ArrayList<Trigger>();
        List<DataProvider<DPO>> addedDataProviders = new ArrayList<DataProvider<DPO>>();
        if (property != null) {
            addedTriggers.add(new PropertyValueChangeTrigger(property));
            addedDataProviders.add(new PropertyValueProvider<DPO>(property));
        }

        return new SingleResultCollectorContext<DPO>(addedTriggers, addedDataProviders);
    }

    /**
     * Adds the specified result collectors as triggers and data providers to the validator under construction.
     *
     * @param resultCollectors Result collectors to be added.
     * @param <DPO>            Type of data provider output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public static <DPO> MultipleResultCollectorContext<DPO> collect(Collection<ResultCollector<?,
            DPO>> resultCollectors) {
        List<Trigger> addedTriggers = new ArrayList<Trigger>();
        List<DataProvider<DPO>> addedDataProviders = new ArrayList<DataProvider<DPO>>();
        if (resultCollectors != null) {
            addedTriggers.addAll(resultCollectors);
            addedDataProviders.addAll(resultCollectors);
        }

        return new MultipleResultCollectorContext<DPO>(addedTriggers, addedDataProviders);
    }

    /**
     * Adds a new result collector as trigger and data provider to the validator under construction, in order to collect
     * the results of the specified validator.
     *
     * @param validator Validator to collect the result from.<br>A result collector will be created, added as a result
     *                  handler to the specified validator, and added as a trigger and data provider in the validator
     *                  under construction.
     * @param <DPO>     Type of data provider output, that is to say, type of result of the specified validator.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public static <DPO> SingleResultCollectorContext<DPO> collect(SimpleValidator<?, ?, ?, ?, ?, ?,
            ResultHandler<DPO>, DPO> validator) {
        List<Trigger> addedTriggers = new ArrayList<Trigger>();
        List<DataProvider<DPO>> addedDataProviders = new ArrayList<DataProvider<DPO>>();
        if (validator != null) {
            // Create result collector
            SimpleResultCollector<DPO> resultCollector = new SimpleResultCollector<DPO>();

            // Register result collector in specified validator
            validator.addResultHandler(resultCollector);

            // Result result collector in validator under construction
            addedTriggers.add(resultCollector);
            addedDataProviders.add(resultCollector);
        }

        return new SingleResultCollectorContext<DPO>(addedTriggers, addedDataProviders);
    }
}
