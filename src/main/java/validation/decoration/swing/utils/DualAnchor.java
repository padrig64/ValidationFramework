package validation.decoration.swing.utils;

import java.awt.Dimension;
import java.awt.Point;

public class DualAnchor {

	private Anchor masterAnchor;
	private Anchor slaveAnchor;

	public DualAnchor(Anchor masterAnchor, Anchor slaveAnchor) {
		this.masterAnchor = masterAnchor;
		this.slaveAnchor = slaveAnchor;
	}

	public Anchor getMasterAnchor() {
		return masterAnchor;
	}

	public void setMasterAnchor(Anchor masterAnchor) {
		this.masterAnchor = masterAnchor;
	}

	public Anchor getSlaveAnchor() {
		return slaveAnchor;
	}

	public void setSlaveAnchor(Anchor slaveAnchor) {
		this.slaveAnchor = slaveAnchor;
	}

	public Point getRelativeSlaveLocation(Dimension masterSize, Dimension slaveSize) {
		return getRelativeSlaveLocation(masterSize.width, masterSize.height, slaveSize.width, slaveSize.height);
	}

	public Point getRelativeSlaveLocation(int masterWidth, int masterHeight, int slaveWidth, int slaveHeight) {
		Point masterAnchorPoint = masterAnchor.getAnchorPoint(masterWidth, masterHeight);
		Point slaveAnchorPoint = slaveAnchor.getAnchorPoint(slaveWidth, slaveHeight);

		Point slaveLocation = new Point((int) (masterAnchorPoint.getX() - slaveAnchorPoint.getX()), (int) (masterAnchorPoint.getY() - slaveAnchorPoint.getY()));

		return slaveLocation;
	}
}
