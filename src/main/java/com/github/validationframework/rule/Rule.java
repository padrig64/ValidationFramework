package com.github.validationframework.rule;

public interface Rule<I, R> {

	public R validate(I input);
}
