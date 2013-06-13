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
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.trigger.Trigger;
import com.google.code.validationframework.base.validator.GeneralValidator;

import java.util.Collection;
import java.util.List;

public class CombinedDataProvidersContext<DPO> {

    private final List<Trigger> registeredTriggers;
    private final List<DataProvider<DPO>> registeredDataProviders;
    private final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping;

    public CombinedDataProvidersContext(final List<Trigger> registeredTriggers, final List<DataProvider<DPO>>
            registeredDataProviders, final GeneralValidator.DataProviderToRuleMapping dataProviderToRuleMapping) {
        this.registeredTriggers = registeredTriggers;
        this.registeredDataProviders = registeredDataProviders;
        this.dataProviderToRuleMapping = dataProviderToRuleMapping;
    }

    public <RO> RuleContext<DPO, Collection<DPO>, RO> check(final Rule<Collection<DPO>, RO> rule) {
        return new RuleContext<DPO, Collection<DPO>, RO>(registeredTriggers, registeredDataProviders, dataProviderToRuleMapping, rule);
    }
}
