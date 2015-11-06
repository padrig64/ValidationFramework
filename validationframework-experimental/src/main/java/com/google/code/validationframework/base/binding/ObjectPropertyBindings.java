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
import com.google.code.validationframework.base.property.CompositeReadableProperty;
import com.google.code.validationframework.base.property.wrap.ReadOnlyPropertyWrapper;
import com.google.code.validationframework.base.transform.CastTransformer;
import com.google.code.validationframework.base.transform.EqualsTransformer;
import com.google.code.validationframework.base.transform.ToStringTransformer;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.Collection;

import static com.google.code.validationframework.base.binding.Binder.read;
import static com.google.code.validationframework.base.binding.BooleanPropertyBindings.not;

/*
 * Object
 *  |_ areEqual(ReadableProperty...)
 *  |_ isEqualTo(ReadableProperty, Object)
 *  |_ isNotEqualTo(ReadableProperty, Object)
 *  |_ isNull(ReadableProperty)
 *  |_ isNotNull(ReadableProperty)
 *  |_ toString(ReadableProperty)
 *  |_ asObject(ReadableProperty)
 *  |_ TODO check(Rule)
 *  |_ TODO transform
 *  |_ readOnly(ReadableProperty)
 *
 * TODO Set
 *  |_ size
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ contains
 *  |_ doesNotContain
 *
 * TODO List
 *  |_ size
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ valueAt
 *  |_ contains
 *  |_ doesNotContain
 *
 * TODO Map
 *  |_ size
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ valueAt
 *  |_ containsValue
 *  |_ doesNotContainValue
 *  |_ containsKey
 *  |_ doesNotContainKey
 *
 * TODO If / Then / Else
 *
 * TODO composite(...)
 */
public final class ObjectPropertyBindings {

    /**
     * Private constructor for utility class.
     */
    private ObjectPropertyBindings() {
        // Nothing to be done
    }

    public static <R> BoundProperty<R> readOnly(ReadableProperty<R> property) {
        return new ReadOnlyBoundPropertyWrapper<R>(property);
    }

    public static BoundProperty<Object> asObject(final ReadableProperty<?> property) {
        return asObjectHelper(property);
    }

    private static <R> BoundProperty<Object> asObjectHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Object>() {
            @Override
            protected Disposable bind(WritableProperty<Object> targetProperty) {
                return read(property).transform(new CastTransformer<R, Object>()).write(targetProperty);
            }
        };
    }

    public static BoundProperty<String> toString(final ReadableProperty<?> property) {
        return toStringHelper(property);
    }

    private static <R> BoundProperty<String> toStringHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property).transform(new ToStringTransformer<R>()).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> isNull(final ReadableProperty<?> property) {
        return isNullHelper(property);
    }

    private static <R> BoundProperty<Boolean> isNullHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(property).transform(new EqualsTransformer<R>(null)).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> isNotNull(final ReadableProperty<?> property) {
        return not(isNull(property));
    }

    public static BoundProperty<Boolean> isEqualTo(final ReadableProperty<?> property,
                                                   final Object referenceValue) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(asObject(property))
                        .transform(new EqualsTransformer<Object>(referenceValue))
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> areEqual(final ReadableProperty<?>... properties) {
        return new AbstractBoundProperty<Boolean>() {

            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                CompositeReadableProperty<Object> composite = new CompositeReadableProperty<Object>();
                for (ReadableProperty<?> property : properties) {
                    composite.addProperty(asObject(property));
                }
                return read(composite)
                        .transform(new Transformer<Collection<Object>, Boolean>() {
                            @Override
                            public Boolean transform(Collection<Object> input) {
                                Boolean result;
                                if ((input == null) || (input.size() < 2)) {
                                    result = null;
                                } else {
                                    result = null;
                                    Object ref = null;
                                    for (Object value : input) {
                                        if (ref == null) {
                                            ref = value;
                                        } else if (!ValueUtils.areEqual(value, ref)) {
                                            result = false;
                                            break;
                                        }
                                    }
                                    if (result == null) {
                                        result = true;
                                    }
                                }
                                return result;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Boolean> isNotEqualTo(final ReadableProperty<?> property,
                                                      final Object referenceValue) {
        return not(isEqualTo(property, referenceValue));
    }

    private static class ReadOnlyBoundPropertyWrapper<R> extends ReadOnlyPropertyWrapper<R>
            implements BoundProperty<R> {

        public ReadOnlyBoundPropertyWrapper(ReadableProperty<R> wrappedProperty) {
            super(wrappedProperty);
        }
    }
}
