package com.suppliq.manufacturepro;

import com.suppliq.manufacturepro.Base.AppView;
import com.suppliq.manufacturepro.Base.BenchmarkTimer;
import com.suppliq.manufacturepro.Base.ScreenUtil;
import com.suppliq.manufacturepro.Database.DatabaseInitializer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.List;

import static com.suppliq.manufacturepro.Utils.LoggerManager.*;
import static com.suppliq.manufacturepro.Utils.Utilities.setScreenSize;

public class MainApp extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
        BenchmarkTimer.startAppBenchmark();
        DatabaseInitializer.initialize();
        logInfo("Starting ManufacturePro...");
        FXMLLoader loader = new FXMLLoader(AppView.MAIN.getViewUrl());
        Scene scene = new Scene(loader.load());
        //Style css
        scene.getStylesheets().add(getClass().getResource("/com/suppliq/manufacturepro/Styles/application.css").toExternalForm());
        // Set Screen size
        setScreenSize(primaryStage);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/com/suppliq/manufacturepro/Images/app_logo.png")));

        primaryStage.setScene(scene);
        primaryStage.setTitle("Manufacture Pro");
        primaryStage.show();

        // 🎯 הוספת listener למעבר בין מסכים
        //primaryStage.xProperty().addListener((obs, oldX, newX) -> ScreenUtil.handleScreenChange(primaryStage));
        //primaryStage.yProperty().addListener((obs, oldY, newY) -> ScreenUtil.handleScreenChange(primaryStage));

        BenchmarkTimer.printResults();
    }



    public static void main(String[] args) {
        Application.launch(args);
    }
}
