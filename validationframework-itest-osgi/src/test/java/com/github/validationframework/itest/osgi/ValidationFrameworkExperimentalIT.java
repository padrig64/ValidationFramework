package com.github.validationframework.itest.osgi;

import com.github.validationframework.api.dataprovider.DataProvider;
import com.github.validationframework.api.resulthandler.ResultHandler;
import com.github.validationframework.base.rule.string.StringRegexRule;
import com.github.validationframework.base.trigger.ManualTrigger;
import com.github.validationframework.base.validator.DefaultSimpleValidator;
import com.github.validationframework.experimental.builder.SimpleValidatorBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

import static org.mockito.Mockito.verify;
import static org.ops4j.pax.exam.CoreOptions.*;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ValidationFrameworkExperimentalIT {

    private class DataTrigger extends ManualTrigger implements DataProvider<String> {
        private String data;

        @Override
        public String getData() {
            return data;
        }

        public void fireTriggerEvent(String data) {
            this.data = data;
            super.fireTriggerEvent();
        }
    }

    @Configuration
    public Option[] config() {
        return options(
                mavenBundle("validationframework", "validationframework-core").versionAsInProject(),
                mavenBundle("validationframework", "validationframework-experimental").versionAsInProject(),
                TestOptions.junitAndMockitoBundles()
        );
    }

    @Test
    public void simpleValidatorBuilder() {
        DataTrigger dataTrigger = new DataTrigger();
        ResultHandler<Boolean> mockResultHandler = Mockito.mock(ResultHandler.class);

        DefaultSimpleValidator<String, Boolean> validator =
                SimpleValidatorBuilder
                        .on(dataTrigger)
                        .read(dataTrigger)
                        .check(new StringRegexRule("^Hello World$"))
                        .handleWith(mockResultHandler)
                        .build();

        dataTrigger.fireTriggerEvent("Hello World");

        verify(mockResultHandler).handleResult(Boolean.TRUE);

        dataTrigger.fireTriggerEvent("Goodbye World");

        verify(mockResultHandler).handleResult(Boolean.FALSE);

        validator.dispose();
    }

}
