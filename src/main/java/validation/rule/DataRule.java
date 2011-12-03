package validation.rule;

public interface DataRule<D, R> {

	public R validate(D input);
}
