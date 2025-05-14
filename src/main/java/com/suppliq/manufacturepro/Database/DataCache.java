package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Models.Product;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Models.Price;
import java.util.List;

public class DataCache {

    public static List<Product> products;
    public static List<Customer> customers;
    public static List<Price> prices;

    private DataCache() {
        // prevents instantiation
    }
}