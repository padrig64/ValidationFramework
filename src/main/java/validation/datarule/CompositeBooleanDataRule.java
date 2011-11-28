package validation.datarule;

import java.util.ArrayList;
import java.util.List;

public class CompositeBooleanDataRule<D> implements DataRule<D, Boolean> {

	private List<DataRule<D, Boolean>> rules = new ArrayList<DataRule<D, Boolean>>();

	public void addRule(DataRule<D, Boolean> rule) {
		rules.add(rule);
	}

	public void removeRule(DataRule<D, Boolean> rule) {
		rules.remove(rule);
	}

	@Override
	public Boolean validate(D data) {
		Boolean result = true;

		for (DataRule<D, Boolean> rule : rules) {
			result &= rule.validate(data);
		}

		return result;
	}
}
