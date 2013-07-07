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
import com.google.code.validationframework.base.resulthandler.ResultCollector;
import com.google.code.validationframework.base.transform.Transformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
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

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralValidator.class);

    private List<Transformer> dataProviderOutputTransformers = new ArrayList<Transformer>();

    private DataProviderToRuleMapping dataProviderToRuleMapping = DataProviderToRuleMapping.ALL_TO_EACH;

    private List<Transformer> ruleInputTransformers = new ArrayList<Transformer>();

    private List<Transformer> ruleOutputTransformers = new ArrayList<Transformer>();

    private RuleToResultHandlerMapping ruleToResultHandlerMapping = RuleToResultHandlerMapping.ALL_TO_EACH;

    private List<Transformer> resultHandlerInputTransformers = new ArrayList<Transformer>();

    public void addResultCollector(final ResultCollector<?, DPO> resultCollector) {
        if (resultCollector != null) {
            addTrigger(resultCollector);
            addDataProvider(resultCollector);
        }
    }

    public void removeResultCollector(final ResultCollector<?, DPO> resultCollector) {
        if (resultCollector != null) {
            removeTrigger(resultCollector);
            removeDataProvider(resultCollector);
        }
    }

    public Transformer[] getDataProviderOutputTransformers() {
        final Transformer[] transformers;

        if (dataProviderOutputTransformers == null) {
            transformers = null;
        } else {
            transformers = dataProviderOutputTransformers.toArray(new Transformer[dataProviderOutputTransformers.size
                    ()]);
        }

        return transformers;
    }

    public void setDataProviderOutputTransformers(final Transformer... dataProviderOutputTransformers) {
        if (dataProviderOutputTransformers == null) {
            this.dataProviderOutputTransformers = null;
        } else {
            this.dataProviderOutputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.dataProviderOutputTransformers, dataProviderOutputTransformers);
        }
    }

    public void setDataProviderOutputTransformers(final Collection<Transformer> dataProviderOutputTransformers) {
        if (dataProviderOutputTransformers == null) {
            this.dataProviderOutputTransformers = null;
        } else {
            this.dataProviderOutputTransformers = new ArrayList<Transformer>();
            this.dataProviderOutputTransformers.addAll(dataProviderOutputTransformers);
        }
    }

    public void mapDataProvidersToRules(final DataProviderToRuleMapping dataProviderToRuleMapping) {
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
    }

    public Transformer[] getRuleInputTransformers() {
        final Transformer[] transformers;

        if (ruleInputTransformers == null) {
            transformers = null;
        } else {
            transformers = ruleInputTransformers.toArray(new Transformer[ruleInputTransformers.size()]);
        }

        return transformers;
    }

    public void setRuleInputTransformers(final Transformer... ruleInputTransformers) {
        if (ruleInputTransformers == null) {
            this.ruleInputTransformers = null;
        } else {
            this.ruleInputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.ruleInputTransformers, ruleInputTransformers);
        }
    }

    public void setRuleInputTransformers(final Collection<Transformer> ruleInputTransformers) {
        if (ruleInputTransformers == null) {
            this.ruleInputTransformers = null;
        } else {
            this.ruleInputTransformers = new ArrayList<Transformer>();
            this.ruleInputTransformers.addAll(ruleInputTransformers);
        }
    }

    public Transformer[] getRuleOutputTransformers() {
        final Transformer[] transformers;

        if (ruleOutputTransformers == null) {
            transformers = null;
        } else {
            transformers = ruleOutputTransformers.toArray(new Transformer[ruleOutputTransformers.size()]);
        }

        return transformers;
    }

    public void setRuleOutputTransformers(final Transformer... ruleOutputTransformers) {
        if (ruleOutputTransformers == null) {
            this.ruleOutputTransformers = null;
        } else {
            this.ruleOutputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.ruleOutputTransformers, ruleOutputTransformers);
        }
    }

    public void setRuleOutputTransformers(final Collection<Transformer> ruleOutputTransformers) {
        if (ruleOutputTransformers == null) {
            this.ruleOutputTransformers = null;
        } else {
            this.ruleOutputTransformers = new ArrayList<Transformer>();
            this.ruleOutputTransformers.addAll(ruleOutputTransformers);
        }
    }

    public void mapRulesToResultHandlers(final RuleToResultHandlerMapping ruleToResultHandlerMapping) {
        this.ruleToResultHandlerMapping = ruleToResultHandlerMapping;
    }

    public Transformer[] getResultHandlerInputTransformers() {
        final Transformer[] transformers;

        if (resultHandlerInputTransformers == null) {
            transformers = null;
        } else {
            transformers = resultHandlerInputTransformers.toArray(new Transformer[resultHandlerInputTransformers.size
                    ()]);
        }

        return transformers;
    }

    public void setResultHandlerInputTransformers(final Transformer... resultHandlerInputTransformers) {
        if (resultHandlerInputTransformers == null) {
            this.resultHandlerInputTransformers = null;
        } else {
            this.resultHandlerInputTransformers = new ArrayList<Transformer>();
            Collections.addAll(this.resultHandlerInputTransformers, resultHandlerInputTransformers);
        }
    }

    public void setResultHandlerInputTransformers(final Collection<Transformer> resultHandlerInputTransformers) {
        if (resultHandlerInputTransformers == null) {
            this.resultHandlerInputTransformers = null;
        } else {
            this.resultHandlerInputTransformers = new ArrayList<Transformer>();
            this.resultHandlerInputTransformers.addAll(resultHandlerInputTransformers);
        }
    }

    /**
     * Convenience method to trigger the validation without adding a
     * {@link com.google.code.validationframework.base.trigger.ManualTrigger}.
     */
    public void trigger() {
        processTrigger(null);
    }

    /**
     * @see AbstractSimpleValidator#processTrigger(Trigger)
     */
    @Override
    protected void processTrigger(final Trigger trigger) {
        switch (dataProviderToRuleMapping) {
            case EACH_TO_EACH:
                processEachDataProviderWithEachRule();
                break;
            case ALL_TO_EACH:
                processAllDataProvidersWithEachRule();
                break;
            default:
                LOGGER.error("Unsupported " + DataProviderToRuleMapping.class.getSimpleName() + ": " +
                        dataProviderToRuleMapping);
        }
    }

    @SuppressWarnings("unchecked")
    private void processEachDataProviderWithEachRule() {
        // For each data provider
        for (final DataProvider<DPO> dataProvider : dataProviders) {
            // Get the data provider output
            Object transformedOutput = dataProvider.getData();

            // Transform the data provider output
            if (dataProviderOutputTransformers != null) {
                for (final Transformer transformer : dataProviderOutputTransformers) {
                    transformedOutput = transformer.transform(transformedOutput);
                }
            }

            // Transform the transformed data provider output to rule input
            if (ruleInputTransformers != null) {
                for (final Transformer transformer : ruleInputTransformers) {
                    transformedOutput = transformer.transform(transformedOutput);
                }
            }
            final RI ruleInput = (RI) transformedOutput;

            // Process the rule input with the rules
            processRules(ruleInput);
        }
    }

    private void processAllDataProvidersWithEachRule() {
        // For each data provider
        final List<Object> transformedDataProvidersOutput = new ArrayList<Object>(dataProviders.size());
        for (final DataProvider<DPO> dataProvider : dataProviders) {
            // Get the data provider output
            Object transformedOutput = dataProvider.getData();

            // Transform the data provider output
            if (dataProviderOutputTransformers != null) {
                for (final Transformer transformer : dataProviderOutputTransformers) {
                    transformedOutput = transformer.transform(transformedOutput);
                }
            }

            // Put the transformed data provider output in a list
            transformedDataProvidersOutput.add(transformedOutput);
        }

        // Transform the list of transformed data provider output to rule input
        Object transformedRulesInput = transformedDataProvidersOutput;
        if (ruleInputTransformers != null) {
            for (final Transformer transformer : ruleInputTransformers) {
                transformedRulesInput = transformer.transform(transformedRulesInput);
            }
        }
        final RI ruleInput = (RI) transformedRulesInput;

        // Process the rule input with the rules
        processRules(ruleInput);
    }

    private void processRules(final RI ruleInput) {
        switch (ruleToResultHandlerMapping) {
            case EACH_TO_EACH:
                processEachRuleWithEachResultHandler(ruleInput);
                break;
            case ALL_TO_EACH:
                processAllRulesWithEachResultHandler(ruleInput);
                break;
            default:
                LOGGER.error("Unsupported " + RuleToResultHandlerMapping.class.getSimpleName() + ": " +
                        ruleToResultHandlerMapping);
        }
    }

    private void processEachRuleWithEachResultHandler(final RI ruleInput) {
        // For each rule
        for (final Rule<RI, RO> rule : rules) {
            // Validate the data and get the rule output
            Object ruleOutput = rule.validate(ruleInput);

            // Transform the rule output
            if (ruleOutputTransformers != null) {
                for (final Transformer transformer : ruleOutputTransformers) {
                    ruleOutput = transformer.transform(ruleOutput);
                }
            }

            // Transform the transformed rule output to result handler input
            if (resultHandlerInputTransformers != null) {
                for (final Transformer transformer : resultHandlerInputTransformers) {
                    ruleOutput = transformer.transform(ruleOutput);
                }
            }
            final RHI resultHandlerInput = (RHI) ruleOutput;

            // Process the result handler input with the result handlers
            processResultHandlers(resultHandlerInput);
        }
    }

    private void processAllRulesWithEachResultHandler(final RI ruleInput) {
        // For each rule
        final List<Object> combinedRulesOutput = new ArrayList<Object>(rules.size());
        for (final Rule<RI, RO> rule : rules) {
            // Validate the data and get the rule output
            Object data = rule.validate(ruleInput);

            // Transform the rule output
            if (ruleOutputTransformers != null) {
                for (final Transformer transformer : ruleOutputTransformers) {
                    data = transformer.transform(data);
                }
            }

            // Put the transformed rule output in a list
            combinedRulesOutput.add(data);
        }

        // Transform the list of transformed rule output to result handler input
        Object ruleOutput = combinedRulesOutput;
        if (resultHandlerInputTransformers != null) {
            for (final Transformer transformer : resultHandlerInputTransformers) {
                ruleOutput = transformer.transform(ruleOutput);
            }
        }
        final RHI resultHandlerInput = (RHI) ruleOutput;

        // Process the result handler input with the result handlers
        processResultHandlers(resultHandlerInput);
    }

    private void processResultHandlers(final RHI resultHandlerInput) {
        for (final ResultHandler<RHI> resultHandler : resultHandlers) {
            resultHandler.handleResult(resultHandlerInput);
        }
    }
}
