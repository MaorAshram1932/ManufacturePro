package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.CustomerDAO;
import com.suppliq.manufacturepro.Database.DataCache;
import com.suppliq.manufacturepro.Exceptions.Alerts;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Models.PriceList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.Optional;

import static com.suppliq.manufacturepro.Utils.FieldBinder.*;

/**
 * Controller class for the Add/Edit Customer dialog.
 * Manages form binding, validation, and customer persistence.
 */
public class AddCustomerController {

    @FXML private Label formTitleLabel;
    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private TextField addressField;
    @FXML private ComboBox<PriceList> priceListComboBox;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    // Temporary customer copy used for dialog fields binding.
    private Customer customerModel = new Customer(0, "", "", "", "", 0);

    // Holds the reference to the original customer.
    private Customer originalCustomer = null;

    // Flag to indicate whether the form is in edit mode or creating a new customer.
    private boolean isEditMode = false;

    // Holds the Final customer to return from the dialog.
    private Customer finalCustomer = null;

    /**
     * Initializes the form and binds UI fields to the customer model.
     * Called automatically by the FXMLLoader.
     */
    @FXML
    public void initialize() {
        bindAllFields();
        //setupButtonActions();
        setupPriceListComboBox();
    }

    /**
     * Configures action listeners for the Cancel and Save buttons.
     * Cancel closes the dialog; Save triggers validation and persistence.
     */
    private void setupButtonActions() {
        cancelButton.setOnAction(e -> closeWindow());
        saveButton.setOnAction(e -> saveCustomer());
    }

    /**
     * Binds all relevant input fields to the properties of the customer model.
     */
    private void bindAllFields() {
        bindTextField(nameField, customerModel.nameProperty());
        bindTextField(phoneField, customerModel.phoneProperty());
        bindTextField(emailField, customerModel.emailProperty());
        bindTextField(addressField, customerModel.addressProperty());
    }

    /**
     * Sets the customer to be edited and prepares the form.
     *
     * @param customer the existing customer to edit
     * @throws IllegalArgumentException if customer is null
     */
    public void setCustomerToEdit(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer to edit must not be null.");
        }

        this.isEditMode = true;
        this.originalCustomer = customer;

        this.customerModel = new Customer(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getAddress(),
                customer.getPriceListId()
        );
        customerModel.setPriceListName(customer.getPriceListName());

        formTitleLabel.setText("✏ ערוך לקוח");
        saveButton.setText("עדכן");

        setupPriceListComboBox();
        bindAllFields();
    }

    /**
     * Validates and saves the customer.
     * In edit mode: updates the original customer with the modified values.
     * In create mode: inserts the new customer into the database.
     */
    @FXML
    private void saveCustomer() {
        if (!validateForm()) return;

        try {

            customerModel.setPriceListId(0);

            if (isEditMode) {
                originalCustomer.setName(customerModel.getName());
                originalCustomer.setEmail(customerModel.getEmail());
                originalCustomer.setPhone(customerModel.getPhone());
                originalCustomer.setAddress(customerModel.getAddress());
                originalCustomer.setPriceListId(customerModel.getPriceListId());

                CustomerDAO.updateCustomer(originalCustomer);
                finalCustomer = originalCustomer;
            } else {
                CustomerDAO.insertCustomer(customerModel);
                finalCustomer = customerModel;
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
        if (customerModel.getName().isBlank()) {
            Alerts.showError("יש להזין שם לקוח.");
            return false;
        }
        if (customerModel.getPhone().isBlank()) {
            Alerts.showError("יש להזין מספר טלפון.");
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
     * Returns the customer created or edited in the form.
     *
     * @return the resulting customer instance
     */
    public Customer getResult() {
        return finalCustomer;
    }


    /**
     * Configures the price list ComboBox for the customer form.
     * Loads price lists from cache, sets display formatting, and adds a default option.
     */
    private void setupPriceListComboBox() {
        // Load all available price lists from cache
        if (DataCache.priceLists != null) {
            priceListComboBox.setItems(FXCollections.observableArrayList(DataCache.priceLists));
        }

        // Convert a PriceList object into its name for display in the ComboBox
        priceListComboBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(PriceList priceList) {
                return priceList != null ? priceList.getName() : "";
            }

            @Override
            public PriceList fromString(String string) {
                // Not needed, as users select from predefined items only
                return null;
            }
        });

        // Add a default price list entry for customers without a specific price list
        priceListComboBox.getItems().add(0, new PriceList(0, "מחירון רגיל"));

        // Select the first item by default to ensure there's always a selection
        // priceListComboBox.getSelectionModel().selectFirst();

        // 🔄 Bind selected PriceList -> customerModel.priceListId
        priceListComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                customerModel.setPriceListId(newVal.getId());
                customerModel.setPriceListName(newVal.getName());
            }
        });

        // 🔄 Initialize ComboBox selection from customerModel.priceListId
        Optional<PriceList> selected = priceListComboBox.getItems().stream()
                .filter(p -> p.getId() == customerModel.getPriceListId())
                .findFirst();

        if (selected.isPresent()) {
            priceListComboBox.setValue(selected.get());
        } else {
            priceListComboBox.getSelectionModel().selectFirst();
        }

        selected.ifPresent(priceListComboBox::setValue);
    }


}
