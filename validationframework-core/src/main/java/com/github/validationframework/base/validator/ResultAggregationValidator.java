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

package com.github.validationframework.base.validator;

import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.base.transform.Aggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Concrete default implementation of a simple validator.<br>A simple validator has data providers and rules that are
 * bound to a known specific type of data, and result handlers that are bound to a known specific type of
 * result.<br>When any of its triggers is initiated, the simple validator will read all the data from all of its data
 * providers, check them all against all of its rules, and handles all the results using all of its result handlers.
 *
 * @param <RI> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 *             type of the component itself.
 * @param <RO> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
 *
 * @see AbstractSimpleValidator
 * @see Trigger
 * @see DataProvider
 * @see Rule
 * @see ResultHandler
 */
public class ResultAggregationValidator<RI, RO, HI> extends AbstractSimpleValidator<Trigger, DataProvider<RI>, RI,
        Rule<RI, RO>, RI, RO, ResultHandler<HI>, HI> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultAggregationValidator.class);

    private Aggregator<RO, HI> resultAggregator = null;

    public ResultAggregationValidator(final Aggregator<RO, HI> resultAggregator) {
        this.resultAggregator = resultAggregator;
    }

    /**
     * @see AbstractSimpleValidator#processTrigger(Trigger)
     */
    @Override
    protected void processTrigger(final Trigger trigger) {
        if (dataProviders.isEmpty()) {
            LOGGER.warn("No data providers in validator: " + this);
        } else {
            // Process data from all providers
            for (final DataProvider<RI> dataProvider : dataProviders) {
                processData(dataProvider.getData());
            }
        }
    }

    /**
     * Validates the specified data all rules.
     *
     * @param data Data to be validated against all rules.
     */
    protected void processData(final RI data) {
        // Check data against all rules
        final Collection<RO> results = new ArrayList<RO>(resultHandlers.size());
        for (final Rule<RI, RO> rule : rules) {
            results.add(rule.validate(data));
        }

        // Aggregate all results and process the output
        final HI aggregatedResult = resultAggregator.transform(results);
        processResult(aggregatedResult);
    }

    /**
     * Handles the specified aggregated result using all result handlers.
     *
     * @param aggregatedResult Aggregated result to be processed by all result handlers.
     */
    protected void processResult(final HI aggregatedResult) {
        // Process the result with all result handlers
        for (final ResultHandler<HI> resultHandler : resultHandlers) {
            resultHandler.handleResult(aggregatedResult);
        }
    }
}
