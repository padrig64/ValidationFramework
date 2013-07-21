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

package com.google.code.validationframework.base.validator.buildercontext.generalvalidator;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.resulthandler.ResultCollector;
import com.google.code.validationframework.base.transform.Transformer;
import com.google.code.validationframework.base.validator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SingleResultCollectorContext<DPO> {

    private final Collection<Trigger> triggers;
    private final Collection<DataProvider<DPO>> dataProviders;

    public SingleResultCollectorContext(final Collection<Trigger> triggers, final Collection<DataProvider<DPO>>
            dataProviders) {
        this.triggers = triggers;
        this.dataProviders = dataProviders;
    }

    public MultipleResultCollectorContext<DPO> collect(final ResultCollector<?, DPO> resultCollector) {
        if (resultCollector != null) {
            triggers.add(resultCollector);
            dataProviders.add(resultCollector);
        }

        // Change context
        return new MultipleResultCollectorContext<DPO>(triggers, dataProviders);
    }

    public <TDPO> TransformedDataProviderContext transform(final Transformer<DPO, TDPO> ruleInputTransformer) {
        final List<Transformer> transformers = new ArrayList<Transformer>();
        if (ruleInputTransformer != null) {
            transformers.add(ruleInputTransformer);
        }

        return new TransformedDataProviderContext<DPO, TDPO>(triggers, dataProviders,
                GeneralValidator.DataProviderToRuleMapping.EACH_TO_EACH, transformers);
    }

    public <RO> SingleRuleContext<DPO, DPO, RO> check(final Rule<DPO, RO> rule) {
        final List<Rule<DPO, RO>> rules = new ArrayList<Rule<DPO, RO>>();
        if (rule != null) {
            rules.add(rule);
        }

        // Change context
        return new SingleRuleContext<DPO, DPO, RO>(triggers, dataProviders,
                GeneralValidator.DataProviderToRuleMapping.EACH_TO_EACH, null, rules);
    }
}
