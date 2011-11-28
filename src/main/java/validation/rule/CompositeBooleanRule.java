package validation.rule;

import java.util.ArrayList;
import java.util.List;

public class CompositeBooleanRule implements Rule<Boolean> {

	private List<Rule<Boolean>> rules = new ArrayList<Rule<Boolean>>();

	public void addRule(Rule<Boolean> rule) {
		rules.add(rule);
	}

	public void removeRule(Rule<Boolean> rule) {
		rules.remove(rule);
	}

	@Override
	public Boolean validate() {
		Boolean result = true;

		for (Rule<Boolean> rule : rules) {
			result &= rule.validate();
		}

		return result;
	}
}
