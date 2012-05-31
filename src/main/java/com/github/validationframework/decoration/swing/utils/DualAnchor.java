package com.github.validationframework.decoration.swing.utils;

import java.awt.Dimension;
import java.awt.Point;

public class DualAnchor {

	private Anchor masterAnchor;
	private Anchor slaveAnchor;

	public DualAnchor(final Anchor masterAnchor, final Anchor slaveAnchor) {
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
