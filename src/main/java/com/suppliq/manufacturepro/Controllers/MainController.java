package com.suppliq.manufacturepro.Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.Node;
import java.io.IOException;

import static com.suppliq.manufacturepro.Utils.LoggerManager.logInfo;
import static com.suppliq.manufacturepro.Utils.Utilities.storeName;

public class MainController {



    @FXML
    private BorderPane mainPane;

    @FXML
    private ImageView logo;

    @FXML
    private Label storeNameLabel;


    @FXML
    public void initialize() {
        // טען את הלוגו
        Image logoImage = new Image(getClass().getResourceAsStream("/com/suppliq/manufacturepro/Images/logo.png"));
        logo.setImage(logoImage);

        // עדכן את שם החנות
        storeNameLabel.setText(storeName);
    }


    @FXML
    private void showHomeView() {
        loadView("home-view.fxml");
    }

    @FXML
    private void showProductView() {
        loadView("product-view.fxml");
    }

    private void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/suppliq/manufacturepro/Views/" + fxmlFile));
            logInfo("/com/suppliq/manufacturepro/Views/" + fxmlFile);
            Node view = loader.load();
            mainPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
