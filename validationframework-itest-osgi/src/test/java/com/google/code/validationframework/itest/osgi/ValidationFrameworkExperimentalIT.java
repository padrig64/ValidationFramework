package com.google.code.validationframework.itest.osgi;

import com.google.code.validationframework.api.dataprovider.DataProvider;
import com.google.code.validationframework.api.resulthandler.ResultHandler;
import com.google.code.validationframework.base.rule.string.StringRegexRule;
import com.google.code.validationframework.base.trigger.ManualTrigger;
import com.google.code.validationframework.base.validator.generalvalidator.GeneralValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerMethod;

import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;
import static org.mockito.Mockito.verify;
import static org.ops4j.pax.exam.CoreOptions.mavenBundle;
import static org.ops4j.pax.exam.CoreOptions.options;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerMethod.class)
public class ValidationFrameworkExperimentalIT {

    private class DataTrigger extends ManualTrigger implements DataProvider<String> {
        private String data;

        @Override
        public String getData() {
            return data;
        }

        public void trigger(String data) {
            this.data = data;
            super.trigger();
        }
    }

    @Configuration
    public Option[] config() {
        return options(mavenBundle("com.google.code.validationframework",
                "validationframework-core").versionAsInProject(), mavenBundle("com.google.code.validationframework",
                "validationframework-experimental").versionAsInProject(), TestOptions.junitAndMockitoBundles());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void simpleValidatorBuilder() {
        DataTrigger dataTrigger = new DataTrigger();
        ResultHandler<Boolean> mockResultHandler = Mockito.mock(ResultHandler.class);

        GeneralValidator<String, String, Boolean, Boolean> validator = on(dataTrigger) //
                .read(dataTrigger) //
                .check(new StringRegexRule("^Hello World$")) //
                .handleWith(mockResultHandler) //
                .getValidator();

        dataTrigger.trigger("Hello World");

        verify(mockResultHandler).handleResult(Boolean.TRUE);

        dataTrigger.trigger("Goodbye World");

        verify(mockResultHandler).handleResult(Boolean.FALSE);

        validator.dispose();
    }

}
