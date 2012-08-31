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

package com.github.validationframework.experimental.builder.context.simplevalidator;

import com.github.validationframework.api.trigger.Trigger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Context to add the first triggers.
 */
public class TriggerContext {

	private static final Logger LOGGER = LoggerFactory.getLogger(TriggerContext.class);

	private static final String NEW_INSTANCE_ERROR_MSG = "Failed creating instance of class: ";

	public DataProviderContext on(final Class<? extends Trigger> triggerClass) {
		Trigger trigger = null;
		try {
			trigger = triggerClass.newInstance();
		} catch (InstantiationException e) {
			LOGGER.error(NEW_INSTANCE_ERROR_MSG + triggerClass, e);
		} catch (IllegalAccessException e) {
			LOGGER.error(NEW_INSTANCE_ERROR_MSG + triggerClass, e);
		}
		return on(trigger);
	}

	public DataProviderContext on(final Trigger trigger) {
		final List<Trigger> registeredTriggers = new ArrayList<Trigger>();
		if (trigger != null) {
			registeredTriggers.add(trigger);
		}
		return new DataProviderContext(registeredTriggers);
	}

	/**
	 * Adds the first triggers to the validator.
	 *
	 * @param triggers Triggers to be added.
	 *
	 * @return Data provider context allowing to add triggers and data providers.
	 */
	public DataProviderContext on(final Trigger... triggers) {
		final List<Trigger> registeredTriggers = new ArrayList<Trigger>();
		if (triggers != null) {
			Collections.addAll(registeredTriggers, triggers);
		}
		return new DataProviderContext(registeredTriggers);
	}

	public DataProviderContext on(final Collection<Trigger> triggers) {
		final List<Trigger> registeredTriggers = new ArrayList<Trigger>();
		if (triggers != null) {
			registeredTriggers.addAll(triggers);
		}
		return new DataProviderContext(registeredTriggers);
	}
}
