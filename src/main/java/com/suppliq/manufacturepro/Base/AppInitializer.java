package com.suppliq.manufacturepro.Base;

import com.suppliq.manufacturepro.Controllers.CustomerController;
import com.suppliq.manufacturepro.Controllers.OrderController;
import com.suppliq.manufacturepro.Controllers.PriceListController;
import com.suppliq.manufacturepro.Controllers.ProductController;
import com.suppliq.manufacturepro.Database.*;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Models.PriceList;

public class AppInitializer {

    public static void initializeApp() {
        preloadData();       // שלב 1 – טען את הנתונים מה־DB
        preloadViews();      // שלב 2 – טען את כל קבצי ה־FXML
        bindDataToViews();   // שלב 3 – חבר את הנתונים לתצוגות

    }

    private static void preloadData() {
        BenchmarkTimer.markStart("טעינת נתונים");

        // כאן טוענים את הנתונים ומכניסים אותם ל־DataCache
        DataCache.orders = OrderDAO.getOrders();
        DataCache.products = ProductDAO.getProducts();
        DataCache.customers = CustomerDAO.getCustomers();
        DataCache.priceLists = PriceListDAO.getAllPriceLists();
        // אם יש גם לקוחות/מחירים:
        // DataCache.customers = CustomerDAO.getAllCustomers();
        // DataCache.prices = PriceDAO.getAllPrices();
        BenchmarkTimer.markEnd("טעינת נתונים");
    }

    private static void preloadViews() {
        BenchmarkTimer.markStart("טעינת תצוגות");
        ViewCache cache = ViewCache.getInstance();
        cache.preload(AppView.HOME);
        cache.preload(AppView.ORDERS);
        cache.preload(AppView.PRODUCTS);
        cache.preload(AppView.CUSTOMERS);
        cache.preload(AppView.PRICE_LISTS);
        //cache.preload(AppView.ORDERS.getFileName());
        // תוסיף כאן עוד עמודים שתרצה לטעון מראש
        BenchmarkTimer.markEnd("טעינת תצוגות");
    }

    private static void bindDataToViews() {
        BenchmarkTimer.markStart("חיבור נתונים לתצוגות");
        OrderController orderController = ViewCache.getInstance().getController(AppView.ORDERS);
        orderController.setOrderList(DataCache.orders);
        ProductController productController = ViewCache.getInstance().getController(AppView.PRODUCTS);
        productController.setProductList(DataCache.products);
        CustomerController customerController = ViewCache.getInstance().getController(AppView.CUSTOMERS);
        customerController.setCustomerList(DataCache.customers);
        PriceListController priceListController = ViewCache.getInstance().getController(AppView.PRICE_LISTS);
        priceListController.setPriceListList(DataCache.priceLists);
        BenchmarkTimer.markEnd("חיבור נתונים לתצוגות");
    }
}
