package com.suppliq.manufacturepro.Base;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public enum AppView {

    MAIN("main-view.fxml"),
    HOME("home-view.fxml"),
    PRODUCTS("product-view.fxml"),

    PRICE_LISTS("pricelist-view.fxml"),
    CUSTOMERS("customer-view.fxml"),
    ORDERS("order-view.fxml"),

    ADD_PRODUCT("add-product-view.fxml"),
    ADD_CUSTOMER("add-customer-view.fxml");



    private final String fileName;

    AppView(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

//    public URL getViewUrl() {
//        return AppView.class.getResource("/com/suppliq/manufacturepro/Views/" + fileName);
//    }

    public URL getViewUrl() {
        try {
            Path path = Paths.get("src/main/resources/com/suppliq/manufacturepro/Views/" + fileName);
            return path.toUri().toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

}