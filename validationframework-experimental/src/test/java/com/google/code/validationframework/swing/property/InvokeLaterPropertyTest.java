package com.google.code.validationframework.swing.property;

import com.google.code.validationframework.api.property.ReadableProperty;
import com.google.code.validationframework.api.property.ReadableWritableProperty;
import com.google.code.validationframework.api.property.ValueChangeListener;
import com.google.code.validationframework.base.property.simple.SimpleBooleanProperty;
import com.google.code.validationframework.base.transform.OrBooleanAggregator;
import com.google.code.validationframework.swing.property.wrap.InvokeLaterPropertyWrapper;
import org.junit.Test;

import static com.google.code.validationframework.base.binding.Binder.read;

public class InvokeLaterPropertyTest {

    @Test
    public void testInvokeLater() throws InterruptedException {
        ReadableWritableProperty<Boolean, Boolean> rolloverProperty1 = new SimpleBooleanProperty(false);
        ReadableWritableProperty<Boolean, Boolean> rolloverProperty2 = new SimpleBooleanProperty(false);

        ReadableWritableProperty<Boolean, Boolean> tempGlobalRolloverProperty = new SimpleBooleanProperty(false);

        read(rolloverProperty1, rolloverProperty2) //
                .transform(new OrBooleanAggregator()) //
                .write(tempGlobalRolloverProperty);

        ReadableWritableProperty<Boolean, Boolean> globalRolloverProperty = new SimpleBooleanProperty(false);
        read(new InvokeLaterPropertyWrapper<Boolean>(tempGlobalRolloverProperty)).write(globalRolloverProperty);
        globalRolloverProperty.addValueChangeListener(new ValueChangeListener<Boolean>() {
            @Override
            public void valueChanged(ReadableProperty<Boolean> property, Boolean oldValue, Boolean newValue) {
                System.out.println(oldValue + " => " + newValue);
            }
        });

        rolloverProperty1.setValue(true);
        rolloverProperty1.setValue(false);
        rolloverProperty2.setValue(true);
        rolloverProperty2.setValue(false);

        Thread.sleep(2000);
    }
}
