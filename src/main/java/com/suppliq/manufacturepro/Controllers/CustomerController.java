package com.suppliq.manufacturepro.Controllers;



import com.suppliq.manufacturepro.Base.AppCSS;
import com.suppliq.manufacturepro.Database.DataCache;
import com.suppliq.manufacturepro.Database.CustomerDAO;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Base.AppView;
import com.suppliq.manufacturepro.Utils.ColumnWidths;
import com.suppliq.manufacturepro.Utils.LoggerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class CustomerController {

    @FXML
    private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> columnName;
    @FXML private TableColumn<Customer, String> columnEmail;
    @FXML private TableColumn<Customer, String> columnPhone;
    @FXML private TableColumn<Customer, String> columnAddress;
    @FXML private TableColumn<Customer, Integer> columnPriceList;
    @FXML private TableColumn<Customer, Void> columnActions;
    @FXML private TextField searchField;

    // Observable list of all customers – automatically syncs with UI
    private final ObservableList<Customer> originalCustomerList = FXCollections.observableArrayList();

    // Filtered view of the customer list
    private final FilteredList<Customer> filteredCustomerList = new FilteredList<>(originalCustomerList, p -> true);

    /**
     * Initializes the customer controller.
     */
    @FXML
    public void initialize() {
        setupTableColumns();       // Bind columns to Customer fields
        configureTableColumns();   // Set preferred/minimum widths and auto-resize policy
        setupActionButtons();      // Add Edit/Delete buttons to each row
        setupFiltering();          // Enable search-based filtering
        setupSorting();            // Enable column-based sorting
        loadCustomers();            // Load data from cache into the observable list
        enableCopy();
    }


    /**
     * Configures all standard table columns by binding them to their corresponding fields
     * in the Customer model using reflection (e.g., getName(), getPrice()).
     */
    private void setupTableColumns() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        columnPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        columnAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        columnPriceList.setCellValueFactory(new PropertyValueFactory<>("priceListName"));

    }

    /**
     * Creates a custom cell factory for the "Actions" column that adds "Edit" and "Delete" buttons
     * to each row. Each button is styled and triggers its corresponding handler method using the customer
     * associated with that specific row.
     */
    private void setupActionButtons() {
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(" ערוך ✏ ");
            private final Button deleteButton = new Button("מחק 🗑 ");
            private final HBox actionBox = new HBox(10, editButton, deleteButton);

            {
                editButton.getStyleClass().addAll("edit-button", "action-button", "button");
                deleteButton.getStyleClass().addAll("delete-button", "action-button", "button");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                // If the row is empty or the index is out of bounds, clear the cell
                if (empty || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                    return;
                }

                // Get the Customer instance associated with this row
                Customer currentCustomer = getTableView().getItems().get(getIndex());

                // Set action for the "Edit" button – opens the customer in edit mode
                editButton.setOnAction(event -> editCustomer(currentCustomer));

                // Set action for the "Delete" button – prompts for confirmation and deletes the customer
                deleteButton.setOnAction(event -> deleteCustomer(currentCustomer));

                // Display both buttons in the current table cell
                setGraphic(actionBox);
            }
        });
    }

    /**
     * Sets preferred and minimum widths for each table column based on content type,
     * and applies automatic column resizing to distribute available space.
     */
    private void configureTableColumns() {
        ColumnWidths.setSmartWidth(columnName, ColumnWidths.MID_TXT, ColumnWidths.SHORT_TXT);
        ColumnWidths.setSmartWidth(columnEmail, ColumnWidths.MID_TXT, ColumnWidths.MID_TXT);
        ColumnWidths.setSmartWidth(columnPhone, ColumnWidths.MID_NUM, ColumnWidths.SHORT_NUM);
        ColumnWidths.setSmartWidth(columnAddress, ColumnWidths.MID_TXT, ColumnWidths.MID_TXT);
        ColumnWidths.setSmartWidth(columnActions, ColumnWidths.ACTIONS, ColumnWidths.ACTIONS);

        // Auto-sizes columns to fill the table based on header and visible content.
        //customerTable.setColumnResizePolicy(tv -> true);
        customerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Loads customers from the DataCache into the observable list.
     */
    private void loadCustomers() {
        originalCustomerList.setAll(DataCache.customers);
        if (DataCache.customers.isEmpty()) {
            LoggerManager.logWarning("⚠️ Warning: DataCache.customers is empty — did the DAO run?");
        }

    }

    /**
     * Sets up dynamic filtering of the customer table based on the searchField input.
     * This should be called once during initialization.
     */
    private void setupFiltering() {
        // Listen for changes in the search field's text
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            // Update the filtering condition whenever the input changes
            filteredCustomerList.setPredicate(customer -> {
                // If the search field is empty, show all customers
                if (newValue == null || newValue.isEmpty()) return true;

                // Convert search text to lowercase for case-insensitive matching
                String lowerCaseFilter = newValue.toLowerCase();
                // Check if the customer matches the search text
                // Return true to include the row in the table, false to exclude it.
                return customer.getName().toLowerCase().contains(lowerCaseFilter)
                        || customer.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || customer.getAddress().toLowerCase().contains(lowerCaseFilter)
                        || customer.getPhone().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    /**
     * Connects the sorted data to the table and binds it to the comparator for column-based sorting.
     * The table will automatically sort rows when a column header is clicked.
     */
    private void setupSorting() {
        // Wrap the filtered list to enable dynamic sorting based on user interaction
        SortedList<Customer> sortedData = new SortedList<>(filteredCustomerList);

        // Bind the sorted list's comparator to the table's column sorting logic
        sortedData.comparatorProperty().bind(customerTable.comparatorProperty());

        // Set the sorted list as the data source for the table
        customerTable.setItems(sortedData);
    }

    /**
     * Updates the internal customer list used by the table.
     * This method is typically called once after preloading data,
     * and assumes that filtering and sorting have already been set up.
     *
     * @param customers the list of customers to display
     */
    public void setCustomerList(List<Customer> customers) {
        originalCustomerList.setAll(customers);
    }



    /**
     * Opens the dialog for adding or editing a customer,
     * then updates the observable customer list and the cache accordingly.
     *
     * @param customerToEdit the customer to edit, or null to create a new one
     */
    private void openCustomerDialog(Customer customerToEdit) {
        try {
            // Load the FXML using the AppView enum (centralized path)
            FXMLLoader loader = new FXMLLoader(AppView.ADD_CUSTOMER.getViewUrl());
            Parent root = loader.load();
            AddCustomerController controller = loader.getController();

            // Pass the customer to edit, if applicable
            if (customerToEdit != null) {
                controller.setCustomerToEdit(customerToEdit);
            }

            // Show the dialog window (modal)
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);

            // Apply global application CSS to the dialog (needed since this is a new Scene and doesn't inherit styles)
            scene.getStylesheets().add(AppCSS.APP_STYLE.getCssPath());

            dialogStage.setScene(scene);
            dialogStage.showAndWait();

            // Retrieve the result from the form
            Customer result = controller.getResult();

            if (result != null) {
                if (customerToEdit == null) {
                    // New customer: add to both view and cache
                    originalCustomerList.add(result);
                    DataCache.customers.add(result);
                } else {
                    // Edited customer: replace in both view and cache
                    int listIndex = originalCustomerList.indexOf(customerToEdit);
                    int cacheIndex = DataCache.customers.indexOf(customerToEdit);
                    if (listIndex != -1) originalCustomerList.set(listIndex, result);
                    if (cacheIndex != -1) DataCache.customers.set(cacheIndex, result);
                }
            }

        } catch (IOException e) {
            LoggerManager.logError("Error loading customer dialog", e);
            e.printStackTrace();

        }
    }




    /**
     * Handles the action of adding a new customer.
     */
    @FXML
    private void addCustomer() {
        openCustomerDialog(null);
    }

    /**
     * Opens the edit dialog for the given customer.
     * @param customer the customer to edit
     */
    private void editCustomer(Customer customer) {
        openCustomerDialog(customer);
    }

    /**
     * Deletes the given customer after user confirmation and updates the UI and cache.
     * This avoids a full database reload by directly removing the customer from memory.
     *
     * @param customer the customer to delete
     */
    private void deleteCustomer(Customer customer) {
        // Show a confirmation dialog before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("אישור מחיקה");
        alert.setHeaderText(null);
        alert.setContentText("האם אתה בטוח שברצונך למחוק את \"" + customer.getName() + "\"?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // 1. Delete from the database
                CustomerDAO.deleteCustomer(customer.getId());

                // 2. Remove the customer from the cache (for global use)
                DataCache.customers.remove(customer);

                // 3. Remove the customer from the controller's list (updates the table view)
                originalCustomerList.remove(customer);
            }
        });
    }




    private void enableCopy() {
        customerTable.getSelectionModel().setCellSelectionEnabled(true);
        customerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        customerTable.setOnKeyPressed(event -> {
            if (event.isControlDown() && event.getCode().toString().equals("C")) {
                ObservableList<TablePosition> selectedCells = customerTable.getSelectionModel().getSelectedCells();
                if (selectedCells.isEmpty()) return;

                TablePosition<?, ?> pos = selectedCells.get(0);
                int row = pos.getRow();
                int colIndex = pos.getColumn();

                TableColumn<?, ?> column = customerTable.getColumns().get(colIndex);

                if (column == columnPhone || column == columnEmail) {
                    Object cell = column.getCellData(row);
                    if (cell != null) {
                        ClipboardContent content = new ClipboardContent();
                        content.putString(cell.toString());
                        Clipboard.getSystemClipboard().setContent(content);
                        System.out.println("📋 Copied to clipboard: " + cell);
                    }
                }
            }
        });
    }





}
