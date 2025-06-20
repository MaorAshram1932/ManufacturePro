package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Models.Order;
import com.suppliq.manufacturepro.Models.Product;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Models.PriceList;

import java.util.ArrayList;
import java.util.List;

public class DataCache {

    public static List<Product> products = new ArrayList<>();
    public static List<Customer> customers = new ArrayList<>();;
    public static List<PriceList> priceLists = new ArrayList<>();;
    public static List<Order> orders = new ArrayList<>();;

    private DataCache() {
        // prevents instantiation
    }
}