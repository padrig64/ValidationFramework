/*
 * Copyright (c) 2012, Patrick Moawad
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.github.validationframework.experiment.builder;

import com.github.validationframework.api.validator.MappableValidator;

public class MappableValidatorBuilder {

	public static class TriggerBranch {

		/**
		 * Creates a new branch starting from the specified first trigger.
		 *
		 * @param trigger
		 * @param <T>
		 *
		 * @return
		 */
		public <T> DataProviderBranch<T> when(final T trigger) {
			// TODO
			return new DataProviderBranch<T>();
		}
	}

	public static class DataProviderBranch<T> extends TriggerBranch {

		/**
		 * Creates a new branch starting from the specified data provider.
		 *
		 * @param dataProvider
		 * @param <P>
		 *
		 * @return
		 */
		public <P> RuleBranch<T, P> checkDataFrom(final P dataProvider) {
			// TODO
			return new RuleBranch<T, P>();
		}
	}

	public static class RuleBranch<T, P> extends DataProviderBranch<T> {

		/**
		 * Creates a new branch starting from the specified rule.
		 *
		 * @param rule
		 * @param <R>
		 *
		 * @return
		 */
		public <R> ResultHandlerBranch<T, P, R> with(final R rule) {
			// TODO
			return new ResultHandlerBranch<T, P, R>();
		}
	}

	public static class ResultHandlerBranch<T, P, R> extends RuleBranch<T, P> {

		/**
		 * Creates a new leaf starting from the specified result handler.
		 *
		 * @param resultHandler
		 * @param <H>
		 *
		 * @return
		 */
		public <H> ValidatorBranch<T, P, R, H> handleResultWith(final H resultHandler) {
			// TODO
			return new ValidatorBranch<T, P, R, H>();
		}
	}

	public static class ValidatorBranch<T, P, R, H> extends ResultHandlerBranch<T, P, R> {

		/**
		 * Creates the whole validator from the whole tree.
		 *
		 * @param <R>
		 *
		 * @return
		 */
		public <R> MappableValidator<T, P, R, H> done() {
			// TODO
			return null;
		}
	}

	public static <T> DataProviderBranch<T> when(final T trigger) {
		// TODO
		return new TriggerBranch().when(trigger);
	}
}
