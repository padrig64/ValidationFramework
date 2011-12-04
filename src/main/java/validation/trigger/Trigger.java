package validation.trigger;

public interface Trigger<D> {

	public void addTriggerListener(TriggerListener<D> listener);

	public void removeTriggerListener(TriggerListener<D> listener);
}
