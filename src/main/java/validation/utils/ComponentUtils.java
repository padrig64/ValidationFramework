package validation.utils;

import java.awt.Component;
import java.awt.Point;

import validation.decoration.DualAnchor;

public class ComponentUtils {

	private ComponentUtils() {
		// Nothing to be done
	}

	public static Point getSlaveLocation(Component master, Component slave, DualAnchor dualAnchor) {
		Point slaveLoc = new Point(0, 0);

		if(dualAnchor != null) {
			Point masterPoint = dualAnchor.getMasterAnchor().getComponentPoint(master);
			Point slavePoint = dualAnchor.getSlaveAnchor().getComponentPoint(slave);
		}

		return slaveLoc;
	}
}
