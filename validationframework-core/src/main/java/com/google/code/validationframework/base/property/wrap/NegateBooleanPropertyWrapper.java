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

package com.google.code.validationframework.base.property.wrap;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.AbstractReadableProperty;
import com.google.code.validationframework.base.transform.NegateBooleanTransformer;

/**
 * Wrapper for a boolean readable property and that will negate the value of the wrapped property.
 *
 * @see NegateBooleanTransformer
 */
public class NegateBooleanPropertyWrapper extends AbstractReadableProperty<Boolean> {

    /**
     * Listener to changes of the value of the wrapped property.
     */
    private class ValueChangeAdapter implements ValueChangeListener<Boolean> {

        /**
         * @see ValueChangeListener#valueChanged(ReadableProperty, Object, Object)
         */
        @Override
        public void valueChanged(ReadableProperty<Boolean> property, Boolean oldValue, Boolean newValue) {
            Boolean transformedOldValue = transformer.transform(oldValue);
            Boolean transformedNewValue = transformer.transform(newValue);
            maybeNotifyListeners(transformedOldValue, transformedNewValue);
        }
    }

    /**
     * Wrapped property whose value is to be negated.
     */
    private final ReadableProperty<Boolean> wrappedProperty;

    /**
     * Transformer used to negate the boolean property.
     */
    private final Transformer<Boolean, Boolean> transformer;

    /**
     * Constructor specifying the property to be wrapped and negated.
     * <p>
     * The value representing the negation of a null value will be a null value.
     *
     * @param wrappedProperty Property to be wrapped and negated.
     */
    public NegateBooleanPropertyWrapper(ReadableProperty<Boolean> wrappedProperty) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addValueChangeListener(new ValueChangeAdapter());
        this.transformer = new NegateBooleanTransformer();
    }

    /**
     * Constructor specifying the property be wrapped and negated, and the value representing the negation of a null
     * value.
     *
     * @param wrappedProperty Property to be wrapped and negated.
     * @param nullNegation    Value representing the negation of a null value.
     */
    public NegateBooleanPropertyWrapper(ReadableProperty<Boolean> wrappedProperty, Boolean nullNegation) {
        this.wrappedProperty = wrappedProperty;
        this.wrappedProperty.addValueChangeListener(new ValueChangeAdapter());
        this.transformer = new NegateBooleanTransformer(nullNegation);
    }

    /**
     * @see AbstractReadableProperty#getValue()
     */
    @Override
    public Boolean getValue() {
        return transformer.transform(wrappedProperty.getValue());
    }
}
