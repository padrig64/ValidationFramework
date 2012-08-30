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

public class BaseNumberLessThanRule<T extends Number> implements Rule<T, Boolean> {

	private T minimumValue = null;

	/**
	 * Default constructor.
	 */
	public BaseNumberLessThanRule() {
		// Nothing to be done
	}

	public BaseNumberLessThanRule(final T minimumValue) {
		setMinimumValue(minimumValue);
	}

	public Number getMinimumValue() {
		return minimumValue;
	}

	public void setMinimumValue(final T minimumValue) {
		this.minimumValue = minimumValue;
	}

	/**
	 * @see com.github.validationframework.api.rule.Rule#validate(Object)
	 */
	@Override
	public Boolean validate(final T data) {
		double comparableDataValue = Double.NaN;
		if (data != null) {
			comparableDataValue = data.doubleValue();
		}
		double comparableRuleValue = Double.NaN;
		if (minimumValue != null) {
			comparableRuleValue = minimumValue.doubleValue();
		}

		return (Double.isNaN(comparableDataValue) && Double.isNaN(comparableRuleValue)) ||
				(comparableDataValue < comparableRuleValue);
	}
}
