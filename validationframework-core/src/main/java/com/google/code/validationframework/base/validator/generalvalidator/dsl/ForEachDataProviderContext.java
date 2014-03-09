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
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.validator.generalvalidator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * DSL-related context of the {@link GeneralValidatorBuilder} after using the {@link GeneralValidator
 * .MappingStrategy#SPLIT} mapping.
 *
 * @param <DPO> Type of data provider output.
 */
public class ForEachDataProviderContext<DPO> {

    /**
     * Triggers to be added to the validator under construction.
     */
    private final Collection<Trigger> addedTriggers;

    /**
     * Data providers to be added to the validator under construction.
     */
    private final Collection<DataProvider<DPO>> addedDataProviders;

    /**
     * Constructor specifying the already known elements of the validator under construction.
     *
     * @param addedTriggers      Triggers to be added.
     * @param addedDataProviders Data providers to be added.
     */
    public ForEachDataProviderContext(Collection<Trigger> addedTriggers, //
                                      Collection<DataProvider<DPO>> addedDataProviders) {
        this.addedTriggers = addedTriggers;
        this.addedDataProviders = addedDataProviders;
    }

    /**
     * Adds the specified rule input transformer to the validator under construction.
     *
     * @param ruleInputTransformer Rule input transformer to be added.
     * @param <TDPO>               Type of transformer output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <TDPO> TransformedDataProviderContext transform(Transformer<DPO, TDPO> ruleInputTransformer) {
        List<Transformer> transformers = new ArrayList<Transformer>();
        if (ruleInputTransformer != null) {
            transformers.add(ruleInputTransformer);
        }

        // Change context
        return new TransformedDataProviderContext<DPO, TDPO>(addedTriggers, addedDataProviders,
                GeneralValidator.MappingStrategy.SPLIT, transformers);
    }

    /**
     * Adds the specified rule to the validator under construction.
     *
     * @param rule Rule to be added.
     * @param <RO> Type of rule output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <RO> SingleRuleContext<DPO, DPO, RO> check(Rule<DPO, RO> rule) {
        List<Rule<DPO, RO>> addedRules = new ArrayList<Rule<DPO, RO>>();
        if (rule != null) {
            addedRules.add(rule);
        }

        // Change context
        return new SingleRuleContext<DPO, DPO, RO>(addedTriggers, addedDataProviders,
                GeneralValidator.MappingStrategy.SPLIT, null, addedRules);
    }

    /**
     * Adds the specified rules to the validator under construction.
     *
     * @param rules Rules to be added.
     * @param <RO>  Type of rule output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <RO> SingleRuleContext<DPO, DPO, RO> check(Collection<Rule<DPO, RO>> rules) {
        List<Rule<DPO, RO>> addedRules = new ArrayList<Rule<DPO, RO>>();
        if (rules != null) {
            addedRules.addAll(rules);
        }

        // Change context
        return new SingleRuleContext<DPO, DPO, RO>(addedTriggers, addedDataProviders,
                GeneralValidator.MappingStrategy.SPLIT, null, addedRules);
    }
}
