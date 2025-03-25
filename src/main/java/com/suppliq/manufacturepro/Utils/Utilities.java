package com.suppliq.manufacturepro.Utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import static com.suppliq.manufacturepro.Utils.ConfigManager.getCustomerConfig;

public class Utilities {
    public static final String storeName = getCustomerConfig("customer.name");


    public static final double screenWidth = 0.8;
    public static final double screenHeight = 0.8;
    public static void setScreenSize(Stage stage){
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

        // Calculate 80% of screen size
        double width = screenBounds.getWidth() * screenWidth;
        double height = screenBounds.getHeight() * screenHeight;

        // Set the stage size to 80% of screen dimensions
        stage.setWidth(width);
        stage.setHeight(height);

        // Center the stage
        stage.setX((screenBounds.getWidth() - width) / 2);
        stage.setY((screenBounds.getHeight() - height) / 2);
    }

    public static void setFullScreen(Stage stage){
        stage.setMaximized(true);
    }


}
