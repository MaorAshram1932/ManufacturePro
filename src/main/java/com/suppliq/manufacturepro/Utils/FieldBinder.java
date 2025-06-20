package com.suppliq.manufacturepro.Utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

public class FieldBinder {

    /**
     * Binds a TextField to a StringProperty.
     *
     * @param field         the text field in the UI
     * @param modelProperty the string property in the model
     */
    public static void bindTextField(TextInputControl field, StringProperty modelProperty) {
        field.textProperty().bindBidirectional(modelProperty);
    }

    /**
     * Binds a TextField to a DoubleProperty using a TextFormatter with input validation.
     *
     * @param field         the text field in the UI
     * @param modelProperty the double property in the model
     */
    public static void bindDoubleField(TextField field, DoubleProperty modelProperty) {
        TextFormatter<Double> formatter = new TextFormatter<>(
                new DoubleStringConverter(),
                modelProperty.get(),
                change -> change.getControlNewText().matches("-?\\d*(\\.\\d*)?") ? change : null
        );
        field.setTextFormatter(formatter);
        modelProperty.asObject().bindBidirectional(formatter.valueProperty());
    }

    /**
     * Binds a TextField to an IntegerProperty using a TextFormatter with input validation.
     *
     * @param field         the text field in the UI
     * @param modelProperty the integer property in the model
     */
    public static void bindIntegerField(TextField field, IntegerProperty modelProperty) {
        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(),
                modelProperty.get(),
                change -> change.getControlNewText().matches("\\d*") ? change : null
        );
        field.setTextFormatter(formatter);
        modelProperty.asObject().bindBidirectional(formatter.valueProperty());
    }
}
