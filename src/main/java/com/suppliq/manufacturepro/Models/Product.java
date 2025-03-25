package com.suppliq.manufacturepro.Models;

import javafx.beans.property.*;

public class Product {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty description = new SimpleStringProperty();
    private final DoubleProperty price = new SimpleDoubleProperty();
    private final IntegerProperty stockQuantity = new SimpleIntegerProperty();
    private final StringProperty category = new SimpleStringProperty();

    public Product(int id, String name, String description, double price, int stockQuantity, String category) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.price.set(price);
        this.stockQuantity.set(stockQuantity);
        this.category.set(category);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getDescription() { return description.get(); }
    public double getPrice() { return price.get(); }
    public int getStockQuantity() { return stockQuantity.get(); }
    public String getCategory() { return category.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setDescription(String description) { this.description.set(description); }
    public void setPrice(double price) { this.price.set(price); }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity.set(stockQuantity); }
    public void setCategory(String category) { this.category.set(category); }

    // Property Getters (needed for TableView binding)
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty descriptionProperty() { return description; }
    public DoubleProperty priceProperty() { return price; }
    public IntegerProperty stockQuantityProperty() { return stockQuantity; }
    public StringProperty categoryProperty() { return category; }


}
