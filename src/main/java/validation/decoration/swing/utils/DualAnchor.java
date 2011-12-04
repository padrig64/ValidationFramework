package validation.decoration.swing.utils;

import java.awt.Component;
import java.awt.Point;

import javax.swing.Icon;

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

	public Point getRelativeSlaveLocation(Component master, Icon slave) {

		Point masterAnchorPoint = masterAnchor.getAnchorPoint(master);
		Point slaveAnchorPoint = slaveAnchor.getAnchorPoint(slave);

		Point slaveLocation = new Point((int) (masterAnchorPoint.getX() - slaveAnchorPoint.getX()), (int) (masterAnchorPoint.getY() - slaveAnchorPoint.getY()));

		return slaveLocation;
	}
}
