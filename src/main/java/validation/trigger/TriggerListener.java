package validation.trigger;

public interface TriggerListener<D> {

	public void triggerValidation(D data);
}
