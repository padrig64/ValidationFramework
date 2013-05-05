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

package com.github.validationframework.experimental.builder.context.simplevalidator;

import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.api.trigger.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Context to add more triggers and the first data providers.
 */
public class DataProviderContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataProviderContext.class);

    private static final String NEW_INSTANCE_ERROR_MSG = "Failed creating instance of class: ";

    final List<Trigger> registeredTriggers;

    public DataProviderContext(final List<Trigger> registeredTriggers) {
        this.registeredTriggers = registeredTriggers;
    }

    public DataProviderContext on(final Class<? extends Trigger> triggerClass) {
        Trigger trigger = null;
        try {
            trigger = triggerClass.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error(NEW_INSTANCE_ERROR_MSG + triggerClass, e);
        } catch (IllegalAccessException e) {
            LOGGER.error(NEW_INSTANCE_ERROR_MSG + triggerClass, e);
        }
        return on(trigger);
    }

    public DataProviderContext on(final Trigger trigger) {
        if (trigger != null) {
            registeredTriggers.add(trigger);
        }
        return this;
    }

    /**
     * Adds more triggers to the validator.
     *
     * @param triggers Triggers to be added.
     *
     * @return Same data provider context.
     */
    public DataProviderContext on(final Trigger... triggers) {
        if (triggers != null) {
            Collections.addAll(registeredTriggers, triggers);
        }
        return this;
    }

    public DataProviderContext on(final Collection<Trigger> triggers) {
        if (triggers != null) {
            registeredTriggers.addAll(triggers);
        }
        return this;
    }

    public <D> RuleContext<D> read(final Class<? extends DataProvider<D>> dataProviderClass) {
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

    public <D> RuleContext<D> read(final DataProvider<D> dataProvider) {
        final List<DataProvider<D>> registeredDataProviders = new ArrayList<DataProvider<D>>();
        if (dataProvider != null) {
            registeredDataProviders.add(dataProvider);
        }
        return new RuleContext<D>(registeredTriggers, registeredDataProviders);
    }

    /**
     * Adds the first data providers to the validator.
     *
     * @param dataProviders Data providers to be added.
     * @param <D>           Type of data to be validated.<br>It can be, for instance,
     *                      the type of data handled by a component, or the
     *                      type of the component itself.
     *
     * @return Rule context allowing to add data providers and rules, but not triggers.
     */
    public <D> RuleContext<D> read(final DataProvider<D>... dataProviders) {
        final List<DataProvider<D>> registeredDataProviders = new ArrayList<DataProvider<D>>();
        if (dataProviders != null) {
            Collections.addAll(registeredDataProviders, dataProviders);
        }
        return new RuleContext<D>(registeredTriggers, registeredDataProviders);
    }

    public <D> RuleContext<D> read(final Collection<DataProvider<D>> dataProviders) {
        final List<DataProvider<D>> registeredDataProviders = new ArrayList<DataProvider<D>>();
        if (dataProviders != null) {
            registeredDataProviders.addAll(dataProviders);
        }
        return new RuleContext<D>(registeredTriggers, registeredDataProviders);
    }
}
