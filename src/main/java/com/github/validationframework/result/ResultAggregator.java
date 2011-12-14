package com.github.validationframework.result;

public interface ResultAggregator<R, A> {

	public A aggregateResults(R results);
}
