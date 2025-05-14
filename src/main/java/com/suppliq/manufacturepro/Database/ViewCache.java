package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Utils.LoggerManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewCache {

    private static ViewCache instance;


    private final Map<String, Pair<Node, Object>> cachedViews = new HashMap<>();

    private ViewCache() {
    }

    public static ViewCache getInstance() {
        if (instance == null) {
            instance = new ViewCache();
        }
        return instance;
    }

    public void preload(String fxmlFile) {
        if (!cachedViews.containsKey(fxmlFile)) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/suppliq/manufacturepro/Views/" + fxmlFile));
                Node view = loader.load();
                Object controller = loader.getController();
                cachedViews.put(fxmlFile, new Pair<>(view, controller));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public Node getView(String fxmlFile) {
        if (!cachedViews.containsKey(fxmlFile)) {
            preload(fxmlFile); // אופציונלי - טען אם לא נטען עדיין
        }
        return cachedViews.get(fxmlFile).getKey();
    }

    public <T> T getController(String fxmlFile) {
        if (!cachedViews.containsKey(fxmlFile)) {
            preload(fxmlFile);
        }
        return (T) cachedViews.get(fxmlFile).getValue();
    }

    public void clear(String fxmlFile) {
        cachedViews.remove(fxmlFile);
        LoggerManager.logDebug("Cleared FXML cache: " + fxmlFile);
    }
}
