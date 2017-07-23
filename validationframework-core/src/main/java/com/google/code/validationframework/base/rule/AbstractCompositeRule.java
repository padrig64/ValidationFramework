/*
 * Copyright (c) 2017, ValidationFramework Authors
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

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.rule.Rule;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstraction of a composite rule composed of sub-rules.
 *
 * @param <RI> Type of data to be validated.<br>
 *             It can be, for instance, the type of data handled by a component, or the type of the component itself.
 * @param <RO> Type of validation result.<br>
 *             It can be, for instance, an enumeration or just a boolean.
 *
 * @see Rule
 * @see Disposable
 */
public abstract class AbstractCompositeRule<RI, RO> implements Rule<RI, RO>, Disposable {

    /**
     * Sub-rules to be checked.
     */
    protected final List<Rule<RI, RO>> rules = new ArrayList<Rule<RI, RO>>();

    /**
     * Default constructor.
     */
    public AbstractCompositeRule() {
        // Nothing to be done
    }

    /**
     * Constructor specifying the sub-rule(s) to be added.
     *
     * @param rules Sub-rule(s) to be added.
     *
     * @see #addRule(Rule)
     */
    public AbstractCompositeRule(Rule<RI, RO>... rules) {
        if (rules != null) {
            for (Rule<RI, RO> rule : rules) {
                addRule(rule);
            }
        }
    }

    /**
     * Adds the specified sub-rule to be checked.
     *
     * @param rule Sub-rule to be added.
     */
    public void addRule(Rule<RI, RO> rule) {
        rules.add(rule);
    }

    /**
     * Removes the specified sub-rule to be checked.
     *
     * @param rule Sub-rule tobe removed
     */
    public void removeRule(Rule<RI, RO> rule) {
        rules.remove(rule);
    }

    /**
     * @see Disposable#dispose()
     */
    @Override
    public void dispose() {
        for (Rule<RI, RO> rule : rules) {
            if (rule instanceof Disposable) {
                ((Disposable) rule).dispose();
            }
        }
    }
}
