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

package com.github.validationframework.trigger.swing.component;

import com.github.validationframework.trigger.AbstractTrigger;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class JSliderTrigger extends AbstractTrigger<JSlider> {

	private class InputAdapter implements ChangeListener {

		@Override
		public void stateChanged(final ChangeEvent e) {
			if ((e.getSource() instanceof JSlider) && !(((JSlider) e.getSource()).getValueIsAdjusting())) {
				triggerValidation();
			}
		}
	}

	private JSlider slider = null;

	private final InputAdapter inputAdapter = new InputAdapter();

	public JSliderTrigger(final JSlider inputComponent) {
		super();
		attach(inputComponent);
	}

	public void attach(final JSlider inputComponent) {
		detach();

		slider = inputComponent;
		if (slider != null) {
			slider.addChangeListener(inputAdapter);
		}
	}

	public void detach() {
		if (slider != null) {
			slider.removeChangeListener(inputAdapter);
			slider = null;
		}
	}

	@Override
	public JSlider getInput() {
		return slider;
	}
}
