/*
 * Copyright (c) 2012, Patrick Moawad
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

package com.github.validationframework.validator;

import com.github.validationframework.feedback.FeedBack;
import com.github.validationframework.rule.Rule;
import com.github.validationframework.trigger.Trigger;

/**
 * Interface to be implemented by the validators.<br>The validator is the central point of the validation framework. It
 * has validation triggers (for example, whenever the user enters some data in a field, presses an Apply button, etc.),
 * which will read the data to be validated (for example, from the input field, a database, etc.), and will pass the
 * data to the various validation rules (for example, expecting data to be entered in a specific format), which will
 * produce validation results (for example, invalid input, valid input, input too long, misspell, etc.), which will be
 * used to give appropriate feedback to the user (for example, a popup dialog, an error icon, etc.).
 *
 * @param <I> Type of the input to be validated.
 * @param <R> Type of the validation results from the validation rules.
 * @see Trigger
 * @see Rule
 * @see FeedBack
 */
public interface Validator<I, R> {

	/**
	 * Adds the specified validation trigger.
	 *
	 * @param trigger Validation trigger.
	 */
	public void addTrigger(Trigger<I> trigger);

	/**
	 * Removes the specified validation trigger.
	 *
	 * @param trigger Validation trigger to be removed.
	 */
	public void removeTrigger(Trigger<I> trigger);

	/**
	 * Adds the specified validation rule.
	 *
	 * @param rule Validation rule.
	 */
	public void addRule(Rule<I, R> rule);

	/**
	 * Removes the specified validation rule.
	 *
	 * @param rule Validation rule.
	 */
	public void removeRule(Rule<I, R> rule);

	/**
	 * Adds the specified feedback on validation result.
	 *
	 * @param feedBack Validation result feedback.
	 */
	public void addFeedBack(FeedBack<R> feedBack);

	/**
	 * Removes the specified feedback on validation result.
	 *
	 * @param feedBack Validation result feedback.
	 */
	public void removeFeedBack(FeedBack<R> feedBack);
}
