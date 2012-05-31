package com.github.validationframework.rule;

import java.util.ArrayList;
import java.util.List;

public class CompositeBooleanRule<D> implements Rule<D, Boolean> {

	private final List<Rule<D, Boolean>> rules = new ArrayList<Rule<D, Boolean>>();

	public void addRule(final Rule<D, Boolean> rule) {
		rules.add(rule);
	}

	public void removeRule(final Rule<D, Boolean> rule) {
		rules.remove(rule);
	}

	@Override
	public Boolean validate(final D data) {
		Boolean result = true;

		for (final Rule<D, Boolean> rule : rules) {
			result &= rule.validate(data);
		}

		return result;
	}
}
