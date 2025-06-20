package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.PriceListDAO;
import com.suppliq.manufacturepro.Models.PriceList;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddPriceListController {

    @FXML private TextField nameField;

    @FXML
    private void handleSave() {
        String name = nameField.getText().trim();

        if (name.isEmpty()) {
            nameField.setStyle("-fx-border-color: red;");
            return;
        }

        PriceList newPriceList = new PriceList(name);
        PriceListDAO.addPriceList(newPriceList);

        closeWindow();
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}
