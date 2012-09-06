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

package com.github.validationframework.experimental.resulthandler;

import com.github.validationframework.api.trigger.Trigger;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.api.trigger.TriggerListener;
import com.github.validationframework.swing.resulthandler.AbstractIconTipFeedback;
import javax.swing.JComponent;

public class IconTipFeedbackOnTrigger<O> {

	private class TriggerAdapter implements TriggerListener {

		@Override
		public void triggerValidation(final TriggerEvent event) {
			System.out.println("IconTipFeedbackOnTrigger$TriggerAdapter.triggerValidation: " + event.getSource());
			final Object source = event.getSource();
			if ((source instanceof JComponent) && !source.equals(feedback.getDecoratedComponent())) {
				System.out.println(" |_ RE-ATTACH!");
				// Re-attach decoration to new component
				feedback.detach();
				feedback.attach((JComponent) source);
			}
		}
	}

	private final TriggerListener triggerAdapter = new TriggerAdapter();

	private final AbstractIconTipFeedback<O> feedback;

	public IconTipFeedbackOnTrigger(final AbstractIconTipFeedback<O> feedback) {
		this.feedback = feedback;
	}

	public void addTrigger(final Trigger trigger) {
		if (trigger != null) {
			trigger.addTriggerListener(triggerAdapter);
		}
	}

	public void removeTrigger(final Trigger trigger) {
		if (trigger != null) {
			trigger.removeTriggerListener(triggerAdapter);
		}
	}

	public void addTriggers(final Trigger... triggers) {
		if (triggers != null) {
			for (final Trigger trigger : triggers) {
				addTrigger(trigger);
			}
		}
	}

	public void removeTriggers(final Trigger... triggers) {
		if (triggers != null) {
			for (final Trigger trigger : triggers) {
				removeTrigger(trigger);
			}
		}
	}
}
