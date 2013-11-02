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

package com.google.code.validationframework.javafx.decoration;

import javafx.geometry.Point2D;
import javafx.scene.Node;

/**
 * Pair of anchors.
 * <p/>
 * Typically, this can be used to match an anchor point on a decorated node (master anchor) to an anchor point on its
 * decoration (slave anchor).
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
    public AnchorLink(AnchorLink anchorLink) {
        this(new Anchor(anchorLink.getMasterAnchor()), new Anchor(anchorLink.getSlaveAnchor()));
    }

    /**
     * Constructor specifying the master and slave anchors.
     *
     * @param masterAnchor Master anchor.
     * @param slaveAnchor  Slave anchor.
     */
    public AnchorLink(Anchor masterAnchor, Anchor slaveAnchor) {
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
    public void setMasterAnchor(Anchor masterAnchor) {
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
    public void setSlaveAnchor(Anchor slaveAnchor) {
        this.slaveAnchor = slaveAnchor;
    }

    /**
     * Computes the location of the specified node that is slaved to the specified master node using this anchor link.
     *
     * @param masterComponent Master node to which the other node is slaved.
     * @param slaveComponent  Slave node that is slaved to the master node.
     *
     * @return Location where the slave node should be.
     */
    public Point2D getRelativeSlaveLocation(Node masterComponent, Node slaveComponent) {
        return getRelativeSlaveLocation(masterComponent.getBoundsInParent().getWidth(),
                masterComponent.getBoundsInParent().getHeight(), slaveComponent.getBoundsInParent().getWidth(),
                slaveComponent.getBoundsInParent().getHeight());
    }

//    /**
//     * Computes the location of the slave node (whose size is specified) that is slaved to the master node (whose size
//     * is specified) using this anchor link.
//     *
//     * @param masterSize Size of the master node to which the other node is slaved.
//     * @param slaveSize  Size of the slave node that is slaved to the master node.
//     *
//     * @return Location where the slave node should be.
//     */
//    public Point getRelativeSlaveLocation(Dimension2D masterSize, Dimension2D slaveSize) {
//        return getRelativeSlaveLocation(masterSize.getWidth(), masterSize.getHeight(), slaveSize.getWidth(),
//                slaveSize.getHeight());
//    }

    /**
     * Computes the location of the slave node (whose size is specified) that is slaved to the master node
     * (whose size is specified) using this anchor link.
     *
     * @param masterWidth  Width of the master node to which the other node is slaved.
     * @param masterHeight Height of the master node to which the other node is slaved.
     * @param slaveWidth   Width of the slave node that is slaved to the master node.
     * @param slaveHeight  Height of the slave node that is slaved to the master node.
     *
     * @return Location where the slave node should be.
     */
    public Point2D getRelativeSlaveLocation(double masterWidth, double masterHeight, double slaveWidth,
                                            double slaveHeight) {
        Point2D masterAnchorPoint = masterAnchor.getAnchorPoint(masterWidth, masterHeight);
        Point2D slaveAnchorPoint = slaveAnchor.getAnchorPoint(slaveWidth, slaveHeight);

        return new Point2D(masterAnchorPoint.getX() - slaveAnchorPoint.getX(),
                masterAnchorPoint.getY() - slaveAnchorPoint.getY());
    }
}
