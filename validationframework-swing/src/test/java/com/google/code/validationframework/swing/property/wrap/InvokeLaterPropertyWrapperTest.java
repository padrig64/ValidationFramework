/*
 * Copyright (c) 2015, ValidationFramework Authors
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

package com.google.code.validationframework.swing.property.wrap;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import com.google.code.validationframework.base.property.simple.SimpleIntegerProperty;
import com.google.code.validationframework.base.transform.OrBooleanAggregator;
import org.junit.Test;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static com.google.code.validationframework.base.binding.Binder.read;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @see InvokeLaterPropertyWrapper
 */
@Deprecated
public class InvokeLaterPropertyWrapperTest {

    @Test
    public void testNoEventFired() throws InterruptedException {
        final CountDownLatch finished = new CountDownLatch(1);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ReadableWritableProperty<Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
                ReadableWritableProperty<Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);
                ReadableWritableProperty<Boolean> orRolloverProperty = new SimpleBooleanProperty(false);
                read(rolloverProperty1, rolloverProperty2).transform(new OrBooleanAggregator()).write
                        (orRolloverProperty);

                final ReadableWritableProperty<Boolean> globalRolloverProperty = new SimpleBooleanProperty
                        (false);
                read(new InvokeLaterPropertyWrapper<Boolean>(orRolloverProperty)).write(globalRolloverProperty);

                final ValueChangeListener<Boolean> rolloverListener = mock(ValueChangeListener.class);
                globalRolloverProperty.addValueChangeListener(rolloverListener);

                rolloverProperty1.setValue(true);
                assertTrue(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());
                rolloverProperty1.setValue(false);
                assertFalse(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());
                rolloverProperty2.setValue(true);
                assertTrue(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());
                rolloverProperty2.setValue(false);
                assertFalse(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        assertFalse(globalRolloverProperty.getValue());
                        verifyZeroInteractions(rolloverListener);

                        finished.countDown();
                    }
                });
            }
        });

        finished.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void testEventsFired() throws InterruptedException {
        final CountDownLatch finished = new CountDownLatch(1);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                final ReadableWritableProperty<Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
                final ReadableWritableProperty<Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);
                final ReadableWritableProperty<Boolean> orRolloverProperty = new SimpleBooleanProperty(false);
                read(rolloverProperty1, rolloverProperty2).transform(new OrBooleanAggregator()).write
                        (orRolloverProperty);

                final ReadableWritableProperty<Boolean> globalRolloverProperty = new SimpleBooleanProperty
                        (false);
                read(new InvokeLaterPropertyWrapper<Boolean>(orRolloverProperty)).write(globalRolloverProperty);

                final ValueChangeListener<Boolean> rolloverListener = mock(ValueChangeListener.class);
                globalRolloverProperty.addValueChangeListener(rolloverListener);

                rolloverProperty1.setValue(true);
                assertTrue(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());

                rolloverProperty1.setValue(false);
                assertFalse(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());

                rolloverProperty2.setValue(true);
                assertTrue(orRolloverProperty.getValue());
                assertFalse(globalRolloverProperty.getValue());

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        assertTrue(globalRolloverProperty.getValue());

                        rolloverProperty2.setValue(false);
                        assertFalse(orRolloverProperty.getValue());
                        assertTrue(globalRolloverProperty.getValue());

                        rolloverProperty1.setValue(true);
                        assertTrue(orRolloverProperty.getValue());
                        assertTrue(globalRolloverProperty.getValue());

                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                assertTrue(globalRolloverProperty.getValue());

                                rolloverProperty1.setValue(false);
                                assertFalse(orRolloverProperty.getValue());
                                assertTrue(globalRolloverProperty.getValue());

                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        assertFalse(globalRolloverProperty.getValue());

                                        verify(rolloverListener, times(1))
                                                .valueChanged(globalRolloverProperty, false, true);
                                        verify(rolloverListener, times(1))
                                                .valueChanged(globalRolloverProperty, true, false);
                                        verifyNoMoreInteractions(rolloverListener);

                                        finished.countDown();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

        finished.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void defaultDeepDispose() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final SimpleIntegerProperty wrapped = mock(SimpleIntegerProperty.class);
                InvokeLaterPropertyWrapper<Integer> wrapper = new InvokeLaterPropertyWrapper<Integer>(wrapped);

                assertTrue(wrapper.getDeepDispose());

                wrapper.dispose();
                wrapper.dispose();
                wrapper.dispose();

                verify(wrapped).dispose();
            }
        });
    }

    @Test
    public void noEventAfterDefaultDeepDispose() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final SimpleIntegerProperty wrapped = new SimpleIntegerProperty();
                InvokeLaterPropertyWrapper<Integer> wrapper = new InvokeLaterPropertyWrapper<Integer>(wrapped);
                wrapper.addValueChangeListener(new ValueChangeListener<Integer>() {
                    @Override
                    public void valueChanged(ReadableProperty<? extends Integer> property, Integer oldValue, Integer newValue) {
                        // Should not happen
                        assertTrue(false);
                    }
                });

                assertTrue(wrapper.getDeepDispose());

                wrapper.dispose();

                // Modify wrapped value and check that no event is fired by the wrapper
                wrapped.setValue(10);
            }
        });
    }

    @Test
    public void setDeepDispose() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                SimpleIntegerProperty wrapped = mock(SimpleIntegerProperty.class);
                InvokeLaterPropertyWrapper<Integer> wrapper = new InvokeLaterPropertyWrapper<Integer>(wrapped);
                wrapper.setDeepDispose(true);

                assertTrue(wrapper.getDeepDispose());

                wrapper.dispose();
                wrapper.dispose();
                wrapper.dispose();

                verify(wrapped).dispose();
            }
        });
    }

    @Test
    public void noEventAfterSetDeepDispose() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final SimpleIntegerProperty wrapped = new SimpleIntegerProperty();
                InvokeLaterPropertyWrapper<Integer> wrapper = new InvokeLaterPropertyWrapper<Integer>(wrapped);
                wrapper.setDeepDispose(true);
                wrapper.addValueChangeListener(new ValueChangeListener<Integer>() {
                    @Override
                    public void valueChanged(ReadableProperty<? extends Integer> property, Integer oldValue, Integer newValue) {
                        // Should not happen
                        assertTrue(false);
                    }
                });

                assertTrue(wrapper.getDeepDispose());

                wrapper.dispose();

                // Modify wrapped value and check that no event is fired by the wrapper
                wrapped.setValue(10);
            }
        });
    }

    @Test
    public void setShallowDispose() throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                SimpleIntegerProperty wrapped = mock(SimpleIntegerProperty.class);
                InvokeLaterPropertyWrapper<Integer> wrapper = new InvokeLaterPropertyWrapper<Integer>(wrapped);
                wrapper.setDeepDispose(false);

                assertFalse(wrapper.getDeepDispose());

                wrapper.dispose();
                wrapper.dispose();
                wrapper.dispose();

                verify(wrapped, times(0)).dispose();
            }
        });
    }

    @Test
    public void noEventAfterSetShallowDispose() throws InterruptedException, InvocationTargetException {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final SimpleIntegerProperty wrapped = new SimpleIntegerProperty();
                InvokeLaterPropertyWrapper<Integer> wrapper = new InvokeLaterPropertyWrapper<Integer>(wrapped);
                wrapper.setDeepDispose(false);
                wrapper.addValueChangeListener(new ValueChangeListener<Integer>() {
                    @Override
                    public void valueChanged(ReadableProperty<? extends Integer> property, Integer oldValue, Integer newValue) {
                        // Should not happen
                        assertTrue(false);
                    }
                });

                assertFalse(wrapper.getDeepDispose());

                wrapper.dispose();

                // Modify wrapped value and check that no event is fired by the wrapper
                wrapped.setValue(10);
            }
        });
    }
}
