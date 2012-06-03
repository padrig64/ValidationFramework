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

package com.github.validationframework.decoration.swing.utils;

import java.awt.Dimension;
import java.awt.Point;

public class Anchor {

	public static final Anchor TOP_LEFT = new Anchor(0.0f, 0.0f);
	public static final Anchor TOP_RIGHT = new Anchor(1.0f, 0.0f);
	public static final Anchor BOTTOM_LEFT = new Anchor(0.0f, 1.0f);
	public static final Anchor BOTTOM_RIGHT = new Anchor(1.0f, 1.0f);
	public static final Anchor CENTER = new Anchor(0.5F, 0.5f);

	private final float relativeX;
	private final float relativeY;

	private final int offsetX;
	private final int offsetY;

	public Anchor(final float relativeX, final float relativeY) {
		this(relativeX, 0, relativeY, 0);
	}

	public Anchor(final float relativeX, final int offsetX, final float relativeY, final int offsetY) {
		this.relativeX = relativeX;
		this.offsetX = offsetX;
		this.relativeY = relativeY;
		this.offsetY = offsetY;
	}

	public float getRelativeX() {
		return relativeX;
	}

	public float getRelativeY() {
		return relativeY;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public Point getAnchorPoint(final Dimension size) {
		return getAnchorPoint(size.width, size.height);
	}

	public Point getAnchorPoint(final int width, final int height) {
		return new Point((int) (relativeX * width + offsetX), (int) (relativeY * height + offsetY));
	}
}
