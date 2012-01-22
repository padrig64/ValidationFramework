package com.github.validationframework.utils;

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
	 * Compares the two given values by taking null values into account.
	 *
	 * @param val1 First value.
	 * @param val2 Second value.
	 *
	 * @return True if both values are equal or if both are null.
	 */
	public static boolean areEqual(Object val1, Object val2) {
		boolean equal = false;

		if ((val1 == null) && (val2 == null)) {
			equal = true;
		} else if (val1 != null) {
			equal = val1.equals(val2);
		}

		return equal;
	}
}
