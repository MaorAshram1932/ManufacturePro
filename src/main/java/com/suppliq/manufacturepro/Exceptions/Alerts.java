package com.suppliq.manufacturepro.Exceptions;

import javafx.scene.control.Alert;

public class Alerts {

    /**
     * Displays a standard error alert with the given message.
     *
     * @param message the content to display in the alert
     */
    public static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("שגיאה");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
