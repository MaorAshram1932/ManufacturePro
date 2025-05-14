package com.suppliq.manufacturepro.Base;

public enum AppView {
    HOME("home-view.fxml"),
    PRODUCTS("product-view.fxml"),
    CUSTOMERS("customers-view.fxml"),
    ORDERS("orders-view.fxml");

    private final String fileName;

    AppView(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}