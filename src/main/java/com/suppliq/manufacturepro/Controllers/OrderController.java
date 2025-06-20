package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Base.AppCSS;
import com.suppliq.manufacturepro.Database.DataCache;
import com.suppliq.manufacturepro.Database.OrderDAO;
import com.suppliq.manufacturepro.Models.Order;
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
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class OrderController {

    @FXML private TableView<Order> orderTable;
    @FXML private TableColumn<Order, String> columnOrderId;
    @FXML private TableColumn<Order, String> columnCustomerName;
    @FXML private TableColumn<Order, Double> columnOrderDate;
    @FXML private TableColumn<Order, Integer> columnStatus;
    @FXML private TableColumn<Order, String> columnTotalAmount;
    @FXML private TableColumn<Order, Void> columnActions;
    @FXML private TextField searchField;

    // Observable list of all orders – automatically syncs with UI
    private final ObservableList<Order> originalOrderList = FXCollections.observableArrayList();
    // Filtered view of the order list
    private final FilteredList<Order> filteredOrderList = new FilteredList<>(originalOrderList, p -> true);

    /**
     * Initializes the order controller.
     */
    @FXML
    public void initialize() {
        setupTableColumns();       // Bind columns to Order fields
        configureTableColumns();   // Set preferred/minimum widths and auto-resize policy
        setupActionButtons();      // Add Edit/Delete buttons to each row
        setupFiltering();          // Enable search-based filtering
        setupSorting();            // Enable column-based sorting
        loadOrders();            // Load data from cache into the observable list
    }

    /**
     * Configures all standard table columns by binding them to their corresponding fields
     * in the Order model using reflection.
     */
    private void setupTableColumns() {
        columnOrderId.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnCustomerName.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnOrderDate.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnStatus.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        columnTotalAmount.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    /**
     * Creates a custom cell factory for the "Actions" column that adds "Edit" and "Delete" buttons
     * to each row. Each button is styled and triggers its corresponding handler method using the order
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

                // Get the Order instance associated with this row
                Order currentOrder = getTableView().getItems().get(getIndex());

                // Set action for the "Edit" button – opens the order in edit mode
                editButton.setOnAction(event -> editOrder(currentOrder));

                // Set action for the "Delete" button – prompts for confirmation and deletes the order
                deleteButton.setOnAction(event -> deleteOrder(currentOrder));

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
        ColumnWidths.setSmartWidth(columnOrderId, ColumnWidths.MID_TXT, ColumnWidths.SHORT_TXT);
        ColumnWidths.setSmartWidth(columnCustomerName, ColumnWidths.LONG_TXT, ColumnWidths.MID_TXT);
        ColumnWidths.setSmartWidth(columnOrderDate, ColumnWidths.MID_NUM, ColumnWidths.SHORT_NUM);
        ColumnWidths.setSmartWidth(columnStatus, ColumnWidths.SHORT_NUM, ColumnWidths.SHORT_NUM);
        ColumnWidths.setSmartWidth(columnTotalAmount, ColumnWidths.MID_TXT, ColumnWidths.SHORT_TXT);
        ColumnWidths.setSmartWidth(columnActions, ColumnWidths.ACTIONS, ColumnWidths.ACTIONS);

        // Auto-sizes columns to fill the table based on header and visible content.
        //orderTable.setColumnResizePolicy(tv -> true);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Loads orders from the DataCache into the observable list.
     */
    private void loadOrders() {
        originalOrderList.setAll(DataCache.orders);
        if (DataCache.orders.isEmpty()) {
            LoggerManager.logWarning("⚠️ Warning: DataCache.orders is empty — did the DAO run?");
        }

    }

    /**
     * Sets up dynamic filtering of the order table based on the searchField input.
     * This should be called once during initialization.
     */
    private void setupFiltering() {
        // Listen for changes in the search field's text
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            // Update the filtering condition whenever the input changes
            filteredOrderList.setPredicate(order -> {
                // If the search field is empty, show all orders
                if (newValue == null || newValue.isEmpty()) return true;

                // Convert search text to lowercase for case-insensitive matching
                String lowerCaseFilter = newValue.toLowerCase();
                // Check if the order matches the search text
                // Return true to include the row in the table, false to exclude it.
                return String.valueOf(order.getId()).contains(lowerCaseFilter)
                        || order.getCustomerName().toLowerCase().contains(lowerCaseFilter)
                        || order.getStatus().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    /**
     * Connects the sorted data to the table and binds it to the comparator for column-based sorting.
     * The table will automatically sort rows when a column header is clicked.
     */
    private void setupSorting() {
        // Wrap the filtered list to enable dynamic sorting based on user interaction
        SortedList<Order> sortedData = new SortedList<>(filteredOrderList);

        // Bind the sorted list's comparator to the table's column sorting logic
        sortedData.comparatorProperty().bind(orderTable.comparatorProperty());

        // Set the sorted list as the data source for the table
        orderTable.setItems(sortedData);
    }

    /**
     * Updates the internal order list used by the table.
     * This method is typically called once after preloading data,
     * and assumes that filtering and sorting have already been set up.
     *
     * @param orders the list of orders to display
     */
    public void setOrderList(List<Order> orders) {
        originalOrderList.setAll(orders);
    }



    /**
     * Opens the dialog for adding or editing an order,
     * then updates the observable order list and the cache accordingly.
     *
     * @param orderToEdit the order to edit, or null to create a new one
     */
    private void openOrderDialog(Order orderToEdit) {
        try {
            // Load the FXML using the AppView enum (centralized path)
            FXMLLoader loader = new FXMLLoader(AppView.ADD_PRODUCT.getViewUrl());
            Parent root = loader.load();
            AddOrderController controller = loader.getController();




            // Pass the order to edit, if applicable
            if (orderToEdit != null) {
               // controller.setOrderToEdit(orderToEdit);
            }

            // Show the dialog window (modal)
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);

            // Apply global application CSS to the dialog (needed since this is a new Scene and doesn't inherit styles)
            scene.getStylesheets().add(AppCSS.APP_STYLE.getCssPath());

            dialogStage.setScene(scene);
            dialogStage.showAndWait();


            // Retrieve the result from the form
            //Order result = controller.getResult();
            Order result = null;

            if (result != null) {
                if (orderToEdit == null) {
                    // New order: add to both view and cache
                    originalOrderList.add(result);
                    DataCache.orders.add(result);
                } else {
                    // Edited order: replace in both view and cache
                    int listIndex = originalOrderList.indexOf(orderToEdit);
                    int cacheIndex = DataCache.orders.indexOf(orderToEdit);
                    if (listIndex != -1) originalOrderList.set(listIndex, result);
                    if (cacheIndex != -1) DataCache.orders.set(cacheIndex, result);
                }
            }

        } catch (IOException e) {
            LoggerManager.logError("Error loading order dialog", e);
            e.printStackTrace();

        }
    }




    /**
     * Handles the action of adding a new order.
     */
    @FXML
    private void addOrder() {
        openOrderDialog(null);
    }

    /**
     * Opens the edit dialog for the given order.
     * @param order the order to edit
     */
    private void editOrder(Order order) {
        openOrderDialog(order);
    }

    /**
     * Deletes the given order after user confirmation and updates the UI and cache.
     * This avoids a full database reload by directly removing the order from memory.
     *
     * @param order the order to delete
     */
    private void deleteOrder(Order order) {
        // Show a confirmation dialog before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("אישור מחיקה");
        alert.setHeaderText(null);
        alert.setContentText("האם אתה בטוח שברצונך למחוק את \"" + order.getId() + "\"?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // 1. Delete from the database
                OrderDAO.deleteOrder(order.getId());

                // 2. Remove the order from the cache (for global use)
                DataCache.orders.remove(order);

                // 3. Remove the order from the controller's list (updates the table view)
                originalOrderList.remove(order);
            }
        });
    }



}
