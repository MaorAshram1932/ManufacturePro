package com.suppliq.manufacturepro.Models;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Order {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty customerId = new SimpleIntegerProperty();
    private final StringProperty customerName = new SimpleStringProperty(); // מוצג בטבלה
    private final ObjectProperty<LocalDateTime> orderDate = new SimpleObjectProperty<>();
    private final StringProperty status = new SimpleStringProperty(); // בעברית: "בהמתנה" / "בטיפול" וכו’
    private final DoubleProperty totalAmount = new SimpleDoubleProperty();

    public Order(int id, int customerId, String customerName,
                 LocalDateTime orderDate, String status, double totalAmount) {
        this.id.set(id);
        this.customerId.set(customerId);
        this.customerName.set(customerName);
        this.orderDate.set(orderDate);
        this.status.set(status);
        this.totalAmount.set(totalAmount);
    }

    // --- Getters ---
    public int getId() { return id.get(); }
    public int getCustomerId() { return customerId.get(); }
    public String getCustomerName() { return customerName.get(); }
    public LocalDateTime getOrderDate() { return orderDate.get(); }
    public String getStatus() { return status.get(); }
    public double getTotalAmount() { return totalAmount.get(); }

    // --- Setters ---
    public void setId(int id) { this.id.set(id); }
    public void setCustomerId(int customerId) { this.customerId.set(customerId); }
    public void setCustomerName(String customerName) { this.customerName.set(customerName); }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate.set(orderDate); }
    public void setStatus(String status) { this.status.set(status); }
    public void setTotalAmount(double totalAmount) { this.totalAmount.set(totalAmount); }

    // --- Property Accessors (for JavaFX binding) ---
    public IntegerProperty idProperty() { return id; }
    public IntegerProperty customerIdProperty() { return customerId; }
    public StringProperty customerNameProperty() { return customerName; }
    public ObjectProperty<LocalDateTime> orderDateProperty() { return orderDate; }
    public StringProperty statusProperty() { return status; }
    public DoubleProperty totalAmountProperty() { return totalAmount; }
}
