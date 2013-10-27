package com.google.code.validationframework.demo.javafx;

import com.google.code.validationframework.base.resulthandler.BooleanResultCollector;
import com.google.code.validationframework.base.rule.bool.AndBooleanRule;
import com.google.code.validationframework.base.rule.bool.IsTrueRule;
import com.google.code.validationframework.base.rule.string.StringNotEmptyRule;
import com.google.code.validationframework.base.transform.NegateBooleanTransformer;
import com.google.code.validationframework.base.trigger.ManualTrigger;
import com.google.code.validationframework.javafx.dataprovider.ObservableValueProvider;
import com.google.code.validationframework.javafx.resulthandler.WritableValueSetterResultHandler;
import com.google.code.validationframework.javafx.trigger.ObservableValueChangeTrigger;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.tbee.javafx.scene.layout.MigPane;

import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.collect;
import static com.google.code.validationframework.base.validator.generalvalidator.dsl.GeneralValidatorBuilder.on;

public class JavaFXDemoApp extends Application {

    /**
     * @see Application#start(Stage)
     */
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = initComponents();
        stage.setScene(new Scene(root, 640, 480));
        stage.show();
    }

    private Parent initComponents() {
        MigPane parent = new MigPane("fill, wrap 1", "push[]push", "push[]related[]unrelated[]20[]push");

        Label nameLabel = new Label("Enter your name:");
        parent.add(nameLabel);
        TextField textField = new TextField();
        parent.add(textField);

        CheckBox agreeCheckBox = new CheckBox("I agree with the terms and conditions");
        parent.add(agreeCheckBox);

        Button submitButton = new Button("Submit");
        parent.add(submitButton);

        // Install validation on textfield
        ManualTrigger manualTrigger = new ManualTrigger();
        BooleanResultCollector nameResultCollector = new BooleanResultCollector();
        on(new ObservableValueChangeTrigger<String>(textField.textProperty())) //
                .on(manualTrigger) //
                .read(new ObservableValueProvider<String>(textField.textProperty())) //
                .check(new StringNotEmptyRule()) //
                .handleWith(nameResultCollector); //

        // Install validation on "I agree" checkbox
        BooleanResultCollector agreeResultCollector = new BooleanResultCollector();
        on(new ObservableValueChangeTrigger<Boolean>(agreeCheckBox.selectedProperty())) //
                .on(manualTrigger) //
                .read(new ObservableValueProvider<Boolean>(agreeCheckBox.selectedProperty())) //
                .check(new IsTrueRule()) //
                .handleWith(agreeResultCollector); //

        // Install global validation
        collect(nameResultCollector) //
                .collect(agreeResultCollector) //
                .check(new AndBooleanRule()) //
                .transform(new NegateBooleanTransformer()) //
                .handleWith(new WritableValueSetterResultHandler<Boolean>(submitButton.disableProperty()));

        // Initial trigger
        manualTrigger.trigger();

        return parent;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
