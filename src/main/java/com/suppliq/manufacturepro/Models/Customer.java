package com.suppliq.manufacturepro.Models;

import javafx.beans.property.*;

public class Customer {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty name = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final IntegerProperty priceListId = new SimpleIntegerProperty();

    // Display-only: name of the price list, derived from priceListId (not part of DB schema)
    private final StringProperty priceListName = new SimpleStringProperty();


    public Customer(int id, String name, String email, String phone, String address, int priceListId) {
        this.id.set(id);
        this.name.set(name);
        this.email.set(email);
        this.phone.set(phone);
        this.address.set(address);
        this.priceListId.set(priceListId);
    }

    // Getters
    public int getId() { return id.get(); }
    public String getName() { return name.get(); }
    public String getEmail() { return email.get(); }
    public String getPhone() { return phone.get(); }
    public String getAddress() { return address.get(); }
    public int getPriceListId() { return priceListId.get(); }
    public String getPriceListName() { return priceListName.get(); }

    // Setters
    public void setId(int id) { this.id.set(id); }
    public void setName(String name) { this.name.set(name); }
    public void setEmail(String email) { this.email.set(email); }
    public void setPhone(String phone) { this.phone.set(phone); }
    public void setAddress(String address) { this.address.set(address); }
    public void setPriceListId(int priceListId) { this.priceListId.set(priceListId); }
    public void setPriceListName(String priceListName) { this.priceListName.set(priceListName); }

    // Property Getters
    public IntegerProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty emailProperty() { return email; }
    public StringProperty phoneProperty() { return phone; }
    public StringProperty addressProperty() { return address; }
    public IntegerProperty priceListIdProperty() { return priceListId; }
    public StringProperty priceListNameProperty() { return priceListName; }
}
