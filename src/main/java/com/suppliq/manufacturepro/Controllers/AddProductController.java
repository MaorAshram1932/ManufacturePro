package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Exceptions.Alerts;
import com.suppliq.manufacturepro.Models.Product;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import static com.suppliq.manufacturepro.Utils.FieldBinder.*;

public class AddProductController {

    @FXML private Label formTitleLabel;
    @FXML private TextField nameField;
    @FXML private TextArea descriptionField;
    @FXML private TextField priceField;
    @FXML private TextField stockField;
    @FXML private TextField categoryField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // Temporary product copy used for dialog fields binding.
    private Product productModel = new Product(0, "", "", 0.0, 0, "");

    // Holds the reference to the original product.
    private Product originalProduct = null;

    // Flag to indicate whether the form is in edit mode or creating a new product.
    private boolean isEditMode = false;

    // Holds the Final product to return from the dialog.
    private Product finalProduct = null;

    /**
     * Initializes the form and binds UI fields to the product model.
     * Called automatically by the FXMLLoader.
     */
    @FXML
    public void initialize() {
        // Bind UI fields to model properties
        bindAllFields();
        // Button actions
        setupButtonActions();
    }

    /**
     * Configures action listeners for the Cancel and Save buttons.
     * Cancel closes the dialog; Save triggers validation and persistence.
     */
    private void setupButtonActions() {
        cancelButton.setOnAction(e -> closeWindow());
        saveButton.setOnAction(e -> saveProduct());
    }

    /**
     * Binds all relevant input fields to the properties of the product model.
     * Handles both simple String fields and numeric fields using TextFormatter.
     */
    private void bindAllFields() {
        // --- Bind basic text fields ---
        bindTextField(nameField, productModel.nameProperty());
        bindTextField(descriptionField, productModel.descriptionProperty());
        bindTextField(categoryField, productModel.categoryProperty());

        // --- Bind numeric fields with format and validation ---
        bindDoubleField(priceField, productModel.priceProperty());
        bindIntegerField(stockField, productModel.stockQuantityProperty());
    }

    /**
     * Sets the product to be edited and prepares the form.
     * Creates a copy of the original product to bind the form to –
     * this prevents accidental live changes before saving.
     *
     * @param product the existing product to edit
     * @throws IllegalArgumentException if product is null
     */
    public void setProductToEdit(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product to edit must not be null.");
        }

        this.isEditMode = true;
        this.originalProduct = product;

        // Create a deep copy to bind the UI fields to a temporary object.
        // This allows canceling without modifying the original product.
        this.productModel = new Product(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getCategory()
        );

        formTitleLabel.setText("✏ ערוך מוצר");
        saveButton.setText("עדכן");

        // Bind UI fields to the temporary model, not to the original product.
        bindAllFields();
    }

    /**
     * Validates and saves the product.
     * In edit mode: updates the original product with the modified values.
     * In create mode: inserts the new product into the database.
     * Closes the window after successful operation.
     */
    @FXML
    private void saveProduct() {
        if (!validateForm()) return;

        try {
            if (isEditMode) {
                // Apply the updated values from the temporary model to the original product.
                originalProduct.setName(productModel.getName());
                originalProduct.setDescription(productModel.getDescription());
                originalProduct.setPrice(productModel.getPrice());
                originalProduct.setStockQuantity(productModel.getStockQuantity());
                originalProduct.setCategory(productModel.getCategory());

                // Update the product in the database.
                ProductDAO.updateProduct(originalProduct);
                finalProduct = originalProduct;
            } else {
                // Insert the new product into the database.
                ProductDAO.insertProduct(productModel);
                finalProduct = productModel;
            }

            closeWindow();

        } catch (Exception e) {
            Alerts.showError("אירעה שגיאה בלתי צפויה. אנא נסה שוב.");
        }
    }

    /**
     * Validates all input fields before saving.
     *
     * @return true if all fields are valid; false otherwise
     */
    private boolean validateForm() {
        // Name
        if (productModel.getName().isBlank()) {
            Alerts.showError("יש להזין שם מוצר.");
            return false;
        }
        if (!productModel.getName().matches("^[\\p{L} ]+$")) {
            Alerts.showError("שם המוצר יכול להכיל אותיות ורווחים בלבד.");
            return false;
        }

        // Description
        if (productModel.getDescription().isBlank()) {
            Alerts.showError("יש להזין תיאור למוצר.");
            return false;
        }
        if (!productModel.getDescription().matches("^[\\p{L} ]+$")) {
            Alerts.showError("תיאור המוצר יכול להכיל אותיות ורווחים בלבד.");
            return false;
        }

        // Category
        if (productModel.getCategory().isBlank()) {
            Alerts.showError("יש לבחור קטגוריה.");
            return false;
        }
        if (!productModel.getCategory().matches("^[\\p{L} ]+$")) {
            Alerts.showError("שם הקטגוריה יכול להכיל אותיות ורווחים בלבד.");
            return false;
        }

        // Price
        if (productModel.getPrice() < 0) {
            Alerts.showError("יש להזין מחיר מספרי שהוא 0 ומעלה.");
            return false;
        }

        // Stock
        if (productModel.getStockQuantity() < 0) {
            Alerts.showError("יש להזין כמות במלאי שהיא 0 ומעלה.");
            return false;
        }

        return true;
    }

    /**
     * Closes the current window.
     */
    @FXML
    private void closeWindow() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    /**
     * Returns the product created or edited in the form.
     *
     * @return the resulting product instance
     */
    public Product getResult() {
        return finalProduct;
    }
}
