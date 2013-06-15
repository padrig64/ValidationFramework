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
import java.util.Collections;
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

    private List<Transformer> dataProvidersOutputTransformers = new ArrayList<Transformer>();

    private DataProviderToRuleMapping dataProviderToRuleMapping = DataProviderToRuleMapping.ALL_TO_EACH;

    private List<Transformer> combinedDataProvidersOutputTransformers = new ArrayList<Transformer>();

    private List<Transformer> rulesOutputTransformers = new ArrayList<Transformer>();

    private RuleToResultHandlerMapping ruleToResultHandlerMapping = RuleToResultHandlerMapping.ALL_TO_EACH;

    private List<Transformer> combinedRulesOutputTransformers = new ArrayList<Transformer>();

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

    public void mapDataProvidersToRules(final DataProviderToRuleMapping dataProviderToRuleMapping,
                                        final Transformer... dataProvidersOutputToRulesInputTransformers) {
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
        this.dataProvidersOutputTransformers.clear();
        if (dataProvidersOutputToRulesInputTransformers == null) {
            this.dataProvidersOutputTransformers.add(new CastTransformer<Object, RI>());
        } else {
            Collections.addAll(this.dataProvidersOutputTransformers, dataProvidersOutputToRulesInputTransformers);
        }
    }

    public void mapRulesToResultHandlers(final RuleToResultHandlerMapping ruleToResultHandlerMapping,
                                         final Transformer... rulesOutputToResultHandlerInputTransformers) {
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
        this.rulesOutputTransformers.clear();
        if (rulesOutputToResultHandlerInputTransformers == null) {
            this.dataProvidersOutputTransformers.add(new CastTransformer<Object, RI>());
        } else {
            Collections.addAll(this.rulesOutputTransformers, rulesOutputToResultHandlerInputTransformers);
        }
    }

    @Override
    protected void processTrigger(final Trigger trigger) {
        switch (dataProviderToRuleMapping) {
            case EACH_TO_EACH:
                for (final DataProvider<DPO> dataProvider : dataProviders) {
                    Object dataProviderOutput = dataProvider.getData();
                    if (dataProvidersOutputTransformers != null) {
                        for (final Transformer transformer : dataProvidersOutputTransformers) {
                            dataProviderOutput = transformer.transform(dataProviderOutput);
                        }
                    }
                    if (combinedDataProvidersOutputTransformers != null) {
                        for (final Transformer transformer : combinedDataProvidersOutputTransformers) {
                            dataProviderOutput = transformer.transform(dataProviderOutput);
                        }
                    }

                    final RI ruleInput = (RI) dataProviderOutput;
                    processRules(ruleInput);
                }
                break;

            case ALL_TO_EACH:
                final List<Object> combinedDataProvidersOutput = new ArrayList<Object>(dataProviders.size());
                for (final DataProvider<DPO> dataProvider : dataProviders) {
                    Object data = dataProvider.getData();
                    if (dataProvidersOutputTransformers != null) {
                        for (final Transformer transformer : dataProvidersOutputTransformers) {
                            data = transformer.transform(data);
                        }
                    }
                    combinedDataProvidersOutput.add(data);
                }
                Object dataProvidersOutput = combinedDataProvidersOutput;
                if (combinedDataProvidersOutputTransformers != null) {
                    for (final Transformer transformer : combinedDataProvidersOutputTransformers) {
                        dataProvidersOutput = transformer.transform(dataProvidersOutput);
                    }
                }

                final RI ruleInput = (RI) dataProvidersOutput;
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
                    Object ruleOutput = rule.validate(ruleInput);
                    if (rulesOutputTransformers != null) {
                        for (final Transformer transformer : rulesOutputTransformers) {
                            ruleOutput = transformer.transform(ruleOutput);
                        }
                    }
                    if (combinedRulesOutputTransformers != null) {
                        for (final Transformer transformer : combinedRulesOutputTransformers) {
                            ruleOutput = transformer.transform(ruleOutput);
                        }
                    }

                    final RHI resultHandlerInput = (RHI) ruleOutput;
                    processResultHandlers(resultHandlerInput);
                }
                break;

            case ALL_TO_EACH:
                final List<Object> combinedRulesOutput = new ArrayList<Object>(rules.size());
                for (final DataProvider<DPO> dataProvider : dataProviders) {
                    Object data = dataProvider.getData();
                    if (dataProvidersOutputTransformers != null) {
                        for (final Transformer transformer : dataProvidersOutputTransformers) {
                            data = transformer.transform(data);
                        }
                    }
                    combinedRulesOutput.add(data);
                }
                Object ruleOutput = combinedRulesOutput;
                if (combinedDataProvidersOutputTransformers != null) {
                    for (final Transformer transformer : combinedDataProvidersOutputTransformers) {
                        ruleOutput = transformer.transform(ruleOutput);
                    }
                }

                final RHI resultHandlerInput = (RHI) ruleOutput;
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
