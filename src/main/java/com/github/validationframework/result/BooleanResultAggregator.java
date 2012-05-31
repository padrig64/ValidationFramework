package com.github.validationframework.result;

public class BooleanResultAggregator extends AbstractResultAggregator<Boolean, Boolean> {

	@Override
	public Boolean getAggregatedResult() {
		boolean aggregatedResult = false;

		if (!results.isEmpty()) {
			aggregatedResult = true;
			for (final Boolean result : results.values()) {
				aggregatedResult &= result;
			}
		}

		return aggregatedResult;
	}
}
