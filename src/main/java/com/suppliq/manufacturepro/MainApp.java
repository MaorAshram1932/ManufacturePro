package com.suppliq.manufacturepro;

import com.suppliq.manufacturepro.Database.DatabaseConnector;
import com.suppliq.manufacturepro.Exceptions.ExceptionHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;

import static com.suppliq.manufacturepro.Utils.ConfigManager.*;
import static com.suppliq.manufacturepro.Utils.Globals.getProjectPath;
import static com.suppliq.manufacturepro.Utils.LoggerManager.*;
import static com.suppliq.manufacturepro.Utils.Utilities.setScreenSize;

public class MainApp extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception {
        logInfo("Starting ManufacturePro...");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/suppliq/manufacturepro/Views/main-view.fxml"));
        Scene scene = new Scene(loader.load());


        //Style css
        scene.getStylesheets().add(getClass().getResource("/com/suppliq/manufacturepro/Styles/application.css").toExternalForm());
        // Set Screen size
        setScreenSize(primaryStage);



        primaryStage.setScene(scene);
        primaryStage.setTitle("Manufacture Pro");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);


    }
}
