package com.github.validationframework.result;

public class BooleanResultAggregator implements ResultAggregator<Boolean, Boolean> {

	@Override
	public Boolean aggregateResults(AggregatableResult<Boolean>... results) {
		boolean aggregatedResult = true;

		for (AggregatableResult<Boolean> result : results) {
			aggregatedResult &= result.getAggregatableResult();
		}

		return aggregatedResult;
	}
}
