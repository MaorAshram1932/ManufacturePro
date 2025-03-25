package com.suppliq.manufacturepro.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HomeController {

    @FXML
    private Label homeLabel;

    @FXML
    public void initialize() {
        homeLabel.setText("ברוך הבא למסך הבית!");
    }
}
