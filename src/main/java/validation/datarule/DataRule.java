package validation.datarule;

public interface DataRule<D, R> {

	public R validate(D input);
}
