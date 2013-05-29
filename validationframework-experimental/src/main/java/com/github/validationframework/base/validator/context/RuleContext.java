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

package com.github.validationframework.base.validator.context;

import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.api.trigger.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Context to add more data providers and the first rules.
 *
 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 *            type of the component itself.
 */
public class RuleContext<D> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleContext.class);

    private static final String NEW_INSTANCE_ERROR_MSG = "Failed creating instance of class: ";

    final List<Trigger> registeredTriggers;
    final List<DataProvider<D>> registeredDataProviders;

    public RuleContext(final List<Trigger> registeredTriggers, final List<DataProvider<D>> registeredDataProviders) {
        this.registeredTriggers = registeredTriggers;
        this.registeredDataProviders = registeredDataProviders;
    }

    public RuleContext<D> read(final Class<? extends DataProvider<D>> dataProviderClass) {
        DataProvider<D> dataProvider = null;
        try {
            dataProvider = dataProviderClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(NEW_INSTANCE_ERROR_MSG + dataProviderClass, e);
        } catch (IllegalAccessException e) {
            LOGGER.error(NEW_INSTANCE_ERROR_MSG + dataProviderClass, e);
        }
        return read(dataProvider);
    }

    public RuleContext<D> read(final DataProvider<D> dataProvider) {
        if (dataProvider != null) {
            registeredDataProviders.add(dataProvider);
        }
        return this;
    }

    /**
     * Adds more data providers to the validator.
     *
     * @param dataProviders Data providers to be added.
     *
     * @return Same rule context.
     */
    public RuleContext<D> read(final DataProvider<D>... dataProviders) {
        if (dataProviders != null) {
            Collections.addAll(registeredDataProviders, dataProviders);
        }
        return this;
    }

    public RuleContext<D> read(final Collection<DataProvider<D>> dataProviders) {
        if (dataProviders != null) {
            registeredDataProviders.addAll(dataProviders);
        }
        return this;
    }

    public <O> ResultHandlerContext<D, O> check(final Class<? extends Rule<D, O>> ruleClass) {
        Rule<D, O> rule = null;
        try {
            rule = ruleClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(NEW_INSTANCE_ERROR_MSG + ruleClass, e);
        } catch (IllegalAccessException e) {
            LOGGER.error(NEW_INSTANCE_ERROR_MSG + ruleClass, e);
        }
        return check(rule);
    }

    public <O> ResultHandlerContext<D, O> check(final Rule<D, O> rule) {
        final List<Rule<D, O>> registeredRules = new ArrayList<Rule<D, O>>();
        if (rule != null) {
            registeredRules.add(rule);
        }
        return new ResultHandlerContext<D, O>(registeredTriggers, registeredDataProviders, registeredRules);
    }

    /**
     * Adds the first rules to the validator.
     *
     * @param rules Rules to be added.
     * @param <O>   Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
     *
     * @return Result handler context allowing to add rules and result handlers, but not data providers.
     */
    public <O> ResultHandlerContext<D, O> check(final Rule<D, O>... rules) {
        final List<Rule<D, O>> registeredRules = new ArrayList<Rule<D, O>>();
        if (rules != null) {
            Collections.addAll(registeredRules, rules);
        }
        return new ResultHandlerContext<D, O>(registeredTriggers, registeredDataProviders, registeredRules);
    }

    public <O> ResultHandlerContext<D, O> check(final Collection<Rule<D, O>> rules) {
        final List<Rule<D, O>> registeredRules = new ArrayList<Rule<D, O>>();
        if (rules != null) {
            registeredRules.addAll(rules);
        }
        return new ResultHandlerContext<D, O>(registeredTriggers, registeredDataProviders, registeredRules);
    }
}
