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

package com.github.validationframework.experimental.format;

import com.github.validationframework.api.rule.Rule;
import com.github.validationframework.base.transform.Aggregator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RuleBasedFormat<E, O> extends Format {

    /**
     * Generated serial UID.
     */
    private static final long serialVersionUID = 3259788532170669392L;

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RuleBasedFormat.class);

    private Format wrappedFormat = null;

    private final List<Rule<String, E>> preRules = new ArrayList<Rule<String, E>>();

    private Aggregator<E, Boolean> preResultAggregator = null;

    private final List<Rule<Object, O>> postRules = new ArrayList<Rule<Object, O>>();

    private Aggregator<O, Boolean> postResultAggregator = null;

    public RuleBasedFormat() {
        super();
    }

    public RuleBasedFormat(final Format wrappedFormat) {
        super();
        setWrappedFormat(wrappedFormat);
    }

    public RuleBasedFormat(final Aggregator<E, Boolean> preResultAggregator, final Format wrappedFormat,
                           final Aggregator<O, Boolean> postResultAggregator) {
        super();
        setWrappedFormat(wrappedFormat);
        setPreResultAggregator(preResultAggregator);
        setPostResultAggregator(postResultAggregator);
    }

    public Format getWrappedFormat() {
        return wrappedFormat;
    }

    public void setWrappedFormat(final Format wrappedFormat) {
        this.wrappedFormat = wrappedFormat;
    }

    public void addPreRule(final Rule<String, E> preRule) {
        preRules.add(preRule);
    }

    public void removePreRule(final Rule<String, E> preRule) {
        preRules.remove(preRule);
    }

    public Aggregator<E, Boolean> getPreResultAggregator() {
        return preResultAggregator;
    }

    public void setPreResultAggregator(final Aggregator<E, Boolean> preResultAggregator) {
        this.preResultAggregator = preResultAggregator;
    }

    public void addPostRule(final Rule<Object, O> postRule) {
        postRules.add(postRule);
    }

    public void removePostRule(final Rule<Object, O> postRule) {
        postRules.remove(postRule);
    }

    public Aggregator<O, Boolean> getPostResultAggregator() {
        return postResultAggregator;
    }

    public void setPostResultAggregator(final Aggregator<O, Boolean> postResultAggregator) {
        this.postResultAggregator = postResultAggregator;
    }

    /**
     * @see Format#format(Object)
     */
    @Override
    public Object parseObject(final String source) throws ParseException {
        checkPreRules(source);
        final Object object = super.parseObject(source);
        checkPostRules(object);

        return object;
    }

    private void checkPreRules(final String input) throws ParseException {
        // Check pre-rules one by one
        final Collection<E> preResults = new ArrayList<E>();
        for (final Rule<String, E> rule : preRules) {
            preResults.add(rule.validate(input));
        }

        // Aggregate pre-rules results
        if (preResultAggregator == null) {
            LOGGER.warn("No result aggregator set for the pre-rules");
        } else {
            final Boolean preAggregatedResult = preResultAggregator.transform(preResults);
            if (Boolean.FALSE.equals(preAggregatedResult)) {
                throw new ParseException("", -1);
            }
        }
    }

    private void checkPostRules(final Object input) throws ParseException {
        // Check post-rules one by one
        final Collection<O> postResults = new ArrayList<O>();
        for (final Rule<Object, O> rule : postRules) {
            postResults.add(rule.validate(input));
        }

        // Aggregate post-rules results
        if (postResultAggregator == null) {
            LOGGER.warn("No result aggregator set for the post-rules");
        } else {
            final Boolean postAggregatedResult = postResultAggregator.transform(postResults);
            if (Boolean.FALSE.equals(postAggregatedResult)) {
                throw new ParseException("", -1);
            }
        }
    }

    /**
     * @see Format#format(Object, StringBuffer, FieldPosition)
     */
    @Override
    public StringBuffer format(final Object obj, final StringBuffer toAppendTo, final FieldPosition pos) {
        // Just forward the call to the wrapped format
        return wrappedFormat.format(obj, toAppendTo, pos);
    }

    /**
     * @see Format#parseObject(String, ParsePosition)
     */
    @Override
    public Object parseObject(final String source, final ParsePosition pos) {
        // Just forward the call to the wrapped format
        return wrappedFormat.parseObject(source, pos);
    }
}
