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

package com.google.code.validationframework.base.validator.oldcontext;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.transform.Transformer;
import com.google.code.validationframework.base.validator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataProviderTransformedCombinedTransformedContext<DPO, TDPO> {

    private final Collection<Trigger> triggers;
    private final Collection<DataProvider<DPO>> dataProviders;
    private final Collection<Transformer> dataProvidersOutputTransformers;
    private final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping;
    private final Collection<Transformer> combinedDataProvidersOutputTransformers;

    public DataProviderTransformedCombinedTransformedContext(final Collection<Trigger> triggers, //
                                                             final Collection<DataProvider<DPO>> dataProviders, //
                                                             final Collection<Transformer>
                                                                     dataProvidersOutputTransformers, //
                                                             final GeneralValidator.DataProviderToRuleMapping
                                                                     dataProviderToRuleMapping, //
                                                             final Collection<Transformer>
                                                                     combinedDataProvidersOutputTransformers) {
        this.triggers = triggers;
        this.dataProviders = dataProviders;
        this.dataProvidersOutputTransformers = dataProvidersOutputTransformers;
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.combinedDataProvidersOutputTransformers = combinedDataProvidersOutputTransformers;
    }

    public <TTDPO> DataProviderTransformedCombinedTransformedContext<DPO,
            TTDPO> transform(final Transformer<Collection<TDPO>, TTDPO> dataProvidersOutputTransformer) {
        if (dataProvidersOutputTransformer != null) {
            combinedDataProvidersOutputTransformers.add(dataProvidersOutputTransformer);
        }

        // Stay in the same context but create a new instance because the output type has changed
        return new DataProviderTransformedCombinedTransformedContext<DPO, TTDPO>(triggers, dataProviders,
                dataProvidersOutputTransformers, dataProviderToRuleMapping, combinedDataProvidersOutputTransformers);
    }

    public <RO> RuleContext<DPO, TDPO, RO> check(final Rule<TDPO, RO> rule) {
        final List<Rule<TDPO, RO>> rules = new ArrayList<Rule<TDPO, RO>>();
        if (rule != null) {
            rules.add(rule);
        }

        // Change context
        return new RuleContext<DPO, TDPO, RO>(triggers, dataProviders, dataProvidersOutputTransformers,
                dataProviderToRuleMapping, combinedDataProvidersOutputTransformers, rules);
    }

    public <RO> RuleContext<DPO, TDPO, RO> check(final Collection<Rule<TDPO, RO>> rules) {
        final List<Rule<TDPO, RO>> ruleList = new ArrayList<Rule<TDPO, RO>>();
        if (rules != null) {
            ruleList.addAll(rules);
        }

        // Change context
        return new RuleContext<DPO, TDPO, RO>(triggers, dataProviders, dataProvidersOutputTransformers, dataProviderToRuleMapping, combinedDataProvidersOutputTransformers, ruleList);
    }
}
