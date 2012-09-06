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

package com.github.validationframework.swing.dataprovider;

import com.github.validationframework.api.dataprovider.TypedDataProvider;
import com.github.validationframework.base.transform.CastTransformer;
import com.github.validationframework.base.transform.Transformer;
import java.awt.Component;
import java.text.ParseException;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provider of the text of the current text editor component from a given table.<br>Note that if the table is not in
 * editing, no text can be provided.
 */
public class JTableTextEditorValueProvider<T> implements TypedDataProvider<T> {

	/**
	 * Logger for this class.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(JTableTextEditorValueProvider.class);

	/**
	 * Table holding the editor component to get the text from.
	 */
	private final JTable table;

	private final Transformer<Object, T> transformer;

	/**
	 * Constructor specifying the table holding the editor component to get the text from.
	 *
	 * @param table Editable table.
	 */
	public JTableTextEditorValueProvider(final JTable table) {
		this(table, new CastTransformer<Object, T>());
	}

	public JTableTextEditorValueProvider(final JTable table, final Transformer<Object, T> transformer) {
		this.table = table;
		this.transformer = transformer;
	}

	/**
	 * @see TypedDataProvider#getData()
	 */
	@Override
	public T getData() {
		T typedValue = null;

		final Component editorComponent = table.getEditorComponent();
		if (editorComponent instanceof JFormattedTextField) {
			final JFormattedTextField formattedTextField = (JFormattedTextField) editorComponent;

			try {
				// Parse text
				final String dataText = formattedTextField.getText();
				final JFormattedTextField.AbstractFormatter formatter = formattedTextField.getFormatter();
				if (formatter != null) {
					final Object dataValue = formatter.stringToValue(dataText);

					// Convert it to the required type
					typedValue = transformer.transform(dataValue);
				}
			} catch (ParseException e) {
				// Nothing to be done
			}
		} else {
			LOGGER.warn("Table editor component is not a JFormattedTextField: " + editorComponent);
		}

		return typedValue;
	}
}
