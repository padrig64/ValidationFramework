package validation.rule;

import java.util.ArrayList;
import java.util.List;

public class CompositeBooleanRule<D> implements Rule<D, Boolean> {

	private List<Rule<D, Boolean>> rules = new ArrayList<Rule<D, Boolean>>();

	public void addRule(Rule<D, Boolean> rule) {
		rules.add(rule);
	}

	public void removeRule(Rule<D, Boolean> rule) {
		rules.remove(rule);
	}

	@Override
	public Boolean validate(D data) {
		Boolean result = true;

		for (Rule<D, Boolean> rule : rules) {
			result &= rule.validate(data);
		}

		return result;
	}
}
