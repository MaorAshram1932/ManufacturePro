package com.suppliq.manufacturepro.Models;

public class PriceListItem {
    private int id;
    private int priceListId;
    private int productId;
    private double price;

    public PriceListItem(int id, int priceListId, int productId, double price) {
        this.id = id;
        this.priceListId = priceListId;
        this.productId = productId;
        this.price = price;
    }

    public PriceListItem(int priceListId, int productId, double price) {
        this(0, priceListId, productId, price);
    }

    public int getId() { return id; }
    public int getPriceListId() { return priceListId; }
    public int getProductId() { return productId; }
    public double getPrice() { return price; }

    public void setId(int id) { this.id = id; }
    public void setPriceListId(int priceListId) { this.priceListId = priceListId; }
    public void setProductId(int productId) { this.productId = productId; }
    public void setPrice(double price) { this.price = price; }
}
