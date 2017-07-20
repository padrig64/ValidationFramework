/*
 * Copyright (c) 2017, ValidationFramework Authors
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
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.api.rule.Rule;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.property.AbstractReadableProperty;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import com.google.code.validationframework.base.property.simple.SimpleProperty;
import com.google.code.validationframework.base.property.wrap.NegateBooleanPropertyWrapper;
import com.google.code.validationframework.base.property.wrap.ReadOnlyPropertyWrapper;
import com.google.code.validationframework.base.transform.AndBooleanAggregator;
import com.google.code.validationframework.base.transform.OrBooleanAggregator;
import com.google.code.validationframework.base.utils.ValueUtils;

import java.util.Collection;
import java.util.Collections;

import static com.google.code.validationframework.base.binding.Binder.read;

/*
 * Object
 *  |_ isEqualTo
 *  |_ isNotEqualTo
 *  |_ isNull
 *  |_ isNotNull
 *  |_ toString
 *  |_ toObject
 *
 * Boolean
 *  |_ and
 *  |_ or
 *  |_ not
 *  |_ nand
 *  |_ nor
 *  |_ xor
 *  |_ xnor
 *
 * Double
 *  |_ toInt
 *  |_ toLong
 *  |_ toFloat
 *  |_ toDouble
 *  |_ negate
 *  |_ abs
 *  |_ sin
 *  |_ cos
 *  |_ tan
 *  |_ asin
 *  |_ acos
 *  |_ atan
 *  |_ sinh
 *  |_ cosh
 *  |_ tanh
 *  |_ deg
 *  |_ rad
 *  |_ exp
 *  |_ log
 *  |_ log10
 *  |_ sqrt
 *  |_ cbrt
 *  |_ ceil
 *  |_ floor
 *  |_ round
 *  |_ pow
 *  |_ modulo
 *  |_ add
 *  |_ substract
 *  |_ multiply
 *  |_ divide
 *  |_ min
 *  |_ max
 *  |_ sign
 *  |_ isGreaterThan
 *  |_ isGreaterThanOrEqualTo
 *  |_ isLessThan
 *  |_ isLessThanOrEqualTo
 *  |_ isEqualTo with epsilon
 *  |_ isNotEqualTo with epsilon
 *  |_ toObject
 *
 * String
 *  |_ length
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ concat
 *  |_ contains
 *  |_ matches
 *  |_ isEqualToIgnoreCase
 *  |_ isNotEqualToIgnoreCase
 *
 * Set
 *  |_ size
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ contains
 *
 * List
 *  |_ size
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ valueAt
 *  |_ contains
 *
 * Map
 *  |_ size
 *  |_ isEmpty
 *  |_ isNotEmpty
 *  |_ valueAt
 *  |_ containsValue
 *  |_ containsKey
 *
 * If / Then / Else
 *
 */
public final class Bindings {

    /**
     * Private constructor for utility class.
     */
    private Bindings() {
        // Nothing to be done
    }

    public static ReadableProperty<Boolean> not(ReadableProperty<Boolean> property) {
        return new NegateBooleanPropertyWrapper(property);
    }

    public static ReadableProperty<Boolean> and(ReadableProperty<Boolean>... properties) {
        SimpleBooleanProperty result = new SimpleBooleanProperty();
        Disposable bond = read(properties).transform(new AndBooleanAggregator()).write(result);
        return new BoundProperty<Boolean>(result, bond);
    }

    public static ReadableProperty<Boolean> or(ReadableProperty<Boolean>... properties) {
        SimpleBooleanProperty result = new SimpleBooleanProperty();
        read(properties).transform(new OrBooleanAggregator()).write(result);
        return new ReadOnlyPropertyWrapper<Boolean>(result);
    }

    public static ReadableProperty<Boolean> isEmpty(ReadableProperty<String> property) {
        SimpleBooleanProperty result = new SimpleBooleanProperty();
        Disposable bond = read(property)
                .transform(new Transformer<String, Boolean>() {
                    @Override
                    public Boolean transform(String input) {
                        return (input == null) || input.isEmpty();
                    }
                })
                .write(result);
        return new BoundProperty<Boolean>(result, bond);
    }

    public static ReadableProperty<Boolean> isNotEmpty(ReadableProperty<String> property) {
        SimpleBooleanProperty result = new SimpleBooleanProperty();
        Disposable bond = read(property)
                .transform(new Transformer<String, Boolean>() {
                    @Override
                    public Boolean transform(String input) {
                        return (input != null) && !input.isEmpty();
                    }
                })
                .write(result);
        return new BoundProperty<Boolean>(result, bond);
    }

    public static ReadableProperty<Boolean> isTrue(ReadableProperty<Boolean> property) {
        return isEqualTo(property, true);
    }

    public static ReadableProperty<Boolean> isNotTrue(ReadableProperty<Boolean> property) {
        return isNotEqualTo(property, true);
    }

    public static ReadableProperty<Boolean> isFalse(ReadableProperty<Boolean> property) {
        return isEqualTo(property, false);
    }

    public static ReadableProperty<Boolean> isNotFalse(ReadableProperty<Boolean> property) {
        return isNotEqualTo(property, false);
    }

    public static ReadableProperty<Boolean> isEqualTo(ReadableProperty<?> property, final Object refValue) {
        SimpleBooleanProperty result = new SimpleBooleanProperty();
        // No cast should be needed in R4
        Disposable bond = read((ReadableProperty<Object>) property)
                .transform(new Transformer<Object, Boolean>() {
                    @Override
                    public Boolean transform(Object input) {
                        return ValueUtils.areEqual(input, refValue);
                    }
                })
                .write(result);
        return new BoundProperty<Boolean>(result, bond);
    }

    public static ReadableProperty<Boolean> isNotEqualTo(ReadableProperty<?> property, final Object refValue) {
        SimpleBooleanProperty result = new SimpleBooleanProperty();
        // No cast should be needed in R4
        Disposable bond = read((ReadableProperty<Object>) property)
                .transform(new Transformer<Object, Boolean>() {
                    @Override
                    public Boolean transform(Object input) {
                        return !ValueUtils.areEqual(input, refValue);
                    }
                })
                .write(result);
        return new BoundProperty<Boolean>(result, bond);
    }

    public static <RI, RO> ReadableProperty<RO> check(final Rule<RI, RO> rule, ReadableProperty<RI> property) {
        SimpleProperty<RO> result = new SimpleProperty<RO>();
        Disposable bond = read(property)
                .transform(new Transformer<RI, RO>() {
                    @Override
                    public RO transform(RI input) {
                        return rule.validate(input);
                    }
                })
                .write(result);
        return new BoundProperty<RO>(result, bond);
    }

    public static <I, O> ReadableProperty<O> transform(Transformer<I, O> transformer, ReadableProperty<I> property) {
        SimpleProperty<O> result = new SimpleProperty<O>();
        Disposable bond = read(property).transform(transformer).write(result);
        return new BoundProperty<O>(result, bond);
    }

    public static class BoundProperty<R> extends AbstractReadableProperty<R> implements Disposable {

        private final Collection<Disposable> disposables;

        private ReadableProperty<R> resultProperty;

        public BoundProperty(ReadableProperty<R> resultProperty, Disposable disposable) {
            this(resultProperty, Collections.singleton(disposable));
            resultProperty.addValueChangeListener(new ValueChangeListener<R>() {
                @Override
                public void valueChanged(ReadableProperty<R> property, R oldValue, R newValue) {
                    maybeNotifyListeners(oldValue, newValue);
                }
            });
        }

        public BoundProperty(ReadableProperty<R> resultProperty, Collection<Disposable> disposables) {
            this.resultProperty = resultProperty;
            this.disposables = disposables;
        }

        @Override
        public R getValue() {
            R value;
            if (resultProperty == null) {
                value = null;
            } else {
                value = resultProperty.getValue();
            }
            return value;
        }

        @Override
        public void dispose() {
            for (Disposable disposable : disposables) {
                disposable.dispose();
            }
            disposables.clear();
            if (resultProperty instanceof Disposable) {
                ((Disposable) resultProperty).dispose();
                resultProperty = null;
            }
        }
    }
}
