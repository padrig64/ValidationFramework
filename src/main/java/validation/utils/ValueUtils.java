package validation.utils;

public final class ValueUtils {

	/**
	 * Private constructor for utility class.
	 */
	private ValueUtils() {
		// Nothing to be done
	}

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
