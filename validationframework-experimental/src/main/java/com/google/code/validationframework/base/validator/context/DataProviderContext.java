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

package com.google.code.validationframework.base.validator.context;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.transform.Transformer;
import com.google.code.validationframework.base.validator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DataProviderContext<DPO> {

    private final Collection<Trigger> triggers;
    private final Collection<DataProvider<DPO>> dataProviders;

    public DataProviderContext(final Collection<Trigger> triggers, //
            final Collection<DataProvider<DPO>> dataProviders) {
        this.triggers = triggers;
        this.dataProviders = dataProviders;
    }

    public DataProviderContext<DPO> read(final DataProvider<DPO> dataProvider) {
        if (dataProvider != null) {
            dataProviders.add(dataProvider);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    public DataProviderContext<DPO> read(final Collection<DataProvider<DPO>> dataProviders) {
        if (dataProviders != null) {
            this.dataProviders.addAll(dataProviders);
        }

        // Stay in the same context and re-use the same instance because no type has changed
        return this;
    }

    public <TDPO> DataProviderTransformedContext<DPO, TDPO> transform(final Transformer<DPO,
            TDPO> dataProvidersOutputTransformer) {
        final List<Transformer> transformers = new ArrayList<Transformer>();
        if (dataProvidersOutputTransformer != null) {
            transformers.add(dataProvidersOutputTransformer);
        }

        // Change context
        return new DataProviderTransformedContext<DPO, TDPO>(triggers, dataProviders, transformers);
    }

    public DataProviderTransformedCombinedContext<DPO, DPO> combine() {
        // Change context
        return new DataProviderTransformedCombinedContext<DPO, DPO>(triggers, dataProviders, null,
                GeneralValidator.DataProviderToRuleMapping.ALL_TO_EACH);
    }

    public <RO> RuleContext<DPO, DPO, RO> check(final Rule<DPO, RO> rule) {
        final List<Rule<DPO, RO>> rules = new ArrayList<Rule<DPO, RO>>();
        if (rule != null) {
            rules.add(rule);
        }

        // Change context
        return new RuleContext<DPO, DPO, RO>(triggers, dataProviders, null,
                GeneralValidator.DataProviderToRuleMapping.EACH_TO_EACH, null, rules);
    }

    public <RO> RuleContext<DPO, DPO, RO> check(final Collection<Rule<DPO, RO>> rules) {
        final List<Rule<DPO, RO>> ruleList = new ArrayList<Rule<DPO, RO>>();
        if (rules != null) {
            ruleList.addAll(rules);
        }

        // Change context
        return new RuleContext<DPO, DPO, RO>(triggers, dataProviders, null, GeneralValidator.DataProviderToRuleMapping.EACH_TO_EACH, null, ruleList);
    }
}
