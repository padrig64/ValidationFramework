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
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.transform.Transformer;
import com.google.code.validationframework.base.validator.GeneralValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RuleTransformedContext<DPO, RI, RO, TRO> {

    private final Collection<Trigger> triggers;
    private final Collection<DataProvider<DPO>> dataProviders;
    private final Collection<Transformer> dataProvidersOutputTransformers;
    private final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping;
    private final Collection<Transformer> combinedDataProvidersOutputTransformers;
    private final Collection<Rule<RI, RO>> rules;
    private final Collection<Transformer> rulesOutputTransformers;

    public RuleTransformedContext(final Collection<Trigger> triggers, //
            final Collection<DataProvider<DPO>> dataProviders, //
            final Collection<Transformer> dataProvidersOutputTransformers,
            final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping, //
            final Collection<Transformer> combinedDataProvidersOutputTransformers, final Collection<Rule<RI,
            RO>> rules, //
            final Collection<Transformer> rulesOutputTransformers) {
        this.triggers = triggers;
        this.dataProviders = dataProviders;
        this.dataProvidersOutputTransformers = dataProvidersOutputTransformers;
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.combinedDataProvidersOutputTransformers = combinedDataProvidersOutputTransformers;
        this.rules = rules;
        this.rulesOutputTransformers = rulesOutputTransformers;
    }

    public <TTRO> RuleTransformedContext<DPO, RI, RO, TTRO> transform(final Transformer<TRO,
            TTRO> rulesOutputTransformer) {
        if (rulesOutputTransformer != null) {
            rulesOutputTransformers.add(rulesOutputTransformer);
        }

        // Stay in the same context, but create a new instance because the output type has changed
        return new RuleTransformedContext<DPO, RI, RO, TTRO>(triggers, dataProviders,
                dataProvidersOutputTransformers, dataProviderToRuleMapping, combinedDataProvidersOutputTransformers,
                rules, rulesOutputTransformers);
    }

    public RuleTransformedCombinedContext<DPO, RI, RO, TRO> combine() {
        // Change context
        return new RuleTransformedCombinedContext<DPO, RI, RO, TRO>(triggers, dataProviders,
                dataProvidersOutputTransformers, dataProviderToRuleMapping, combinedDataProvidersOutputTransformers,
                rules, null, GeneralValidator.RuleToResultHandlerMapping.ALL_TO_EACH);
    }

    public ResultHandlerContext<DPO, RI, RO, TRO> handleWith(final ResultHandler<TRO> resultHandler) {
        final List<ResultHandler<TRO>> resultHandlers = new ArrayList<ResultHandler<TRO>>();
        if (resultHandler != null) {
            resultHandlers.add(resultHandler);
        }

        // Change context
        return new ResultHandlerContext<DPO, RI, RO, TRO>(triggers, dataProviders, dataProvidersOutputTransformers,
                dataProviderToRuleMapping, combinedDataProvidersOutputTransformers, rules, rulesOutputTransformers,
                GeneralValidator.RuleToResultHandlerMapping.EACH_TO_EACH, rulesOutputTransformers, resultHandlers);
    }

    public ResultHandlerContext<DPO, RI, RO, TRO> handleWith(final Collection<ResultHandler<TRO>> resultHandlers) {
        final List<ResultHandler<TRO>> resultHandlerList = new ArrayList<ResultHandler<TRO>>();
        if (resultHandlers != null) {
            resultHandlerList.addAll(resultHandlers);
        }

        // Change context
        return new ResultHandlerContext<DPO, RI, RO, TRO>(triggers, dataProviders, dataProvidersOutputTransformers,
                dataProviderToRuleMapping, combinedDataProvidersOutputTransformers, rules, rulesOutputTransformers, GeneralValidator.RuleToResultHandlerMapping.EACH_TO_EACH, rulesOutputTransformers, resultHandlerList);
    }
}
