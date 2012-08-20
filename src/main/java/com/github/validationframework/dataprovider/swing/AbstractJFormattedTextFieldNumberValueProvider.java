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

package com.github.validationframework.dataprovider.swing;

import com.github.validationframework.dataprovider.TypedDataProvider;
import java.text.ParseException;
import javax.swing.JFormattedTextField;

public abstract class AbstractJFormattedTextFieldNumberValueProvider<T extends Number> implements TypedDataProvider<T> {

	private final JFormattedTextField formattedTextField;

	public AbstractJFormattedTextFieldNumberValueProvider(final JFormattedTextField formattedTextField) {
		this.formattedTextField = formattedTextField;
	}

	/**
	 * @see TypedDataProvider#getData()
	 */
	@Override
	public T getData() {
		T numberValue = null;

		try {
			// Parse text
			final String dataText = formattedTextField.getText();
			final JFormattedTextField.AbstractFormatter formatter = formattedTextField.getFormatter();
			final Object dataValue = formatter.stringToValue(dataText);

			// Convert it to the required type
			numberValue = getNumberFromObject(dataValue);
		} catch (ParseException e) {
			numberValue = null;
		}

		System.out.println("JFormattedTextFieldNumberValueProvider.getData: " + numberValue);

		return numberValue;
	}

	protected abstract T getNumberFromObject(final Object value);
}
