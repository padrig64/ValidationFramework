package com.github.validationframework.rule;

import java.util.ArrayList;
import java.util.List;

public class CompositeBooleanRule<I> implements Rule<I, Boolean> {

	private final List<Rule<I, Boolean>> rules = new ArrayList<Rule<I, Boolean>>();

	public void addRule(final Rule<I, Boolean> rule) {
		rules.add(rule);
	}

	public void removeRule(final Rule<I, Boolean> rule) {
		rules.remove(rule);
	}

	@Override
	public Boolean validate(final I input) {
		Boolean result = true;

		for (final Rule<I, Boolean> rule : rules) {
			result &= rule.validate(input);
		}

		return result;
	}
}
