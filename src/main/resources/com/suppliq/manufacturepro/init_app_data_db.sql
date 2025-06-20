-- יצירת מסד הנתונים אם לא קיים
CREATE DATABASE IF NOT EXISTS app_data;
USE app_data;

-- יצירת טבלת מוצרים
CREATE TABLE IF NOT EXISTS products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description TEXT,
    price DOUBLE,
    stock_quantity INT,
    category VARCHAR(100)
);

-- יצירת טבלת מחירונים
CREATE TABLE IF NOT EXISTS price_lists (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

-- טבלת קישור בין מוצרים למחירונים עם מחיר מותאם לכל שילוב
CREATE TABLE IF NOT EXISTS price_list_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    product_id INT NOT NULL,
    price_list_id INT NOT NULL,
    price DOUBLE DEFAULT 0,
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (price_list_id) REFERENCES price_lists(id) ON DELETE CASCADE,
    UNIQUE (product_id, price_list_id)
);

-- יצירת טבלת לקוחות
CREATE TABLE IF NOT EXISTS customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(50),
    address VARCHAR(255),
    email VARCHAR(100),
    price_list_id INT,
    FOREIGN KEY (price_list_id) REFERENCES price_lists(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status ENUM('בהמתנה', 'בטיפול', 'נשלח', 'הושלם', 'בוטל') DEFAULT 'בהמתנה',
    total_amount DECIMAL(10,2) NOT NULL,

    FOREIGN KEY (customer_id) REFERENCES customers(id)
) AUTO_INCREMENT = 10000;

CREATE TABLE IF NOT EXISTS order_items (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    product_name VARCHAR(255),       -- שומר את שם המוצר כדי לשמר מידע גם אם המוצר ישתנה
    unit_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,

    FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(id)
);

