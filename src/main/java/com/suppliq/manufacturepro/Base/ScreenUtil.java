package com.suppliq.manufacturepro.Base;

import javafx.application.Platform;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.List;

public class ScreenUtil {
    private static long lastScreenChangeTime = 0;
    private static Screen currentScreen = Screen.getPrimary();

    public static double getScreenWidth() {
        return Screen.getPrimary().getBounds().getWidth();
    }

    public static double getScreenHeight() {
        return Screen.getPrimary().getBounds().getHeight();
    }

    public static Rectangle2D getScreenBounds() {
        return Screen.getPrimary().getBounds();
    }

    public static double percentageWidth(double percent) {
        return getScreenWidth() * (percent / 100.0);
    }

    public static double percentageHeight(double percent) {
        return getScreenHeight() * (percent / 100.0);
    }

    public static void handleScreenChange(Stage stage) {
        long now = System.currentTimeMillis();
        if (now - lastScreenChangeTime < 300) return; // מחכה לפחות 300ms בין שינויים
        lastScreenChangeTime = now;

        List<Screen> screens = Screen.getScreensForRectangle(
                stage.getX(), stage.getY(), stage.getWidth(), stage.getHeight()
        );

        if (screens.isEmpty()) {
            System.out.println("⚠️ לא נמצא מסך בזמן מעבר – מחכים לסנכרון...");
            return;
        }

        Screen newScreen = screens.get(0);

        if (!newScreen.equals(currentScreen)) {
            currentScreen = newScreen;
            System.out.println("עבר למסך אחר! DPI = " + newScreen.getDpi());

            Platform.runLater(() -> {
                stage.getScene().getRoot().requestLayout();
                stage.sizeToScene(); // לא חובה
            });
        }
    }


}
