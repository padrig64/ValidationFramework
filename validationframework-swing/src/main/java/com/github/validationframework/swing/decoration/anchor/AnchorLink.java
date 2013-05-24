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

package com.github.validationframework.swing.decoration.anchor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;

/**
 * Pair of anchors.<br>Typically, this can be used to match an anchor point on a decorated component (master anchor) to
 * an anchor point on its decoration (slave anchor).
 */
public class AnchorLink {

    /**
     * Anchor to which the other anchor will slaved.
     */
    private Anchor masterAnchor;

    /**
     * Anchor that will be slaved to the master anchor.
     */
    private Anchor slaveAnchor;

    /**
     * Constructor specifying the anchor link to get the master and slave anchors from.
     *
     * @param anchorLink Anchor link to get the master and slave anchors from.
     */
    public AnchorLink(final AnchorLink anchorLink) {
        this(new Anchor(anchorLink.getMasterAnchor()), new Anchor(anchorLink.getSlaveAnchor()));
    }

    /**
     * Constructor specifying the master and slave anchors.
     *
     * @param masterAnchor Master anchor.
     * @param slaveAnchor  Slave anchor.
     */
    public AnchorLink(final Anchor masterAnchor, final Anchor slaveAnchor) {
        this.masterAnchor = masterAnchor;
        this.slaveAnchor = slaveAnchor;
    }

    /**
     * Gets the master anchor.
     *
     * @return Master anchor.
     */
    public Anchor getMasterAnchor() {
        return masterAnchor;
    }

    /**
     * Sets the master anchor.
     *
     * @param masterAnchor Master anchor.
     */
    public void setMasterAnchor(final Anchor masterAnchor) {
        this.masterAnchor = masterAnchor;
    }

    /**
     * Gets the slave anchor.
     *
     * @return Slave anchor.
     */
    public Anchor getSlaveAnchor() {
        return slaveAnchor;
    }

    /**
     * Sets the slave anchor.
     *
     * @param slaveAnchor Slave anchor.
     */
    public void setSlaveAnchor(final Anchor slaveAnchor) {
        this.slaveAnchor = slaveAnchor;
    }

    /**
     * Computes the location of the specified component that is slaved to the specified master component using this
     * anchor link.
     *
     * @param masterComponent Master component to which the other component is slaved.
     * @param slaveComponent  Slave component that is slaved to the master component.
     *
     * @return Location where the slave component should be.
     */
    public Point getRelativeSlaveLocation(final Component masterComponent, final Component slaveComponent) {
        return getRelativeSlaveLocation(masterComponent.getWidth(), masterComponent.getHeight(),
                slaveComponent.getWidth(), slaveComponent.getHeight());
    }

    /**
     * Computes the location of the slave component (whose size is specified) that is slaved to the master component
     * (whose size is specified) using this anchor link.
     *
     * @param masterSize Size of the master component to which the other component is slaved.
     * @param slaveSize  Size of the slave component that is slaved to the master component.
     *
     * @return Location where the slave component should be.
     */
    public Point getRelativeSlaveLocation(final Dimension masterSize, final Dimension slaveSize) {
        return getRelativeSlaveLocation(masterSize.width, masterSize.height, slaveSize.width, slaveSize.height);
    }

    /**
     * Computes the location of the slave component (whose size is specified) that is slaved to the master component
     * (whose size is specified) using this anchor link.
     *
     * @param masterWidth  Width of the master component to which the other component is slaved.
     * @param masterHeight Height of the master component to which the other component is slaved.
     * @param slaveWidth   Width of the slave component that is slaved to the master component.
     * @param slaveHeight  Height of the slave component that is slaved to the master component.
     *
     * @return Location where the slave component should be.
     */
    public Point getRelativeSlaveLocation(final int masterWidth, final int masterHeight, final int slaveWidth,
                                          final int slaveHeight) {
        final Point masterAnchorPoint = masterAnchor.getAnchorPoint(masterWidth, masterHeight);
        final Point slaveAnchorPoint = slaveAnchor.getAnchorPoint(slaveWidth, slaveHeight);

        return new Point((int) (masterAnchorPoint.getX() - slaveAnchorPoint.getX()), (int) (masterAnchorPoint.getY() - slaveAnchorPoint.getY()));
    }
}
