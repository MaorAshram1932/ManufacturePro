package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Base.AppView;
import com.suppliq.manufacturepro.Utils.LoggerManager;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.util.Pair;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ViewCache {

    private static ViewCache instance;


    private final Map<AppView, Pair<Node, Object>> cachedViews = new HashMap<>();

    private ViewCache() {
    }

    public static ViewCache getInstance() {
        if (instance == null) {
            instance = new ViewCache();
        }
        return instance;
    }

    public void preload(AppView view) {
        if (!cachedViews.containsKey(view)) {
            try {
                FXMLLoader loader = new FXMLLoader(view.getViewUrl());
                Node loadedView = loader.load();
                Object controller = loader.getController();
                cachedViews.put(view, new Pair<>(loadedView, controller));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    public Node getView(AppView view) {
        if (!cachedViews.containsKey(view)) {
            preload(view);
        }
        return cachedViews.get(view).getKey();
    }

    @SuppressWarnings("unchecked")
    public <T> T getController(AppView view) {
        if (!cachedViews.containsKey(view)) {
            preload(view);
        }
        return (T) cachedViews.get(view).getValue();
    }

    public void clear(AppView view) {
        cachedViews.remove(view);
        LoggerManager.logDebug("Cleared FXML cache: " + view.name());
    }
}
