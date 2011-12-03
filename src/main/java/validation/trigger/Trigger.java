package validation.trigger;

public interface Trigger {

	public void addTriggerListener(TriggerListener listener);

	public void removeTriggerListener(TriggerListener listener);
}
