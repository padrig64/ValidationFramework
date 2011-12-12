package com.github.validationframework.rule;

public interface Rule<D, R> {

	public R validate(D input);
}
