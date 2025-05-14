package com.suppliq.manufacturepro.Base;

import com.suppliq.manufacturepro.Controllers.ProductController;
import com.suppliq.manufacturepro.Database.DataCache;
import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Database.ViewCache;

public class AppInitializer {

    public static void initializeApp() {
        preloadData();       // שלב 1 – טען את הנתונים מה־DB
        preloadViews();      // שלב 2 – טען את כל קבצי ה־FXML
        bindDataToViews();   // שלב 3 – חבר את הנתונים לתצוגות

    }

    private static void preloadData() {
        BenchmarkTimer.markStart("טעינת נתונים");

        // כאן טוענים את הנתונים ומכניסים אותם ל־DataCache
        DataCache.products = ProductDAO.getProducts();

        // אם יש גם לקוחות/מחירים:
        // DataCache.customers = CustomerDAO.getAllCustomers();
        // DataCache.prices = PriceDAO.getAllPrices();
        BenchmarkTimer.markEnd("טעינת נתונים");
    }

    private static void preloadViews() {
        BenchmarkTimer.markStart("טעינת תצוגות");
        ViewCache cache = ViewCache.getInstance();
        cache.preload(AppView.HOME.getFileName());
        cache.preload(AppView.PRODUCTS.getFileName());
        //cache.preload(AppView.CUSTOMERS.getFileName());
        //cache.preload(AppView.ORDERS.getFileName());
        // תוסיף כאן עוד עמודים שתרצה לטעון מראש
        BenchmarkTimer.markEnd("טעינת תצוגות");
    }

    private static void bindDataToViews() {
        BenchmarkTimer.markStart("חיבור נתונים לתצוגות");
        ProductController productController = ViewCache.getInstance().getController(AppView.PRODUCTS.getFileName());
        productController.setProductList(DataCache.products);
        BenchmarkTimer.markEnd("חיבור נתונים לתצוגות");
    }
}
