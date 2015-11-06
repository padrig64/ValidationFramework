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

package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;
import com.google.code.validationframework.base.transform.NegateBooleanTransformer;

import java.util.Collection;

import static com.google.code.validationframework.base.binding.Binder.read;
import static com.google.code.validationframework.base.binding.ObjectPropertyBindings.isEqualTo;
import static com.google.code.validationframework.base.binding.ObjectPropertyBindings.isNotEqualTo;

/*
 * Boolean
 *  |_ and(ReadableProperty...)
 *  |_ or(ReadableProperty...)
 *  |_ not(ReadableProperty)
 *  |_ nand(ReadableProperty...)
 *  |_ nor(ReadableProperty...)
 *  |_ xor(ReadableProperty...)
 *  |_ xnor(ReadableProperty...)
 *  |_ isTrue(ReadableProperty)
 *  |_ isNotTrue(ReadableProperty)
 *  |_ isFalse(ReadableProperty)
 *  |_ isNotFalse(ReadableProperty)
 */
public final class BooleanPropertyBindings {

    /**
     * Private constructor for utility class.
     */
    private BooleanPropertyBindings() {
        // Nothing to be done
    }

    public static BoundProperty<Boolean> not(final ReadableProperty<Boolean> property) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(property).transform(new NegateBooleanTransformer()).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> and(final ReadableProperty<Boolean>... properties) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(properties).transform(new AndBooleanAggregator()).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> nand(ReadableProperty<Boolean>... properties) {
        return not(and(properties));
    }

    public static BoundProperty<Boolean> or(final ReadableProperty<Boolean>... properties) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(properties).transform(new AndBooleanAggregator()).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> nor(ReadableProperty<Boolean>... properties) {
        return not(or(properties));
    }

    public static BoundProperty<Boolean> xor(final ReadableProperty<Boolean>... properties) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(properties).transform(new Transformer<Collection<Boolean>, Boolean>() {
                    @Override
                    public Boolean transform(Collection<Boolean> input) {
                        boolean result = false;
                        for (Boolean value : input) {
                            if (Boolean.TRUE.equals(value)) {
                                if (result) {
                                    // Already another value being true
                                    result = false;
                                    break;
                                } else {
                                    // Found first value being true
                                    result = true;
                                }
                            }
                        }
                        return result;
                    }
                }).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> xnor(final ReadableProperty<Boolean>... properties) {
        return not(xor(properties));
    }

    public static BoundProperty<Boolean> isTrue(ReadableProperty<Boolean> property) {
        return isEqualTo(property, true);
    }

    public static BoundProperty<Boolean> isNotTrue(ReadableProperty<Boolean> property) {
        return isNotEqualTo(property, true);
    }

    public static BoundProperty<Boolean> isFalse(ReadableProperty<Boolean> property) {
        return isEqualTo(property, false);
    }

    public static BoundProperty<Boolean> isNotFalse(ReadableProperty<Boolean> property) {
        return isNotEqualTo(property, false);
    }
}
