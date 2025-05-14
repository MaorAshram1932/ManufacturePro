package com.suppliq.manufacturepro;

import com.suppliq.manufacturepro.Base.BenchmarkTimer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static com.suppliq.manufacturepro.Utils.LoggerManager.*;
import static com.suppliq.manufacturepro.Utils.Utilities.setScreenSize;

public class MainApp extends Application {




    @Override
    public void start(Stage primaryStage) throws Exception {
        BenchmarkTimer.startAppBenchmark();
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
        BenchmarkTimer.printResults();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
