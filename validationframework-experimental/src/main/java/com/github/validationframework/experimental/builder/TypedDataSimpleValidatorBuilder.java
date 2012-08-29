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

package com.github.validationframework.experimental.builder;

import com.github.validationframework.api.dataprovider.TypedDataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.api.rule.TypedDataRule;
import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.base.validator.TypedDataSimpleValidator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TypedDataSimpleValidatorBuilder {

	/**
	 * Context to add the first triggers.
	 */
	public static class TriggerContext {

		/**
		 * Adds the first triggers to the validator.
		 *
		 * @param triggers Triggers to be added.
		 *
		 * @return Data provider context allowing to add triggers and data providers.
		 */
		public DataProviderContext when(final Trigger... triggers) {
			final List<Trigger> registeredTriggers = new ArrayList<Trigger>();
			if (triggers != null) {
				Collections.addAll(registeredTriggers, triggers);
			}
			return new DataProviderContext(registeredTriggers);
		}
	}

	/**
	 * Context to add more triggers and the first data providers.
	 */
	public static class DataProviderContext {

		final List<Trigger> registeredTriggers;

		public DataProviderContext(final List<Trigger> registeredTriggers) {
			this.registeredTriggers = registeredTriggers;
		}

		/**
		 * Adds more triggers to the validator.
		 *
		 * @param triggers Triggers to be added.
		 *
		 * @return Same data provider context.
		 */
		public DataProviderContext when(final Trigger... triggers) {
			if (triggers != null) {
				Collections.addAll(registeredTriggers, triggers);
			}
			return this;
		}

		/**
		 * Adds the first data providers to the validator.
		 *
		 * @param dataProviders Data providers to be added.
		 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or
		 * the type of the component itself.
		 *
		 * @return Rule context allowing to add data providers and rules, but not triggers.
		 */
		public <D> RuleContext<D> readFrom(final TypedDataProvider<D>... dataProviders) {
			final List<TypedDataProvider<D>> registeredDataProviders = new ArrayList<TypedDataProvider<D>>();
			if (dataProviders != null) {
				Collections.addAll(registeredDataProviders, dataProviders);
			}
			return new RuleContext<D>(registeredTriggers, registeredDataProviders);
		}
	}

	/**
	 * Context to add more data providers and the first rules.
	 *
	 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
	 * type of the component itself.
	 */
	public static class RuleContext<D> {

		final List<Trigger> registeredTriggers;
		final List<TypedDataProvider<D>> registeredDataProviders;

		public RuleContext(final List<Trigger> registeredTriggers,
						   final List<TypedDataProvider<D>> registeredDataProviders) {
			this.registeredTriggers = registeredTriggers;
			this.registeredDataProviders = registeredDataProviders;
		}

		/**
		 * Adds more data providers to the validator.
		 *
		 * @param dataProviders Data providers to be added.
		 *
		 * @return Same rule context.
		 */
		public RuleContext<D> readFrom(final TypedDataProvider<D>... dataProviders) {
			if (dataProviders != null) {
				Collections.addAll(registeredDataProviders, dataProviders);
			}
			return this;
		}

		/**
		 * Adds the first rules to the validator.
		 *
		 * @param rules Rules to be added.
		 * @param <O> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
		 *
		 * @return Result handler context allowing to add rules and result handlers, but not data providers.
		 */
		public <O> ResultHandlerContext<D, O> checkAgainst(final TypedDataRule<D, O>... rules) {
			final List<TypedDataRule<D, O>> registeredRules = new ArrayList<TypedDataRule<D, O>>();
			Collections.addAll(registeredRules, rules);
			return new ResultHandlerContext<D, O>(registeredTriggers, registeredDataProviders, registeredRules);
		}
	}

	/**
	 * Context to add more rules and the first result handlers.
	 *
	 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
	 * type of the component itself.
	 * @param <O> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
	 */
	public static class ResultHandlerContext<D, O> {

		final List<Trigger> registeredTriggers;
		final List<TypedDataProvider<D>> registeredDataProviders;
		final List<TypedDataRule<D, O>> registeredRules;

		public ResultHandlerContext(final List<Trigger> registeredTriggers,
									final List<TypedDataProvider<D>> registeredDataProviders,
									final List<TypedDataRule<D, O>> registeredRules) {
			this.registeredTriggers = registeredTriggers;
			this.registeredDataProviders = registeredDataProviders;
			this.registeredRules = registeredRules;
		}

		/**
		 * Adds another rule to the validator.
		 *
		 * @param rules Rule to be added.
		 *
		 * @return Same result handler context.
		 */
		public ResultHandlerContext<D, O> checkAgainst(final TypedDataRule<D, O>... rules) {
			if (rules != null) {
				Collections.addAll(registeredRules, rules);
			}
			return this;
		}

		/**
		 * Adds the first result handlers to the validator.
		 *
		 * @param resultHandlers Result handlers to be added.
		 *
		 * @return Validator context allowing to add result handlers and to create the validator.
		 */
		public ValidatorContext<D, O> handleResultWith(final ResultHandler<O>... resultHandlers) {
			final List<ResultHandler<O>> registeredResultHandlers = new ArrayList<ResultHandler<O>>();
			if (resultHandlers != null) {
				Collections.addAll(registeredResultHandlers, resultHandlers);
			}
			return new ValidatorContext<D, O>(registeredTriggers, registeredDataProviders, registeredRules,
					registeredResultHandlers);
		}
	}

	/**
	 * Context to add more result handlers and to create the validator.
	 *
	 * @param <D> Type of data to be validated.<br>It can be, for instance, the type of data handled by a component, or the
	 * type of the component itself.
	 * @param <O> Type of validation result.<br>It can be, for instance, an enumeration or just a boolean.
	 */
	public static class ValidatorContext<D, O> {

		final List<Trigger> registeredTriggers;
		final List<TypedDataProvider<D>> registeredDataProviders;
		final List<TypedDataRule<D, O>> registeredRules;
		final List<ResultHandler<O>> registeredResultHandlers;

		public ValidatorContext(final List<Trigger> registeredTriggers,
								final List<TypedDataProvider<D>> registeredDataProviders,
								final List<TypedDataRule<D, O>> registeredRules,
								final List<ResultHandler<O>> registeredResultHandlers) {
			this.registeredTriggers = registeredTriggers;
			this.registeredDataProviders = registeredDataProviders;
			this.registeredRules = registeredRules;
			this.registeredResultHandlers = registeredResultHandlers;
		}

		/**
		 * Adds more result handlers to the validator.
		 *
		 * @param resultHandlers Result handlers to be added.
		 *
		 * @return Same validator context.
		 */
		public ValidatorContext<D, O> handleResultWith(final ResultHandler<O>... resultHandlers) {
			if (resultHandlers != null) {
				Collections.addAll(registeredResultHandlers, resultHandlers);
			}
			return this;
		}

		/**
		 * Creates the validator and makes all the connections.
		 *
		 * @return Newly created and configured validator.
		 */
		public TypedDataSimpleValidator<D, O> done() {
			final TypedDataSimpleValidator<D, O> validator = new TypedDataSimpleValidator<D, O>();

			for (final Trigger trigger : registeredTriggers) {
				validator.addTrigger(trigger);
			}
			for (final TypedDataProvider<D> dataProvider : registeredDataProviders) {
				validator.addDataProvider(dataProvider);
			}
			for (final TypedDataRule<D, O> rule : registeredRules) {
				validator.addRule(rule);
			}
			for (final ResultHandler<O> resultHandler : registeredResultHandlers) {
				validator.addResultHandler(resultHandler);
			}

			return validator;
		}
	}

	public static DataProviderContext when(final Trigger... triggers) {
		return new TriggerContext().when(triggers);
	}
}
