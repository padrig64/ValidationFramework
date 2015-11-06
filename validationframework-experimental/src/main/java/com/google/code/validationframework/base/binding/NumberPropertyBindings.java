package com.google.code.validationframework.base.binding;

import com.google.code.validationframework.api.common.Disposable;
import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.WritableProperty;
import com.google.code.validationframework.api.transform.Transformer;
import com.google.code.validationframework.base.transform.CastTransformer;

import java.util.Collection;

import static com.google.code.validationframework.base.binding.Binder.read;

/*
 * Double
 *  |_ asNumber
 *  |_ toDouble
 *  |_ toFloat
 *  |_ toInt
 *  |_ toLong
 *  |_ toShort
 *  |_ toByte
 *  |_ TODO negate 1
 *  |_ TODO abs 1
 *  |_ TODO sin 2
 *  |_ TODO cos 1
 *  |_ TODO tan 1
 *  |_ TODO asin 1
 *  |_ TODO acos 1
 *  |_ TODO atan 1
 *  |_ TODO sinh 1
 *  |_ TODO cosh 1
 *  |_ TODO tanh 1
 *  |_ TODO deg 1
 *  |_ TODO rad 1
 *  |_ TODO exp 1
 *  |_ TODO log 1
 *  |_ TODO log10 1
 *  |_ TODO sqrt 1
 *  |_ TODO cbrt 1
 *  |_ TODO ceil 1
 *  |_ TODO floor 1
 *  |_ TODO round 1
 *  |_ TODO pow 2
 *  |_ TODO modulo 2
 *  |_ TODO add 2
 *  |_ TODO substract 2
 *  |_ TODO multiply 2
 *  |_ TODO divide 2
 *  |_ min
 *  |_ max
 *  |_ TODO sign 1
 *  |_ TODO isGreaterThan 2
 *  |_ TODO isGreaterThanOrEqualTo 2
 *  |_ TODO isLessThan 2
 *  |_ TODO isLessThanOrEqualTo 2
 *  |_ TODO isEqualTo with epsilon 2
 *  |_ TODO isNotEqualTo with epsilon 2
 */
public final class NumberPropertyBindings {

    /**
     * Private constructor for utility class.
     */
    private NumberPropertyBindings() {
        // Nothing to be done
    }

    public static BoundProperty<Number> asNumber(final ReadableProperty<? extends Number> property) {
        return asNumberHelper(property);
    }

    private static <R extends Number> BoundProperty<Number> asNumberHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Number>() {
            @Override
            protected Disposable bind(WritableProperty<Number> targetProperty) {
                return read(property).transform(new CastTransformer<R, Number>()).write(targetProperty);
            }
        };
    }

    public static BoundProperty<Double> toDouble(final ReadableProperty<? extends Number> property) {
        return toDoubleHelper(property);
    }

    private static <R extends Number> BoundProperty<Double> toDoubleHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Double>() {
            @Override
            protected Disposable bind(WritableProperty<Double> targetProperty) {
                return read(property)
                        .transform(new Transformer<R, Double>() {
                            @Override
                            public Double transform(R input) {
                                Double output;
                                if (input == null) {
                                    output = null;
                                } else {
                                    output = input.doubleValue();
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Float> toFloat(final ReadableProperty<? extends Number> property) {
        return toFloatHelper(property);
    }

    private static <R extends Number> BoundProperty<Float> toFloatHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Float>() {
            @Override
            protected Disposable bind(WritableProperty<Float> targetProperty) {
                return read(property)
                        .transform(new Transformer<R, Float>() {
                            @Override
                            public Float transform(R input) {
                                Float output;
                                if (input == null) {
                                    output = null;
                                } else {
                                    output = input.floatValue();
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Long> toLong(final ReadableProperty<? extends Number> property) {
        return toLongHelper(property);
    }

    private static <R extends Number> BoundProperty<Long> toLongHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Long>() {
            @Override
            protected Disposable bind(WritableProperty<Long> targetProperty) {
                return read(property)
                        .transform(new Transformer<R, Long>() {
                            @Override
                            public Long transform(R input) {
                                Long output;
                                if (input == null) {
                                    output = null;
                                } else {
                                    output = input.longValue();
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Integer> toInteger(final ReadableProperty<? extends Number> property) {
        return toIntegerHelper(property);
    }

    private static <R extends Number> BoundProperty<Integer> toIntegerHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Integer>() {
            @Override
            protected Disposable bind(WritableProperty<Integer> targetProperty) {
                return read(property)
                        .transform(new Transformer<R, Integer>() {
                            @Override
                            public Integer transform(R input) {
                                Integer output;
                                if (input == null) {
                                    output = null;
                                } else {
                                    output = input.intValue();
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Short> toShort(final ReadableProperty<? extends Number> property) {
        return toShortHelper(property);
    }

    private static <R extends Number> BoundProperty<Short> toShortHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Short>() {
            @Override
            protected Disposable bind(WritableProperty<Short> targetProperty) {
                return read(property)
                        .transform(new Transformer<R, Short>() {
                            @Override
                            public Short transform(R input) {
                                Short output;
                                if (input == null) {
                                    output = null;
                                } else {
                                    output = input.shortValue();
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static BoundProperty<Byte> toByte(final ReadableProperty<? extends Number> property) {
        return toByteHelper(property);
    }

    private static <R extends Number> BoundProperty<Byte> toByteHelper(final ReadableProperty<R> property) {
        return new AbstractBoundProperty<Byte>() {
            @Override
            protected Disposable bind(WritableProperty<Byte> targetProperty) {
                return read(property)
                        .transform(new Transformer<R, Byte>() {
                            @Override
                            public Byte transform(R input) {
                                Byte output;
                                if (input == null) {
                                    output = null;
                                } else {
                                    output = input.byteValue();
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static <R extends Number> BoundProperty<R> min(final ReadableProperty<R>... properties) {
        return new AbstractBoundProperty<R>() {
            @Override
            protected Disposable bind(WritableProperty<R> targetProperty) {
                return read(properties)
                        .transform(new Transformer<Collection<R>, R>() {
                            @Override
                            public R transform(Collection<R> input) {
                                R output;
                                if ((input == null) || input.isEmpty()) {
                                    output = null;
                                } else {
                                    output = null;
                                    for (R value : input) {
                                        if (value != null) {
                                            if (output == null) {
                                                output = value;
                                            } else if (value.doubleValue() < output.doubleValue()) {
                                                output = value;
                                            }
                                        }
                                    }
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public static <R extends Number> BoundProperty<R> max(final ReadableProperty<R>... properties) {
        return new AbstractBoundProperty<R>() {
            @Override
            protected Disposable bind(WritableProperty<R> targetProperty) {
                return read(properties)
                        .transform(new Transformer<Collection<R>, R>() {
                            @Override
                            public R transform(Collection<R> input) {
                                R output;
                                if ((input == null) || input.isEmpty()) {
                                    output = null;
                                } else {
                                    output = null;
                                    for (R value : input) {
                                        if (value != null) {
                                            if (output == null) {
                                                output = value;
                                            } else if (value.doubleValue() > output.doubleValue()) {
                                                output = value;
                                            }
                                        }
                                    }
                                }
                                return output;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }
}
