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

import java.text.MessageFormat;
import java.util.Locale;

import static com.google.code.validationframework.base.binding.Binder.read;
import static com.google.code.validationframework.base.binding.BooleanPropertyBindings.not;
import static com.google.code.validationframework.base.binding.ObjectPropertyBindings.isEqualTo;
import static com.google.code.validationframework.base.binding.ObjectPropertyBindings.isNotEqualTo;

/*
 * String
 *  |_ length(ReadableProperty)
 *  |_ isEmpty(ReadableProperty)
 *  |_ isNotEmpty(ReadableProperty)
 *  |_ TODO concat
 *  |_ append
 *  |_ prepend
 *  |_ contains
 *  |_ doesNotContain
 *  |_ startsWith
 *  |_ doesNotStartWith
 *  |_ endsWith
 *  |_ doesNotEndWith
 *  |_ matches
 *  |_ doesNotMatch
 *  |_ TODO isEqualToIgnoreCase
 *  |_ TODO isNotEqualToIgnoreCase
 *  |_ TODO trimLeft
 *  |_ TODO trimRight
 *  |_ trim(ReadableProperty)
 *  |_ format
 *  |_ toLowerCase(ReadableProperty)
 *  |_ toLowerCase(ReadableProperty, Locale)
 *  |_ toUpperCase(ReadableProperty)
 *  |_ toUpperCase(ReadableProperty, Locale)
 *  |_ TODO split
 *  |_ replace(ReadableProperty, char, char)
 *  |_ replace(ReadableProperty, CharSequence, CharSequence)
 *  |_ replaceAll(ReadableProperty, String, String)
 *  |_ replaceFirst(ReadableProperty, String, String)
 */
public final class StringPropertiesBindings {

    /**
     * Private constructor for utility class.
     */
    private StringPropertiesBindings() {
        // Nothing to be done
    }

    public BoundProperty<Integer> length(final ReadableProperty<String> property) {
        return new AbstractBoundProperty<Integer>() {
            @Override
            protected Disposable bind(WritableProperty<Integer> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, Integer>() {
                            @Override
                            public Integer transform(String input) {
                                int length;
                                if (input == null) {
                                    length = 0;
                                } else {
                                    length = input.length();
                                }
                                return length;
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<Boolean> isEmpty(ReadableProperty<String> property) {
        return isEqualTo(length(property), 0);
    }

    public BoundProperty<Boolean> isNotEmpty(ReadableProperty<String> property) {
        return isNotEqualTo(length(property), 0);
    }

    public BoundProperty<String> prepend(final ReadableProperty<String> property, final Object... prefixes) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                StringBuilder result = new StringBuilder();
                                for (Object prefix : prefixes) {
                                    result.append(prefix);
                                }
                                result.append(input);
                                return result.toString();
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> append(final ReadableProperty<String> property, final Object... suffixes) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                StringBuilder result = new StringBuilder(input);
                                for (Object suffix : suffixes) {
                                    result.append(suffix);
                                }
                                return result.toString();
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<Boolean> contains(final ReadableProperty<String> property, final CharSequence sequence) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, Boolean>() {
                            @Override
                            public Boolean transform(String input) {
                                return input.contains(sequence);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<Boolean> doesNotContain(ReadableProperty<String> property, CharSequence sequence) {
        return not(contains(property, sequence));
    }

    public BoundProperty<Boolean> startsWith(final ReadableProperty<String> property, final String prefix) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, Boolean>() {
                            @Override
                            public Boolean transform(String input) {
                                return input.startsWith(prefix);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<Boolean> doesNotStartWith(ReadableProperty<String> property, String prefix) {
        return not(startsWith(property, prefix));
    }

    public BoundProperty<Boolean> endsWith(final ReadableProperty<String> property, final String suffix) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, Boolean>() {
                            @Override
                            public Boolean transform(String input) {
                                return input.endsWith(suffix);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<Boolean> doesNotEndWith(ReadableProperty<String> property, String suffix) {
        return not(endsWith(property, suffix));
    }

    public BoundProperty<Boolean> matches(final ReadableProperty<String> property, final String regex) {
        return new AbstractBoundProperty<Boolean>() {
            @Override
            protected Disposable bind(WritableProperty<Boolean> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, Boolean>() {
                            @Override
                            public Boolean transform(String input) {
                                return input.matches(regex);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<Boolean> doesNotMatch(ReadableProperty<String> property, String regex) {
        return not(matches(property, regex));
    }

    public BoundProperty<String> trim(final ReadableProperty<String> property) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.trim();
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> toLowerCase(final ReadableProperty<String> property) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.toLowerCase();
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> toLowerCase(final ReadableProperty<String> property, final Locale locale) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.toLowerCase(locale);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> toUpperCase(final ReadableProperty<String> property) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.toUpperCase();
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> toUpperCase(final ReadableProperty<String> property, final Locale locale) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.toUpperCase(locale);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> format(final ReadableProperty<String> property, final Object... args) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return MessageFormat.format(input, args);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> replace(final ReadableProperty<String> property,
                                         final char oldChar, final char newChar) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.replace(oldChar, newChar);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> replace(final ReadableProperty<String> property,
                                         final CharSequence oldSequence, final CharSequence newSequence) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.replace(oldSequence, newSequence);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> replaceAll(final ReadableProperty<String> property,
                                            final String regex, final String newSequence) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.replaceAll(regex, newSequence);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }

    public BoundProperty<String> replaceFirst(final ReadableProperty<String> property,
                                              final String regex, final String newSequence) {
        return new AbstractBoundProperty<String>() {
            @Override
            protected Disposable bind(WritableProperty<String> targetProperty) {
                return read(property)
                        .transform(new Transformer<String, String>() {
                            @Override
                            public String transform(String input) {
                                return input.replaceFirst(regex, newSequence);
                            }
                        })
                        .write(targetProperty);
            }
        };
    }
}
