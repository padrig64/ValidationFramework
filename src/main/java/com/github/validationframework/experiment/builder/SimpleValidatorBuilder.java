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

public class SimpleValidatorBuilder {

	public static class TriggerContext {

		/**
		 * Adds the first trigger to the validator.
		 *
		 * @param trigger
		 * @param <T>
		 *
		 * @return
		 */
		public <T> DataProviderContext<T> when(final T trigger) {
			// TODO
			return new DataProviderContext<T>();
		}
	}

	public static class DataProviderContext<T> {

		/**
		 * Adds another trigger to the validator.
		 *
		 * @param trigger
		 * @param <T>
		 *
		 * @return
		 */
		public <T> DataProviderContext<T> when(final T trigger) {
			// TODO
			return new DataProviderContext<T>();
		}

		/**
		 * Adds the first data provider to the validator.
		 *
		 * @param dataProvider
		 * @param <P>
		 *
		 * @return
		 */
		public <P> RuleContext<T, P> checkDataFrom(final P dataProvider) {
			// TODO
			return new RuleContext<T, P>();
		}
	}

	public static class RuleContext<T, P> extends DataProviderContext<T> {

		/**
		 * Adds another data provider to the validator.
		 *
		 * @param dataProvider
		 * @param <P>
		 *
		 * @return
		 */
		public <P> RuleContext<T, P> checkDataFrom(final P dataProvider) {
			// TODO
			return new RuleContext<T, P>();
		}

		/**
		 * Adds the first rule to the validator.
		 *
		 * @param rule
		 * @param <R>
		 *
		 * @return
		 */
		public <R> ResultHandlerContext<T, P, R> with(final R rule) {
			// TODO
			return new ResultHandlerContext<T, P, R>();
		}
	}

	public static class ResultHandlerContext<T, P, R> extends RuleContext<T, P> {

		/**
		 * Adds another rule to the validator.
		 *
		 * @param rule
		 * @param <R>
		 *
		 * @return
		 */
		public <R> ResultHandlerContext<T, P, R> with(final R rule) {
			// TODO
			return new ResultHandlerContext<T, P, R>();
		}

		public <H> ValidatorContext<T, P, R, H> handleResultWith(final H resultHandler) {
			// TODO
			return new ValidatorContext<T, P, R, H>();
		}
	}

	public static class ValidatorContext<T, P, R, H> extends ResultHandlerContext<T, P, R> {

		public <R> MappableValidator<T, P, R, H> done() {
			// TODO
			return null;
		}
	}

	public static <T> DataProviderContext<T> when(final T trigger) {
		// TODO
		return new TriggerContext().when(trigger);
	}
}
