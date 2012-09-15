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

package com.github.validationframework.base.rule.number;

import com.github.validationframework.api.rule.Rule;

/**
 * Rule checking whether the data, being a number, is equal to a specific value.<br>Note that if the data and the
 * specified value are both null or NaN, they will be considered equal.
 *
 * @param <T> Type of number handled by this rule.<br>It also it is not really required for the internal logic of the
 * rule, it helps in reducing compilation warnings and/or errors when add a rule in a validator.
 *
 * @see Rule
 */
public class NumberEqualToRule<T extends Number> implements Rule<T, Boolean> {

	/**
	 * Value to which the data is to be compared.
	 */
	private T exactValue = null;

	/**
	 * Default constructor.
	 */
	public NumberEqualToRule() {
		// Nothing to be done
	}

	/**
	 * Constructor specifying the value to which the data is to be compared.
	 *
	 * @param exactValue Value to which the data is to be compared.
	 */
	public NumberEqualToRule(final T exactValue) {
		setExactValue(exactValue);
	}

	/**
	 * Gets the value to which the data is compared.
	 *
	 * @return Value to which the data is compared.
	 */
	public T getExactValue() {
		return exactValue;
	}

	/**
	 * Sets the value to which the data is to be compared.
	 *
	 * @param exactValue Value to which the data is to be compared.
	 */
	public void setExactValue(final T exactValue) {
		this.exactValue = exactValue;
	}

	/**
	 * @see Rule#validate(Object)
	 */
	@Override
	public Boolean validate(final T data) {
		double comparableDataValue = Double.NaN;
		if (data != null) {
			comparableDataValue = data.doubleValue();
		}
		double comparableRuleValue = Double.NaN;
		if (exactValue != null) {
			comparableRuleValue = exactValue.doubleValue();
		}

		return (Double.compare(comparableDataValue, comparableRuleValue) == 0);
	}
}
