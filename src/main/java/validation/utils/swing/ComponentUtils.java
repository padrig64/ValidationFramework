package validation.utils.swing;

import java.awt.Component;
import java.awt.Point;

import validation.decoration.swing.DualAnchor;

public class ComponentUtils {

	private ComponentUtils() {
		// Nothing to be done
	}

	public static Point getSlaveLocation(Component master, Component slave, DualAnchor dualAnchor) {
		Point slaveLoc = new Point(0, 0);

		if (dualAnchor != null) {
			// TODO
//			Point masterPoint = dualAnchor.getMasterAnchor().getComponentPoint(master);
//			Point slavePoint = dualAnchor.getSlaveAnchor().getComponentPoint(slave);
		}

		return slaveLoc;
	}
}
