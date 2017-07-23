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

package com.google.code.validationframework.base.utils;

import java.util.Comparator;

/**
 * Utility class dealing with various objects and values.
 */
public final class ValueUtils {

    /**
     * Private constructor for utility class.
     */
    private ValueUtils() {
        // Nothing to be done
    }

    /**
     * Compares the two given values by taking null and NaN values into account.
     * <p>
     * Two null values will be considered equal. Two NaN values (either Double or Float) will be considered equal.
     *
     * @param value1 First value.
     * @param value2 Second value.
     *
     * @return True if both values are equal or if both are null.
     */
    public static boolean areEqual(Object value1, Object value2) {
        return ((value1 == null) && (value2 == null)) || //
                (isNaN(value1) && isNaN(value2)) || //
                ((value1 != null) && value1.equals(value2));
    }

    /**
     * Compares the two given values by taking null values into account and the specified comparator.
     *
     * @param value1     First value.
     * @param value2     Second value.
     * @param comparator Comparator to be used to compare the two values in case they are both non null.
     * @param <T>        Type of values to be compared.
     *
     * @return True if both values are null, or equal according to the comparator.
     */
    public static <T> boolean areEqual(T value1, T value2, Comparator<T> comparator) {
        return ((value1 == null) && (value2 == null)) || //
                ((value1 != null) && (value2 != null) && (comparator.compare(value1, value2) == 0));
    }

    /**
     * Checks if the specified value is either NaN.
     *
     * @param value Value to be checked.
     *
     * @return True if the value is NaN, false otherwise.
     */
    private static boolean isNaN(Object value) {
        return ((value instanceof Float) && Float.isNaN((Float) value)) || //
                ((value instanceof Double) && Double.isNaN((Double) value));
    }
}
