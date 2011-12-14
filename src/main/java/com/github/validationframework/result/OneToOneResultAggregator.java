package com.github.validationframework.result;

public class OneToOneResultAggregator<R> implements ResultAggregator<R, R> {

	@Override
	public R aggregateResults(R results) {
		return results;
	}
}
