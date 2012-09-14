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

package com.github.validationframework.experimental.rule;

import com.github.validationframework.api.rule.Rule;
import javax.swing.AbstractSpinnerModel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenience rule that will check if the data is within the bounds of the spinner model.<br>Note that the model of the
 * spinner should be an {@link AbstractSpinnerModel}.
 */
public class JSpinnerNumberModelRule<D extends Number> implements Rule<Comparable<D>, Boolean> {

	/**
	 * Logger for this class.
	 */
	private final Logger LOGGER = LoggerFactory.getLogger(JSpinnerNumberModelRule.class);

	/**
	 * Spinner whose {@link SpinnerNumberModel} is to be checked.
	 */
	private final JSpinner spinner;

	/**
	 * Constructor specifying the spinner whose {@link SpinnerNumberModel} is to be checked.
	 *
	 * @param spinner Spinner whose {@link SpinnerNumberModel} is to be checked.
	 */
	public JSpinnerNumberModelRule(final JSpinner spinner) {
		this.spinner = spinner;
	}

	/**
	 * @see Rule#validate(Object)
	 */
	@Override
	public Boolean validate(final Comparable<D> data) {
		Boolean result = true;

		final SpinnerModel model = spinner.getModel();
		if (model instanceof SpinnerNumberModel) {
			final SpinnerNumberModel numberModel = (SpinnerNumberModel) model;

			// Check min bound
			if (numberModel.getMinimum() != null) {

			}

			// Check max bound
			if (result && (numberModel.getMaximum() != null)) {

			}
		} else {
			LOGGER.error("Spinner model is not an AbstractSpinnerModel: " + spinner);
			result = false;
		}

		return result;
	}
}
