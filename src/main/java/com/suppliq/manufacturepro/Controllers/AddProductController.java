package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Exceptions.Alerts;
import com.suppliq.manufacturepro.Models.Product;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddProductController {
    @FXML private Label formTitleLabel;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField categoryField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Product productToEdit = null; // Holds the product being edited (null if new)
    private Product result = null;        // Holds the result to return to the main screen

    // Temporary validated values stored after form validation
    private String name;
    private String description;
    private String category;
    private double price;
    private int stock;

    /**
     * Initializes the form by setting button actions.
     * This method is called automatically by the FXMLLoader.
     */
    @FXML
    public void initialize() {
        cancelButton.setOnAction(e -> closeWindow());
        saveButton.setOnAction(e -> saveProduct());
    }

    /**
     * Validates all input fields in the product form.
     * Ensures that required fields are not empty and contain only allowed characters.
     *
     * @return true if all fields are valid; false otherwise
     */
    private boolean validateForm() {
        // Read and trim input values
        name = nameField.getText().trim();
        description = descriptionField.getText().trim();
        category = categoryField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();

        // Validate product name
        if (name.isEmpty()) {
            Alerts.showError("יש להזין שם מוצר.");
            return false;
        }
        if (!name.matches("^[\\p{L} ]+$")) {
            Alerts.showError("שם המוצר יכול להכיל אותיות ורווחים בלבד.");
            return false;
        }

        // Validate description
        if (description.isEmpty()) {
            Alerts.showError("יש להזין תיאור למוצר.");
            return false;
        }
        if (!description.matches("^[\\p{L} ]+$")) {
            Alerts.showError("תיאור המוצר יכול להכיל אותיות ורווחים בלבד.");
            return false;
        }

        // Validate category
        if (category.isEmpty()) {
            Alerts.showError("יש לבחור קטגוריה.");
            return false;
        }
        if (!category.matches("^[\\p{L} ]+$")) {
            Alerts.showError("שם הקטגוריה יכול להכיל אותיות ורווחים בלבד.");
            return false;
        }

        // Validate price
        try {
            price = Double.parseDouble(priceText);
            if (price < 0) {
                Alerts.showError("יש להזין מחיר מספרי שהוא 0 ומעלה.");
                return false;
            }
        } catch (NumberFormatException e) {
            Alerts.showError("יש להזין מחיר מספרי תקין.");
            return false;
        }

        // Validate stock
        try {
            stock = Integer.parseInt(stockText);
            if (stock < 0) {
                Alerts.showError("יש להזין כמות במלאי שהיא 0 ומעלה.");
                return false;
            }
        } catch (NumberFormatException e) {
            Alerts.showError("יש להזין כמות במלאי כמספר שלם.");
            return false;
        }

        // All validations passed
        return true;
    }



    /**
     * Saves the product using the validated form input stored in class variables.
     * Determines whether the product is new or being edited,
     * updates the database accordingly, and closes the dialog.
     * If any unexpected error occurs during saving, an error is displayed.
     */
    private void saveProduct() {
        // Validate form before proceeding
        if (!validateForm()) return;

        try {
            if (productToEdit == null) {
                // Create and insert new product
                Product newProduct = new Product(0, name, description, price, stock, category);
                ProductDAO.insertProduct(newProduct);
                result = newProduct;
            } else {
                // Update existing product
                productToEdit.setName(name);
                productToEdit.setDescription(description);
                productToEdit.setPrice(price);
                productToEdit.setStockQuantity(stock);
                productToEdit.setCategory(category);
                ProductDAO.updateProduct(productToEdit);
                result = productToEdit;
            }

            // Close the dialog after saving
            closeWindow();

        } catch (Exception e) {
            // Show a generic error if something unexpected goes wrong
            Alerts.showError("An unexpected error occurred. Please try again.");
        }
    }




    /**
     * Called externally to pass in a product to edit.
     * Populates the form fields with the product's current data
     * and adjusts the form UI for edit mode.
     *
     * @param product the product to edit (must not be null)
     */
    public void setProductToEdit(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product to edit must not be null.");
        }

        this.productToEdit = product;

        // Update the form title and button to reflect "Edit mode"
        formTitleLabel.setText("✏ ערוך מוצר");
        saveButton.setText("עדכן");

        // Populate form fields with the existing product data
        nameField.setText(product.getName());
        descriptionField.setText(product.getDescription());
        priceField.setText(String.valueOf(product.getPrice()));
        stockField.setText(String.valueOf(product.getStockQuantity()));
        categoryField.setText(product.getCategory());
    }


    /**
     * Returns the result product created or edited in the dialog.
     *
     * @return the new or updated Product, or null if the action was cancelled
     */
    public Product getResult() {
        return result;
    }

    /**
     * Closes the window/dialog.
     */
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }


}
