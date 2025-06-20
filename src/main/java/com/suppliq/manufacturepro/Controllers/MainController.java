package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Base.*;
import com.suppliq.manufacturepro.Database.ViewCache;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

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
        // טען לוגו ושם חנות
        logo.setImage(new Image(getClass().getResourceAsStream("/com/suppliq/manufacturepro/Images/logo.png")));
        storeNameLabel.setText(storeName);

        AppInitializer.initializeApp();

        NavigationManager.setMainContainer(mainPane);
        showProducts();
    }
    @FXML
    private void showHome() {
        NavigationManager.goTo(AppView.HOME);
    }

    @FXML
    private void showOrders() {
        BenchmarkTimer.markStart("מעבר לעמוד הזמנות");
        NavigationManager.goTo(AppView.ORDERS);
        BenchmarkTimer.markEnd("מעבר לעמוד הזמנות");
        BenchmarkTimer.printResults();
    }

    @FXML
    private void showProducts() {
        BenchmarkTimer.markStart("מעבר לעמוד מוצרים");
        NavigationManager.goTo(AppView.PRODUCTS);
        BenchmarkTimer.markEnd("מעבר לעמוד מוצרים");
        BenchmarkTimer.printResults();
    }

    @FXML
    private void showPriceLists() {
        BenchmarkTimer.markStart("מעבר לעמוד מחירונים");
        NavigationManager.goTo(AppView.PRICE_LISTS);
        BenchmarkTimer.markEnd("מעבר לעמוד מחירונים");
        BenchmarkTimer.printResults();
    }

    @FXML
    private void showCustomers() {
        BenchmarkTimer.markStart("מעבר לעמוד לקוחות");
        NavigationManager.goTo(AppView.CUSTOMERS);
        BenchmarkTimer.markEnd("מעבר לעמוד לקוחות");
        BenchmarkTimer.printResults();
    }


    @FXML
    public void reloadFromSource() {
        try {
            Scene scene = mainPane.getScene();
            if (scene == null) {
                System.err.println("⚠️ Scene is not available.");
                return;
            }

            // 📂 נתיב לפרויקט
            String projectPath = System.getProperty("user.dir");
            String sourceCssPath = projectPath + "/src/main/resources/com/suppliq/manufacturepro/Styles/application.css";
            String devFolderPath = projectPath + "/dev";
            String outputCssPath = devFolderPath + "/application-reload.css";

            // 🧱 ודא ש־dev/ קיימת
            File devFolder = new File(devFolderPath);
            if (!devFolder.exists() && !devFolder.mkdirs()) {
                System.err.println("❌ Failed to create 'dev' directory.");
                return;
            }

            // 🧾 קריאה והשוואה
            File sourceCssFile = new File(sourceCssPath);
            if (!sourceCssFile.exists()) {
                System.err.println("❌ application.css not found.");
            } else {
                String cssContent = Files.readString(sourceCssFile.toPath());
                File outputCssFile = new File(outputCssPath);

                boolean changed = true;
                if (outputCssFile.exists()) {
                    String existingContent = Files.readString(outputCssFile.toPath());
                    changed = !existingContent.equals(cssContent);
                }

                if (changed) {
                    try (FileWriter writer = new FileWriter(outputCssFile)) {
                        writer.write(cssContent);
                    }
                    System.out.println("✅ CSS updated: " + outputCssFile.getAbsolutePath());

                    for (Window window : Window.getWindows()) {
                        if (window instanceof Stage stage && stage.getScene() != null) {
                            scene.getStylesheets().removeIf(s -> s.contains("application.css") || s.contains("application-reload.css"));
                            scene.getStylesheets().add(outputCssFile.toURI().toString());
                        }
                    }
                    // 🎨 רענון עיצוב
                    // scene.getStylesheets().removeIf(s -> s.contains("application.css") || s.contains("application-reload.css"));
                   // scene.getStylesheets().add(outputCssFile.toURI().toString());
                    System.out.println("✅ Stylesheet reloaded.");
                } else {
                    System.out.println("ℹ️ No changes in CSS – skipping style reload.");
                }
            }

            // 🔁 רענון View תמיד
            AppView currentView = NavigationManager.getCurrentView();
            if (currentView != null) {
                // 🧹 נקה את המטמון (cache) עבור התצוגה הנוכחית
                ViewCache.getInstance().clear(currentView);

                // 🔁 טען מחדש את התצוגה מהקובץ
                NavigationManager.goTo(currentView);

                System.out.println("🔁 View reloaded from source: " + currentView.name());
            } else {
                System.err.println("⚠️ No current view found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }








}
