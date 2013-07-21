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

package com.google.code.validationframework.base.validator.dsl.generalvalidator;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.transform.Transformer;
import com.google.code.validationframework.base.validator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @param <DPO> Type of data provider output.
 */
public class MultipleDataProviderContext<DPO> {

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
    public MultipleDataProviderContext(final Collection<Trigger> addedTriggers, //
                                       final Collection<DataProvider<DPO>> addedDataProviders) {
        this.addedTriggers = addedTriggers;
        this.addedDataProviders = addedDataProviders;
    }

    /**
     * Adds the specified data provider to the validator under construction.
     *
     * @param dataProvider Data provider to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public MultipleDataProviderContext<DPO> read(final DataProvider<DPO> dataProvider) {
        if (dataProvider != null) {
            addedDataProviders.add(dataProvider);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    /**
     * Adds the specified data providers to the validator under construction.
     *
     * @param dataProviders Data providers to be added.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public MultipleDataProviderContext<DPO> read(final Collection<DataProvider<DPO>> dataProviders) {
        if (dataProviders != null) {
            addedDataProviders.addAll(dataProviders);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    /**
     * Makes the validator process each data provider independently.<br>This corresponds to the use of {@link
     * GeneralValidator.DataProviderToRuleMapping#EACH_TO_EACH}.
     *
     * @return Context allowing further construction of the validator using the DSL.
     *
     * @see {@link GeneralValidator.DataProviderToRuleMapping#EACH_TO_EACH}.
     */
    public ForEachDataProviderContext<DPO> forEach() {
        // Change context
        return new ForEachDataProviderContext<DPO>(addedTriggers, addedDataProviders);
    }

    /**
     * Adds the specified rule input transformer to the validator under construction.
     *
     * @param ruleInputTransformer Rule input transformer to be added.
     * @param <TDPO>               Type of transformer output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <TDPO> TransformedDataProviderContext transform(final Transformer<Collection<DPO>,
            TDPO> ruleInputTransformer) {
        final List<Transformer> transformers = new ArrayList<Transformer>();
        if (ruleInputTransformer != null) {
            transformers.add(ruleInputTransformer);
        }

        return new TransformedDataProviderContext<DPO, TDPO>(addedTriggers, addedDataProviders,
                GeneralValidator.DataProviderToRuleMapping.ALL_TO_EACH, transformers);
    }

    /**
     * Adds the specified rule to the validator under construction.
     *
     * @param rule Rule to be added.
     * @param <RO> Type of rule output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <RO> SingleRuleContext<DPO, Collection<DPO>, RO> check(final Rule<Collection<DPO>, RO> rule) {
        final List<Rule<Collection<DPO>, RO>> addedRules = new ArrayList<Rule<Collection<DPO>, RO>>();
        if (rule != null) {
            addedRules.add(rule);
        }

        // Change context
        return new SingleRuleContext<DPO, Collection<DPO>, RO>(addedTriggers, addedDataProviders,
                GeneralValidator.DataProviderToRuleMapping.ALL_TO_EACH, null, addedRules);
    }

    /**
     * Adds the specified rules to the validator under construction.
     *
     * @param rules Rules to be added.
     * @param <RO>  Type of rule output.
     *
     * @return Context allowing further construction of the validator using the DSL.
     */
    public <RO> SingleRuleContext<DPO, Collection<DPO>, RO> check(final Collection<Rule<Collection<DPO>, RO>> rules) {
        final List<Rule<Collection<DPO>, RO>> addedRules = new ArrayList<Rule<Collection<DPO>, RO>>();
        if (rules != null) {
            addedRules.addAll(rules);
        }

        // Change context
        return new SingleRuleContext<DPO, Collection<DPO>, RO>(addedTriggers, addedDataProviders,
                GeneralValidator.DataProviderToRuleMapping.ALL_TO_EACH, null, addedRules);
    }
}
