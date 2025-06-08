package com.suppliq.manufacturepro.Base;

import java.net.URL;

public enum AppView {
    HOME("home-view.fxml"),
    PRODUCTS("product-view.fxml"),

    PRICE_LISTS("pricelist-view.fxml"),
    CUSTOMERS("customers-view.fxml"),
    ORDERS("orders-view.fxml"),

    ADD_PRODUCT("add-product-view.fxml");



    private final String fileName;

    AppView(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public URL getViewUrl() {
        return AppView.class.getResource("/com/suppliq/manufacturepro/Views/" + fileName);
    }
}