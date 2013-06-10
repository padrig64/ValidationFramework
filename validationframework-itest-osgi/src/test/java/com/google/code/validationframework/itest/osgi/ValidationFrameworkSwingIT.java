package com.google.code.validationframework.itest.osgi;

import com.google.code.validationframework.base.rule.string.StringRegexRule;
import com.google.code.validationframework.base.validator.DefaultSimpleValidator;
import com.google.code.validationframework.experimental.builder.SimpleValidatorBuilder;
import com.google.code.validationframework.swing.dataprovider.JTextFieldTextProvider;
import com.google.code.validationframework.swing.resulthandler.bool.ComponentEnablingBooleanResultHandler;
import com.google.code.validationframework.swing.trigger.JTextFieldDocumentChangedTrigger;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.concurrent.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;
import static org.ops4j.pax.exam.CoreOptions.wrappedBundle;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ValidationFrameworkSwingIT {
    @Configuration
    public Option[] config() {
        return options(
                mavenBundle("com.google.code.validationframework", "validationframework-core").versionAsInProject(),
                mavenBundle("com.google.code.validationframework", "validationframework-experimental").versionAsInProject(),
                mavenBundle("com.google.code.validationframework", "validationframework-swing").versionAsInProject(),
                wrappedBundle(mavenBundle("net.java.timingframework", "timingframework-core").versionAsInProject()),
                wrappedBundle(mavenBundle("net.java.timingframework", "timingframework-swing").versionAsInProject()),
                mavenBundle("net.java.dev.jna", "jna").versionAsInProject(),
                wrappedBundle(mavenBundle("net.java.dev.jna", "platform").versionAsInProject()),
                TestOptions.junitAndMockitoBundles()
         );
    }

    private class TestFrame extends JFrame {

        private final JTextField textField;
        private final JButton button;
        private final DefaultSimpleValidator<String,Boolean> validator;

        public TestFrame() {
            textField = new JTextField(20);

            getContentPane().add(textField);
            button = new JButton("Submit");
            button.setEnabled(false);
            getContentPane().add(button);

            setLayout(new FlowLayout());
            pack();
            setVisible(true);

            textField.grabFocus();

            validator = SimpleValidatorBuilder
                    .on(new JTextFieldDocumentChangedTrigger(textField))
                    .read(new JTextFieldTextProvider(textField))
                    .check(new StringRegexRule("^Hello World$"))
                    .handleWith(new ComponentEnablingBooleanResultHandler(button))
                    .build();
        }

    }

    @Test
    public void swingInputAndResultHandling() throws Exception {
        // Assume a non-headless environment
        Assume.assumeTrue(!GraphicsEnvironment.isHeadless());

        // Create TestFrame on EDT and obtain it
        RunnableFuture<TestFrame> testFrameFuture = new FutureTask<TestFrame>(new Callable<TestFrame>() {
            @Override
            public TestFrame call() throws Exception {
                return new TestFrame();
            }
        });
        SwingUtilities.invokeLater(testFrameFuture);
        final TestFrame testFrame = testFrameFuture.get(10,TimeUnit.SECONDS);

        // Simulate the string as input to the textField inside the test frame
        for (final char c : "Hello World".toCharArray()) {
            // Assert that the button on the TestFrame is still disabled
            RunnableFuture<Boolean> value = new FutureTask<Boolean>(new Callable<Boolean>() {
                @Override
                public Boolean call() throws Exception {
                    return testFrame.button.isEnabled();
                }
            });
            SwingUtilities.invokeLater(value);
            assertFalse(value.get(10,TimeUnit.SECONDS));

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    testFrame.textField.dispatchEvent(new KeyEvent(testFrame.textField,
                            KeyEvent.KEY_TYPED, 0,
                            0,
                            KeyEvent.VK_UNDEFINED, c));
                }
            });
            TimeUnit.MILLISECONDS.sleep(100);
        }

        // Assert that button on the test frame is enabled after input
        RunnableFuture<Boolean> value = new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return testFrame.button.isEnabled();
            }
        });
        SwingUtilities.invokeLater(value);
        assertTrue(value.get(10,TimeUnit.SECONDS));
    }

}
