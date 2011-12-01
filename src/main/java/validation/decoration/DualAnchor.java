package validation.decoration;

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
}
