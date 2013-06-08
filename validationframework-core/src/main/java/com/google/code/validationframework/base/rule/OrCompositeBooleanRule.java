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

package com.google.code.validationframework.base.rule;

import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.base.transform.OrBooleanAggregator;
import com.google.code.validationframework.base.transform.Transformer;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Composite rule checking data of a known specific type using sub-rules, and returning a boolean as an aggregation of
 * the boolean results from its sub-rules.
 *
 * @param <RI> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
 *             type of the component itself.
 *
 * @see AbstractCompositeRule
 * @see AndCompositeBooleanRule
 */
public class OrCompositeBooleanRule<RI> extends AbstractCompositeRule<RI, Boolean> {

    /**
     * Boolean aggregator using the OR operator.
     *
     * @see com.google.code.validationframework.base.transform.OrBooleanAggregator
     */
    private final Transformer<Collection<Boolean>, Boolean> aggregator;

    /**
     * @see AbstractCompositeRule#AbstractCompositeRule()
     */
    public OrCompositeBooleanRule() {
        super();
        aggregator = new OrBooleanAggregator();
    }

    /**
     * @see AbstractCompositeRule#AbstractCompositeRule(Rule[])
     */
    public OrCompositeBooleanRule(final Rule<RI, Boolean>... rules) {
        super(rules);
        aggregator = new OrBooleanAggregator();
    }

    /**
     * @see AbstractCompositeRule#validate(Object)
     */
    @Override
    public Boolean validate(final RI data) {
        // Collect results
        final Collection<Boolean> results = new ArrayList<Boolean>();
        for (final Rule<RI, Boolean> rule : rules) {
            final Boolean result = rule.validate(data);
            results.add(result);
        }

        // Aggregate results
        return aggregator.transform(results);
    }
}
