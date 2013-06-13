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

package com.google.code.validationframework.base.validator;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GeneralValidator<DPO, RI, RO, RHI> extends AbstractSimpleValidator<Trigger, DataProvider<DPO>, DPO,
        Rule<RI, RO>, RI, RO, ResultHandler<RHI>, RHI> {

    public enum DataProviderToRuleMapping {
        EACH_TO_EACH,
        ALL_TO_EACH
    }

    public enum RuleToResultHandlerMapping {
        EACH_TO_EACH,
        ALL_TO_EACH
    }

    private final Logger LOGGER = LoggerFactory.getLogger(GeneralValidator.class);

    private DataProviderToRuleMapping dataProviderToRuleMapping = DataProviderToRuleMapping.ALL_TO_EACH;

    private Transformer<Object, RI> dataProviderOutputToRuleInputTransformer = new CastTransformer<Object, RI>();

    private RuleToResultHandlerMapping ruleToResultHandlerMapping = RuleToResultHandlerMapping.ALL_TO_EACH;

    private Transformer<Object, RHI> ruleOutputToResultHandlerInputTransformer = new CastTransformer<Object, RHI>();

    public GeneralValidator() {
        this(DataProviderToRuleMapping.ALL_TO_EACH, RuleToResultHandlerMapping.ALL_TO_EACH);
    }

    public GeneralValidator(final DataProviderToRuleMapping dataProviderToRuleMapping) {
        this(dataProviderToRuleMapping, RuleToResultHandlerMapping.ALL_TO_EACH);
    }

    public GeneralValidator(final RuleToResultHandlerMapping ruleToResultHandlerMapping) {
        this(DataProviderToRuleMapping.ALL_TO_EACH, ruleToResultHandlerMapping);
    }

    public GeneralValidator(final DataProviderToRuleMapping dataProviderToRuleMapping,
                            final RuleToResultHandlerMapping ruleToResultHandlerMapping) {
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
    }

    public void mapDataProvidersToRules(final DataProviderToRuleMapping dataProviderToRuleMapping) {
        mapDataProvidersToRules(dataProviderToRuleMapping, null);
    }

    public void mapDataProvidersToRules(final DataProviderToRuleMapping dataProviderToRuleMapping,
                                        final Transformer<Object, RI> dataProviderOutputToRuleInputTransformer) {
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.dataProviderOutputToRuleInputTransformer = dataProviderOutputToRuleInputTransformer;
        if (this.dataProviderOutputToRuleInputTransformer == null) {
            this.dataProviderOutputToRuleInputTransformer = new CastTransformer<Object, RI>();
        }
    }

    public void mapRulesToResultHandlers(final RuleToResultHandlerMapping ruleToResultHandlerMapping) {
        mapRulesToResultHandlers(ruleToResultHandlerMapping, null);
    }

    public void mapRulesToResultHandlers(final RuleToResultHandlerMapping ruleToResultHandlerMapping,
                                         final Transformer<Object, RHI> ruleOutputToResultHandlerInputTransformer) {
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
        this.ruleOutputToResultHandlerInputTransformer = ruleOutputToResultHandlerInputTransformer;
        if (this.ruleOutputToResultHandlerInputTransformer == null) {
            this.ruleOutputToResultHandlerInputTransformer = new CastTransformer<Object, RHI>();
        }
    }

    @Override
    protected void processTrigger(final Trigger trigger) {
        switch (dataProviderToRuleMapping) {
            case EACH_TO_EACH:
                for (final DataProvider<DPO> dataProvider : dataProviders) {
                    final RI ruleInput = dataProviderOutputToRuleInputTransformer.transform(dataProvider.getData());
                    processRules(ruleInput);
                }
                break;

            case ALL_TO_EACH:
                final List<DPO> dataProvidersOutput = new ArrayList<DPO>(dataProviders.size());
                for (final DataProvider<DPO> dataProvider : dataProviders) {
                    dataProvidersOutput.add(dataProvider.getData());
                }
                final RI ruleInput = dataProviderOutputToRuleInputTransformer.transform(dataProvidersOutput);
                processRules(ruleInput);
                break;

            default:
                LOGGER.error("Unsupported " + DataProviderToRuleMapping.class.getSimpleName() + ": " +
                        dataProviderToRuleMapping);
        }
    }

    private void processRules(final RI ruleInput) {
        switch (ruleToResultHandlerMapping) {
            case EACH_TO_EACH:
                for (final Rule<RI, RO> rule : rules) {
                    final RO ruleOutput = rule.validate(ruleInput);
                    final RHI resultHandlerInput = ruleOutputToResultHandlerInputTransformer.transform(ruleOutput);
                    processResultHandlers(resultHandlerInput);
                }
                break;

            case ALL_TO_EACH:
                final List<RO> rulesOutput = new ArrayList<RO>(rules.size());
                for (final Rule<RI, RO> rule : rules) {
                    rulesOutput.add(rule.validate(ruleInput));
                }
                final RHI resultHandlerInput = ruleOutputToResultHandlerInputTransformer.transform(rulesOutput);
                processResultHandlers(resultHandlerInput);
                break;

            default:
                LOGGER.error("Unsupported " + RuleToResultHandlerMapping.class.getSimpleName() + ": " +
                        ruleToResultHandlerMapping);
        }
    }

    private void processResultHandlers(final RHI resultHandlerInput) {
        for (final ResultHandler<RHI> resultHandler : resultHandlers) {
            resultHandler.handleResult(resultHandlerInput);
        }
    }
}
