package com.github.validationframework.result;

public class BooleanResultAggregator implements ResultAggregator<Boolean[], Boolean> {

	@Override
	public Boolean aggregateResults(Boolean... results) {
		boolean aggregatedResult = true;

		for (Boolean result : results) {
			aggregatedResult &= result;
		}

		return aggregatedResult;
	}
}
