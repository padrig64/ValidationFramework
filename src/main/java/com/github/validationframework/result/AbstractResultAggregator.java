package com.github.validationframework.result;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractResultAggregator<R, A> implements ResultAggregator<A> {

	protected Map<Object, R> results = new HashMap<Object, R>();

	public void putResult(Object key, R result) {
		results.put(key, result);
	}

	public void removeResult(Object key) {
		results.remove(key);
	}
}
