/*
 * Copyright (c) 2017, ValidationFramework Authors
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
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.resulthandler.PropertyResultHandler;
import com.google.code.validationframework.base.validator.generalvalidator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DSL-related context of the {@link GeneralValidatorBuilder} after adding the first rule.
 *
 * @param <DPO> Type of data provider output.
 * @param <RI>  Type of rule input.
 * @param <RO>  Type of rule output.
 */
public class SingleRuleContext<DPO, RI, RO> {

    /**
     * Triggers to be added to the validator under construction.
     */
    private final Collection<Trigger> addedTriggers;

    /**
     * Data providers to be added to the validator under construction.
     */
    private final Collection<DataProvider<DPO>> addedDataProviders;

    /**
     * Data provider to rule mapping to be set to the validator under construction.
     */
    private final GeneralValidator.MappingStrategy dataProviderToRuleMapping;

    /**
     * Rule input transformers to be added to the validator under construction.
     */
    private final Collection<Transformer> addedRuleInputTransformers;

    /**
     * Rules to be added to the validator under construction.
     */
    private final Collection<Rule<RI, RO>> addedRules;

    /**
     * Constructor specifying the already known elements of the validator under construction.
     *
     * @param addedTriggers              Triggers to be added.
     * @param addedDataProviders         Data providers to be added.
     * @param dataProviderToRuleMapping  Data provider to rule mapping to be set.
     * @param addedRuleInputTransformers Rule input transformers to be added.
     * @param addedRules                 Rules to be added.
     */
    public SingleRuleContext(Collection<Trigger> addedTriggers, //
                             Collection<DataProvider<DPO>> addedDataProviders, //
                             GeneralValidator.MappingStrategy dataProviderToRuleMapping, //
                             Collection<Transformer> addedRuleInputTransformers, //
                             Collection<Rule<RI, RO>> addedRules) {
        this.addedTriggers = addedTriggers;
        this.addedDataProviders = addedDataProviders;
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.addedRuleInputTransformers = addedRuleInputTransformers;
        this.addedRules = addedRules;
    }

    /**
     * Adds the specified rule to the validator under construction.
     *
     * @param rule Rule to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public MultipleRuleContext<DPO, RI, RO> check(Rule<RI, RO> rule) {
        if (rule != null) {
            addedRules.add(rule);
        }

        // Change context
        return new MultipleRuleContext<DPO, RI, RO>(addedTriggers, addedDataProviders, dataProviderToRuleMapping,
                addedRuleInputTransformers, addedRules);
    }

    /**
     * Adds the specified rules to the validator under construction.
     *
     * @param rules Rules to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public MultipleRuleContext<DPO, RI, RO> check(Collection<Rule<RI, RO>> rules) {
        if (rules != null) {
            addedRules.addAll(rules);
        }

        // Change context
        return new MultipleRuleContext<DPO, RI, RO>(addedTriggers, addedDataProviders, dataProviderToRuleMapping,
                addedRuleInputTransformers, addedRules);
    }

    /**
     * Adds the specified result handler input transformer to the validator under construction.
     *
     * @param resultHandlerInputTransformer Result handler input transformer to be added.
     * @param <TRO>                         Type of transformer output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <TRO> TransformedRuleContext<DPO, RI, RO, TRO> transform(Transformer<RO,
            TRO> resultHandlerInputTransformer) {
        List<Transformer> transformers = new ArrayList<Transformer>();
        if (resultHandlerInputTransformer != null) {
            transformers.add(resultHandlerInputTransformer);
        }

        return new TransformedRuleContext<DPO, RI, RO, TRO>(addedTriggers, addedDataProviders,
                dataProviderToRuleMapping, addedRuleInputTransformers, addedRules,
                GeneralValidator.MappingStrategy.SPLIT, transformers);
    }

    /**
     * Adds the specified result handler to the validator under construction.
     *
     * @param resultHandler Result handler to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public ResultHandlerContext<DPO, RI, RO, RO> handleWith(ResultHandler<RO> resultHandler) {
        List<ResultHandler<RO>> addedResultHandlers = new ArrayList<ResultHandler<RO>>();
        if (resultHandler != null) {
            addedResultHandlers.add(resultHandler);
        }

        // Change context
        return new ResultHandlerContext<DPO, RI, RO, RO>(addedTriggers, addedDataProviders,
                dataProviderToRuleMapping, addedRuleInputTransformers, addedRules,
                GeneralValidator.MappingStrategy.SPLIT, null, addedResultHandlers);
    }

    public ResultHandlerContext<DPO, RI, RO, RO> handleWith(WritableProperty<RO> property) {
        return handleWith(new PropertyResultHandler<RO>(property));
    }

    /**
     * Adds the specified result handlers to the validator under construction.
     *
     * @param resultHandlers Result handlers to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public ResultHandlerContext<DPO, RI, RO, RO> handleWith(Collection<ResultHandler<RO>> resultHandlers) {
        List<ResultHandler<RO>> addedResultHandlers = new ArrayList<ResultHandler<RO>>();
        if (resultHandlers != null) {
            addedResultHandlers.addAll(resultHandlers);
        }

        // Change context
        return new ResultHandlerContext<DPO, RI, RO, RO>(addedTriggers, addedDataProviders,
                dataProviderToRuleMapping, addedRuleInputTransformers, addedRules,
                GeneralValidator.MappingStrategy.SPLIT, null, addedResultHandlers);
    }
}
