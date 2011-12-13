package com.github.validationframework.result;

public interface ResultAggregator<R, A> {

	public R aggregateResults(AggregatableResult<R>... results);
}