package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddProductController {
    @FXML private Label formTitleLabel;
    @FXML private Product productToEdit = null;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField categoryField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;


    @FXML
    public void initialize() {
        cancelButton.setOnAction(e -> closeWindow());
        saveButton.setOnAction(e -> saveProduct());
    }

    private void saveProduct() {
        try {
            String name = nameField.getText();
            String description = descriptionField.getText();
            double price = Double.parseDouble(priceField.getText());
            int stock = Integer.parseInt(stockField.getText());
            String category = categoryField.getText();

            if (productToEdit == null) {
                // מצב הוספה
                Product newProduct = new Product(0, name, description, price, stock, category);
                ProductDAO.insertProduct(newProduct);
            } else {
                // מצב עריכה
                productToEdit.setName(name);
                productToEdit.setDescription(description);
                productToEdit.setPrice(price);
                productToEdit.setStockQuantity(stock);
                productToEdit.setCategory(category);
                ProductDAO.updateProduct(productToEdit);
            }

            closeWindow();

        } catch (Exception e) {
            showAlert("שגיאה", "יש למלא את כל השדות כראוי.");
        }
    }


    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void setProductToEdit(Product product) {
        this.productToEdit = product;

        formTitleLabel.setText("✏ ערוך מוצר");

        // מלא את השדות
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStockQuantity()));
        categoryField.setText(product.getCategory());

        saveButton.setText("עדכן");
    }

}
