/*
 * Copyright (c) 2013, Patrick Moawad
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

package com.google.code.validationframework.swing.decoration.anchor;

import java.awt.Dimension;
import java.awt.Point;

/**
 * Entity representing an anchor.<br>An anchor is, for instance, a virtual point on a component relatively to its
 * bounds, and can be used as the point to attach decoration.
 */
public class Anchor {

    /**
     * Convenience anchor representing a point at the top left hand corner.
     */
    public static final Anchor TOP_LEFT = new Anchor(0.0f, 0.0f);

    /**
     * Convenience anchor representing a point at the top right hand corner.
     */
    public static final Anchor TOP_RIGHT = new Anchor(1.0f, 0.0f);

    /**
     * Convenience anchor representing a point at the bottom left hand corner.
     */
    public static final Anchor BOTTOM_LEFT = new Anchor(0.0f, 1.0f);

    /**
     * Convenience anchor representing a point at the bottom right hand corner.
     */
    public static final Anchor BOTTOM_RIGHT = new Anchor(1.0f, 1.0f);

    /**
     * Convenience anchor representing a point at the center.
     */
    public static final Anchor CENTER = new Anchor(0.5F, 0.5f);

    /**
     * Relative position in percentage relatively to the origin and length on the X axis.<br>For instance,
     * on a component whose bounds are defined by the rectangle [X,Y,W,H], a value of 0.0f would represent X,
     * and a value of 1.0f would represent X+W.
     */
    private final float relativeX;

    /**
     * Relative position in percentage relatively to the origin and length on the Y axis.<br>For instance,
     * on a component whose bounds are defined by the rectangle [X,Y,W,H], a value of 0.0f would represent Y,
     * and a value of 1.0f would represent Y+H.
     */
    private final float relativeY;

    /**
     * Offset in pixels on the X axis, relatively to {@link #relativeX}.
     */
    private final int offsetX;

    /**
     * Offset in pixels on the Y axis, relatively to {@link #relativeY}.
     */
    private final int offsetY;

    /**
     * Constructor specifying an anchor to copy from.
     *
     * @param anchor Anchor to copy from.
     */
    public Anchor(final Anchor anchor) {
        this(anchor.getRelativeX(), anchor.getOffsetX(), anchor.getRelativeY(), anchor.getOffsetY());
    }

    /**
     * Anchor specifying the relative positions on the X axis and Y axis.
     *
     * @param relativeX Relative position on the X axis.
     * @param relativeY Relative position on the Y axis.
     *
     * @see #relativeX
     * @see #relativeY
     */
    public Anchor(final float relativeX, final float relativeY) {
        this(relativeX, 0, relativeY, 0);
    }

    /**
     * Anchor specifying the relative positions and offsets on the X axis and Y axis.
     *
     * @param relativeX Relative position on the X axis.
     * @param offsetX   Position offset on the X axis.
     * @param relativeY Relative position on the Y axis.
     * @param offsetY   Position offset on the Y axis.
     *
     * @see #relativeX
     * @see #offsetX
     * @see #relativeY
     * @see #offsetY
     */
    public Anchor(final float relativeX, final int offsetX, final float relativeY, final int offsetY) {
        this.relativeX = relativeX;
        this.offsetX = offsetX;
        this.relativeY = relativeY;
        this.offsetY = offsetY;
    }

    /**
     * Gets the relative position in percentage relatively to the origin and length on the X axis.
     */
    public float getRelativeX() {
        return relativeX;
    }

    /**
     * Gets the relative position in percentage relatively to the origin and length on the Y axis.
     */
    public float getRelativeY() {
        return relativeY;
    }

    /**
     * Gets the offset in pixels on the X axis, relatively to {@link #relativeX}.
     */
    public int getOffsetX() {
        return offsetX;
    }

    /**
     * Gets the offset in pixels on the Y axis, relatively to {@link #relativeY}.
     */
    public int getOffsetY() {
        return offsetY;
    }

    /**
     * Retrieves a point on an object of the specified size.
     *
     * @param size Size of the object to get the anchor point for.
     *
     * @return Anchor point.
     *
     * @see #getAnchorPoint(int, int)
     */
    public Point getAnchorPoint(final Dimension size) {
        return getAnchorPoint(size.width, size.height);
    }

    /**
     * Retrieves a point on an object of the specified size.
     *
     * @param width  Width of the object to get the anchor point for.
     * @param height Height of the object to get the anchor point for.
     *
     * @return Anchor point.
     *
     * @see #getAnchorPoint(Dimension)
     */
    public Point getAnchorPoint(final int width, final int height) {
        return new Point((int) (relativeX * width + offsetX), (int) (relativeY * height + offsetY));
    }
}
