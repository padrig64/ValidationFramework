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

package com.github.validationframework.swing.trigger;

import com.github.validationframework.api.common.Disposable;
import com.github.validationframework.api.trigger.TriggerEvent;
import com.github.validationframework.base.trigger.AbstractTrigger;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Trigger that initiates the validation whenever the selection in a list changes.
 */
public class JListSelectionChangedTrigger extends AbstractTrigger implements Disposable {

	/**
	 * Listener to selection changes in the selection model of the list, triggering the validation.<br>Note that there is
	 * no need to track replacement of the selection model in the list as this is already done in {@link JList}.
	 */
	private class SelectionAdapter implements ListSelectionListener {

		/**
		 * @see ListSelectionListener#valueChanged(ListSelectionEvent)
		 */
		@Override
		public void valueChanged(final ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				fireTriggerEvent(new TriggerEvent(list));
			}
		}
	}

	/**
	 * List to track selection changes.
	 */
	private final JList list;

	/**
	 * Listener to selection changes.
	 */
	private final SelectionAdapter selectionAdapter = new SelectionAdapter();

	/**
	 * Constructor specifying the list whose selection changes are meant to trigger validation.
	 *
	 * @param list List whose selection changes are meant to trigger validation.
	 */
	public JListSelectionChangedTrigger(final JList list) {
		this.list = list;
		this.list.addListSelectionListener(selectionAdapter);
	}

	/**
	 * @see Disposable#dispose()
	 */
	@Override
	public void dispose() {
		list.removeListSelectionListener(selectionAdapter);
	}
}
