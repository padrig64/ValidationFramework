/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.swing.dataprovider;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.CastTransformer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import java.awt.Component;
import java.text.ParseException;

/**
 * Provider of the value of the current formatted text editor component from a given table.
 * <p/>
 * Note that if the table is not in editing state, no value can be provided.
 *
 * @param <DPO> Type of data in the text editor.<br>
 *              You may use {@link Object}.
 *
 * @see DataProvider
 * @see JTable
 * @see JTable#getCellEditor()
 */
public class JTableTextEditorValueProvider<DPO> implements DataProvider<DPO> {

    /**
     * Logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(JTableTextEditorValueProvider.class);

    /**
     * Table holding the editor component to get the text from.
     */
    private final JTable table;

    /**
     * Transformer used to convert the object parsed from the formatted text editor component to the expected type.
     */
    private final Transformer<Object, DPO> transformer;

    /**
     * Constructor specifying the table holding the editor component to get the value from.
     *
     * @param table Editable table.
     */
    public JTableTextEditorValueProvider(JTable table) {
        this(table, new CastTransformer<Object, DPO>());
    }

    /**
     * Constructor specifying the table holding the editor component to get the value from and the transformer to be
     * used
     * to convert the text to convert the value to the required type.
     *
     * @param table       Editable table.
     * @param transformer Transformer used to convert the object parsed from the formatted text editor component to the
     *                    expected type.
     */
    public JTableTextEditorValueProvider(JTable table, Transformer<Object, DPO> transformer) {
        this.table = table;
        this.transformer = transformer;
    }

    /**
     * Gets the component providing the data to be validated.
     *
     * @return Component providing the data to be validated.
     */
    public JTable getComponent() {
        return table;
    }

    /**
     * @see DataProvider#getData()
     */
    @Override
    public DPO getData() {
        DPO typedValue = null;

        // Get the formatted textfield editor from the table, if any
        Component editorComponent = table.getEditorComponent();
        if (editorComponent instanceof JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) editorComponent;

            // Parse text
            Object dataValue = null;
            try {
                String dataText = formattedTextField.getText();
                JFormattedTextField.AbstractFormatter formatter = formattedTextField.getFormatter();
                if (formatter != null) {
                    dataValue = formatter.stringToValue(dataText);
                }
            } catch (ParseException e) {
                dataValue = null;
            }

            // Convert it to the required type
            typedValue = transformer.transform(dataValue);
        } else {
            LOGGER.warn("Table editor component is not a JFormattedTextField: " + editorComponent);
        }

        return typedValue;
    }
}
