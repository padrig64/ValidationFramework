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

/**
 * Simple validator allowing to have different mapping between data providers, rules and result handlers.<br>The
 * validator allows to transform the data providers' output before mapping, the rules' input after mapping, the rules'
 * output before mapping and the result handlers' input after mapping.<br>This means that the output of each data
 * provider will be transformed using a series of data provider output {@link Transformer}s. Then, if the data provider
 * to rule mapping is {@link DataProviderToRuleMapping#EACH_TO_EACH}, the output of each data provider output
 * transformation will transformed again using a series of rule input {@link Transformer}s, before being passed to each
 * rule. If the data provider to rule mapping is {@link DataProviderToRuleMapping#ALL_TO_EACH}, the output of all data
 * provider output transformations will be put in a collection and transformed again using the series of rule input
 * {@link Transformer}s, before being passed to each rule.<br>The output of each rule will be transformed using a series
 * of rule output {@link Transformer}s. Then, if the rule to result handler mapping is {@link
 * RuleToResultHandlerMapping#EACH_TO_EACH}, the output of each rule output transformation will be transformed again
 * using a series of result handler input {@link Transformer}s, before being passed to each result handler.<br>If the
 * rule to result handler mapping is {@link RuleToResultHandlerMapping#ALL_TO_EACH}, the output of all rule output
 * transformations will be put in a collection and transformed as a whole using the series of result handler input
 * {@link Transformer}s, before being passed to each result handler.<br>The general validation flow can be
 * represented by the following pattern: triggers -> data providers -> data provider output transformers -> data
 * provider to rule mapping -> rule input transformers -> rules -> rule output transformers -> rule output to result
 * handler input transformers -> result handler input transformers -> result handlers.<br>Note that the use of
 * transformers is optional. By default, the data provider to rule mapping is set to {@link
 * DataProviderToRuleMapping#EACH_TO_EACH} and the rule to result handler mapping is set to {@link
 * RuleToResultHandlerMapping#EACH_TO_EACH}.<br>For type safety, it is highly advised to use the {@link
 * GeneralValidatorBuilder}.
 *
 * @param <DPO> Type of data provider output.<br>This may or may not be the same type as the rule input.
 * @param <RI>  Type of rule input.<br>This may or may not be the same type as the data provider output.
 * @param <RO>  Type of rule output.<br>This may or may not be the same type as the result handler input.
 * @param <RHI> Type of result handler input.<br>This may or may not be the same type as the rule output.
 *
 * @see GeneralValidatorBuilder
 * @see AbstractSimpleValidator
 */
public class GeneralValidator<DPO, RI, RO, RHI> extends AbstractSimpleValidator<Trigger, DataProvider<DPO>, DPO,
        Rule<RI, RO>, RI, RO, ResultHandler<RHI>, RHI> {

    /**
     * Mapping between data providers and rules.
     *
     * @see #setDataProviderOutputTransformers(Transformer[])
     * @see #setDataProviderOutputTransformers(Collection)
     * @see #setDataProviderToRuleMapping(DataProviderToRuleMapping)
     * @see #setRuleInputTransformers(Transformer[])
     * @see #setRuleInputTransformers(Collection)
     */
    public enum DataProviderToRuleMapping {

        /**
         * The data from each data provider will be passed one by one to each rule.<br>For this mapping, if no data
         * provider output transformer is used, it is expected that the rule input is of the same as the type as the
         * data provider output.<br>For type safety, it is highly advised to use the {@link GeneralValidatorBuilder}.
         *
         * @see GeneralValidatorBuilder
         */
        EACH_TO_EACH,

        /**
         * The data from all data providers will passed all at once (in a {@link Collection}) to each rule.<br>For this
         * mapping, if no data provider transformer is used, it is expected that the rule input is a {@link Collection}
         * containing objects of the same type as the data provider output.<br>For type safety, it is highly advised to
         * use the {@link GeneralValidatorBuilder}.
         *
         * @see GeneralValidatorBuilder
         */
        ALL_TO_EACH
    }

    /**
     * Mapping between rules and result handlers.
     *
     * @see #setRuleOutputTransformers(Transformer[])
     * @see #setRuleOutputTransformers(Collection)
     * @see #setRuleToResultHandlerMapping(RuleToResultHandlerMapping)
     * @see #setResultHandlerInputTransformers(Transformer[])
     * @see #setResultHandlerInputTransformers(Collection)
     */
    public enum RuleToResultHandlerMapping {

        /**
         * The result from each rule will be passed one by one to each result handler.<br>For this mapping, if no rule
         * output transformer is used, it is expected that the result handler input is of the same type as the rule
         * output.<br>For type safety, it is highly advised to use the {@link GeneralValidatorBuilder}.
         *
         * @see GeneralValidatorBuilder
         */
        EACH_TO_EACH,

        /**
         * The result from all rules will be passed all at once (in a {@link Collection}) to each result handler.<br>For
         * this mapping, if no rule output transformer is used, it is expected that the result handler input is a {@link
         * Collection} containing objects of the same type as the rule output.<br>For type safety, it is highly advised
         * to use the {@link GeneralValidatorBuilder}.
         *
         * @see GeneralValidatorBuilder
         */
        ALL_TO_EACH
    }

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneralValidator.class);

    /**
     * List of {@link Transformer}s transforming the output of each data provider.<br>The type of input of the first
     * transformer shall match the type of output of the data providers. The type of the input of a subsequent
     * transformer shall match the type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> dataProviderOutputTransformers = new ArrayList<Transformer>();

    /**
     * Mapping between the (possibly transformed) data provider output to the rule input.
     *
     * @see DataProviderToRuleMapping
     */
    private DataProviderToRuleMapping dataProviderToRuleMapping = DataProviderToRuleMapping.EACH_TO_EACH;

    /**
     * List of {@link Transformer}s transforming the input of each rule.<br>The type of input of the first transformer
     * shall match the type of output of the data provider to rule mapping: so either the type of data provider output
     * or a {@link Collection} of objects of the same type as the data provider output. The type of the input of a
     * subsequent transformer shall match the type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> ruleInputTransformers = new ArrayList<Transformer>();

    /**
     * List of {@link Transformer}s transforming the output of each rule.<br>The type of input of the first transformer
     * shall match the type of output of the rules. The type of the input of a subsequent transformer shall match the
     * type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> ruleOutputTransformers = new ArrayList<Transformer>();

    /**
     * Mapping between the (possibly transformed) rule output to the result handler input.
     */
    private RuleToResultHandlerMapping ruleToResultHandlerMapping = RuleToResultHandlerMapping.EACH_TO_EACH;

    /**
     * List of {@link Transformer}s transforming the input of each result handler.<br>The type of input of the first
     * transformer shall match the type of rule to result handler mapping: so either the type of rule output or a {@link
     * Collection} of objects of the same type as the rule output. The type of the input of a subsequent transformer
     * shall match the type of output of the previous transformer.
     *
     * @see #dataProviderToRuleMapping
     */
    private List<Transformer> resultHandlerInputTransformers = new ArrayList<Transformer>();

    /**
     * Adds the specified result collector to the triggers and data providers.
     *
     * @param resultCollector Result collector to be added.
     */
    public void addResultCollector(final ResultCollector<?, DPO> resultCollector) {
        if (resultCollector != null) {
            addTrigger(resultCollector);
            addDataProvider(resultCollector);
        }
    }

    /**
     * Removes the specified result collector from the triggers and data providers.
     *
     * @param resultCollector Result collector to be removed.
     */
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

    public DataProviderToRuleMapping getDataProviderToRuleMapping() {
        return dataProviderToRuleMapping;
    }

    public void setDataProviderToRuleMapping(final DataProviderToRuleMapping dataProviderToRuleMapping) {
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

    public RuleToResultHandlerMapping getRuleToResultHandlerMapping() {
        return ruleToResultHandlerMapping;
    }

    public void setRuleToResultHandlerMapping(final RuleToResultHandlerMapping ruleToResultHandlerMapping) {
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

    /**
     * Processes the output of each data provider one by one with each rule.
     */
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

    /**
     * Processes the output of all data providers, all at once, with each rule.
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Processes the specified rule input.
     *
     * @param ruleInput Rule input to be validated.
     */
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

    /**
     * Processes the specified rule input with each rule, and processes the results of each rule one by one with each
     * result handler.
     *
     * @param ruleInput Rule input to be validated.
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Processes the specified rule input with each rule, and processes the result of all rules, all at once, with each
     * result handler.
     *
     * @param ruleInput Rule input to be validated.
     */
    @SuppressWarnings("unchecked")
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

    /**
     * Processes the specified result handler input with each result handler.
     *
     * @param resultHandlerInput Result handler input to be handled.
     */
    private void processResultHandlers(final RHI resultHandlerInput) {
        for (final ResultHandler<RHI> resultHandler : resultHandlers) {
            resultHandler.handleResult(resultHandlerInput);
        }
    }
}
