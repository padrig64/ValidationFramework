package com.github.validationframework.utils.swing;

import java.awt.Color;

import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.UIResource;

/**
 * Utility class dealing with colors.
 */
public final class ColorUtils {

	/**
	 * Private constructor for utility class.
	 */
	private ColorUtils() {
		// Nothing to be done
	}

	/**
	 * Blends the two colors taking into account their alpha.<br> If one of the two input colors implements the UIResource
	 * interface, the result color will also implement this interface.
	 *
	 * @param src Source color to be blended into the destination color.
	 * @param dst Destination color on which the source color is to be blended.
	 *
	 * @return Color resulting from the color blending.
	 */
	public static Color alphaBlend(Color src, Color dst) {
		Color blend;

		float srcA = (float) src.getAlpha() / 255.0f;
		float dstA = (float) dst.getAlpha() / 255.0f;
		float outA = srcA + dstA * (1 - srcA);

		if (outA > 0) {
			float outR = ((float) src.getRed() * srcA + (float) dst.getRed() * dstA * (1.0f - srcA)) / outA;
			float outG = ((float) src.getGreen() * srcA + (float) dst.getGreen() * dstA * (1.0f - srcA)) / outA;
			float outB = ((float) src.getBlue() * srcA + (float) dst.getBlue() * dstA * (1.0f - srcA)) / outA;
			blend = new Color((int) outR, (int) outG, (int) outB, (int) (outA * 255.0f));
		} else {
			blend = new Color(0, 0, 0, 0);
		}

		if ((src instanceof UIResource) || (dst instanceof UIResource)) {
			blend = new ColorUIResource(blend);
		}

		return blend;
	}

	public static String toString(Color c) {
		StringBuilder sb = new StringBuilder();

		sb.append(c.getClass().getSimpleName()); // Can be a ColorUIResource
		sb.append("[");
		sb.append(c.getRed());
		sb.append(",");
		sb.append(c.getGreen());
		sb.append(",");
		sb.append(c.getBlue());
		sb.append(",");
		sb.append(c.getAlpha());
		sb.append("]");

		return sb.toString();
	}
}
