/*
 * Copyright (c) 2012, Patrick Moawad
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

package com.github.moawad.validationframework.swing.utils;

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
	 * Blends the two colors taking into account their alpha.<br>If one of the two input colors implements the {@link
	 * UIResource} interface, the result color will also implement this interface.
	 *
	 * @param src Source color to be blended into the destination color.
	 * @param dst Destination color on which the source color is to be blended.
	 *
	 * @return Color resulting from the color blending.
	 */
	public static Color alphaBlend(final Color src, final Color dst) {
		Color blend;

		final float srcA = (float) src.getAlpha() / 255.0f;
		final float dstA = (float) dst.getAlpha() / 255.0f;
		final float outA = srcA + dstA * (1 - srcA);

		if (outA > 0) {
			final float outR = ((float) src.getRed() * srcA + (float) dst.getRed() * dstA * (1.0f - srcA)) / outA;
			final float outG = ((float) src.getGreen() * srcA + (float) dst.getGreen() * dstA * (1.0f - srcA)) / outA;
			final float outB = ((float) src.getBlue() * srcA + (float) dst.getBlue() * dstA * (1.0f - srcA)) / outA;
			blend = new Color((int) outR, (int) outG, (int) outB, (int) (outA * 255.0f));
		} else {
			blend = new Color(0, 0, 0, 0);
		}

		if ((src instanceof UIResource) || (dst instanceof UIResource)) {
			blend = new ColorUIResource(blend);
		}

		return blend;
	}

	/**
	 * Convenience to get a more readable string representation of a Color object.
	 *
	 * @param c Color to get the string representation for.
	 *
	 * @return String representation of the specified color.
	 */
	public static String toString(final Color c) {
		final StringBuilder sb = new StringBuilder();

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
