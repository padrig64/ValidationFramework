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

public class ResultHandlerContext<DPO, RI, RO, RHI> {

    private final List<Trigger> registeredTriggers;
    private final List<DataProvider<DPO>> registeredDataProviders;
    private final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping;
    private final List<Rule<RI, RO>> registeredRules;
    private final GeneralValidator.RuleToResultHandlerMapping ruleToResultHandlerMapping;
    private final Transformer<Collection<RO>, RHI> rulesOutputToResultHandlersInputTransformer;
    private final List<ResultHandler<RHI>> registeredResultHandlers;

    public ResultHandlerContext(final List<Trigger> registeredTriggers, final List<DataProvider<DPO>>
            registeredDataProviders, final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping,
                                final List<Rule<RI, RO>> registeredRules,
                                final GeneralValidator.RuleToResultHandlerMapping ruleToResultHandlerMapping,
                                final Transformer<Collection<RO>, RHI> rulesOutputToResultHandlersInputTransformer,
                                final ResultHandler<RHI> resultHandler) {
        this.registeredTriggers = registeredTriggers;
        this.registeredDataProviders = registeredDataProviders;
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.registeredRules = registeredRules;
        this.registeredResultHandlers = new ArrayList<ResultHandler<RHI>>();
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
        this.registeredResultHandlers.add(resultHandler);
        this.rulesOutputToResultHandlersInputTransformer = rulesOutputToResultHandlersInputTransformer;
    }

    public ResultHandlerContext<DPO, RI, RO, RHI> handleWith(final ResultHandler<RHI> resultHandler) {
        if (resultHandler != null) {
            registeredResultHandlers.add(resultHandler);
        }
        return this;
    }

    public GeneralValidator<DPO, RI, RO, RHI> build() {
        // Create validator
        final GeneralValidator<DPO, RI, RO, RHI> validator = new GeneralValidator<DPO, RI, RO,
                RHI>(dataProviderToRuleMapping, ruleToResultHandlerMapping);

        // Add triggers
        for (final Trigger trigger : registeredTriggers) {
            validator.addTrigger(trigger);
        }

        // Add data providers
        for (final DataProvider<DPO> dataProvider : registeredDataProviders) {
            validator.addDataProvider(dataProvider);
        }

        // TODO Map data providers output to rules input
        validator.mapDataProvidersToRules(dataProviderToRuleMapping);

        // Add rules
        for (final Rule<RI, RO> rule : registeredRules) {
            validator.addRule(rule);
        }

        // Map rules output to result handlers input
        validator.mapRulesToResultHandlers(ruleToResultHandlerMapping, rulesOutputToResultHandlersInputTransformer);

        // Add result handlers
        for (final ResultHandler<RHI> resultHandler : registeredResultHandlers) {
            validator.addResultHandler(resultHandler);
        }

        return validator;
    }
}
