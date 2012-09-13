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

package com.github.moawad.validationframework.swing.decoration.anchor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

/**
 * Pair of anchors.<br>Typically, this can be used to match an anchor point on a decorated component (master anchor) to
 * an anchor point on its decoration (slave anchor).
 */
public class AnchorLink {

	private Anchor masterAnchor;
	private Anchor slaveAnchor;

	public AnchorLink(final AnchorLink anchorLink) {
		this(new Anchor(anchorLink.getMasterAnchor()), new Anchor(anchorLink.getSlaveAnchor()));
	}

	public AnchorLink(final Anchor masterAnchor, final Anchor slaveAnchor) {
		this.masterAnchor = masterAnchor;
		this.slaveAnchor = slaveAnchor;
	}

	public Anchor getMasterAnchor() {
		return masterAnchor;
	}

	public void setMasterAnchor(final Anchor masterAnchor) {
		this.masterAnchor = masterAnchor;
	}

	public Anchor getSlaveAnchor() {
		return slaveAnchor;
	}

	public void setSlaveAnchor(final Anchor slaveAnchor) {
		this.slaveAnchor = slaveAnchor;
	}

	public Point getRelativeSlaveLocation(final Component masterComponent, final Component slaveComponent) {
		return getRelativeSlaveLocation(masterComponent.getWidth(), masterComponent.getHeight(),
										slaveComponent.getWidth(), slaveComponent.getHeight());
	}

	public Point getRelativeSlaveLocation(final Dimension masterSize, final Dimension slaveSize) {
		return getRelativeSlaveLocation(masterSize.width, masterSize.height, slaveSize.width, slaveSize.height);
	}

	public Point getRelativeSlaveLocation(final int masterWidth, final int masterHeight, final int slaveWidth,
										  final int slaveHeight) {
		final Point masterAnchorPoint = masterAnchor.getAnchorPoint(masterWidth, masterHeight);
		final Point slaveAnchorPoint = slaveAnchor.getAnchorPoint(slaveWidth, slaveHeight);

		return new Point((int) (masterAnchorPoint.getX() - slaveAnchorPoint.getX()),
						 (int) (masterAnchorPoint.getY() - slaveAnchorPoint.getY()));
	}
}
