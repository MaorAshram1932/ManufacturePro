package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Base.AppCSS;
import com.suppliq.manufacturepro.Database.DataCache;
import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Models.Product;
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

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, String> columnName;
    @FXML private TableColumn<Product, String> columnDescription;
    @FXML private TableColumn<Product, Double> columnPrice;
    @FXML private TableColumn<Product, Integer> columnStock;
    @FXML private TableColumn<Product, String> columnCategory;
    @FXML private TableColumn<Product, Void> columnActions;
    @FXML private TextField searchField;

    // Observable list of all products – automatically syncs with UI
    private final ObservableList<Product> originalProductList = FXCollections.observableArrayList();
    // Filtered view of the product list
    private final FilteredList<Product> filteredProductList = new FilteredList<>(originalProductList, p -> true);

    /**
     * Initializes the product controller.
     */
    @FXML
    public void initialize() {
        setupTableColumns();       // Bind columns to Product fields
        configureTableColumns();   // Set preferred/minimum widths and auto-resize policy
        setupActionButtons();      // Add Edit/Delete buttons to each row
        setupFiltering();          // Enable search-based filtering
        setupSorting();            // Enable column-based sorting
        loadProducts();            // Load data from cache into the observable list
    }


    /**
     * Configures all standard table columns by binding them to their corresponding fields
     * in the Product model using reflection (e.g., getName(), getPrice()).
     */
    private void setupTableColumns() {
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    /**
     * Creates a custom cell factory for the "Actions" column that adds "Edit" and "Delete" buttons
     * to each row. Each button is styled and triggers its corresponding handler method using the product
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

                // Get the Product instance associated with this row
                Product currentProduct = getTableView().getItems().get(getIndex());

                // Set action for the "Edit" button – opens the product in edit mode
                editButton.setOnAction(event -> editProduct(currentProduct));

                // Set action for the "Delete" button – prompts for confirmation and deletes the product
                deleteButton.setOnAction(event -> deleteProduct(currentProduct));

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
        ColumnWidths.setSmartWidth(columnDescription, ColumnWidths.LONG_TXT, ColumnWidths.MID_TXT);
        ColumnWidths.setSmartWidth(columnPrice, ColumnWidths.MID_NUM, ColumnWidths.SHORT_NUM);
        ColumnWidths.setSmartWidth(columnStock, ColumnWidths.SHORT_NUM, ColumnWidths.SHORT_NUM);
        ColumnWidths.setSmartWidth(columnCategory, ColumnWidths.MID_TXT, ColumnWidths.SHORT_TXT);
        ColumnWidths.setSmartWidth(columnActions, ColumnWidths.ACTIONS, ColumnWidths.ACTIONS);

        // Auto-sizes columns to fill the table based on header and visible content.
         //productTable.setColumnResizePolicy(tv -> true);
         productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Loads products from the DataCache into the observable list.
     */
    private void loadProducts() {
        originalProductList.setAll(DataCache.products);
        if (DataCache.products.isEmpty()) {
            LoggerManager.logWarning("⚠️ Warning: DataCache.products is empty — did the DAO run?");
        }

    }

    /**
     * Sets up dynamic filtering of the product table based on the searchField input.
     * This should be called once during initialization.
     */
    private void setupFiltering() {
        // Listen for changes in the search field's text
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {

            // Update the filtering condition whenever the input changes
            filteredProductList.setPredicate(product -> {
                // If the search field is empty, show all products
                if (newValue == null || newValue.isEmpty()) return true;

                // Convert search text to lowercase for case-insensitive matching
                String lowerCaseFilter = newValue.toLowerCase();
                // Check if the product matches the search text
                // Return true to include the row in the table, false to exclude it.
                return product.getName().toLowerCase().contains(lowerCaseFilter)
                        || product.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || product.getCategory().toLowerCase().contains(lowerCaseFilter);
            });
        });
    }

    /**
     * Connects the sorted data to the table and binds it to the comparator for column-based sorting.
     * The table will automatically sort rows when a column header is clicked.
     */
    private void setupSorting() {
        // Wrap the filtered list to enable dynamic sorting based on user interaction
        SortedList<Product> sortedData = new SortedList<>(filteredProductList);

        // Bind the sorted list's comparator to the table's column sorting logic
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());

        // Set the sorted list as the data source for the table
        productTable.setItems(sortedData);
    }

    /**
     * Updates the internal product list used by the table.
     * This method is typically called once after preloading data,
     * and assumes that filtering and sorting have already been set up.
     *
     * @param products the list of products to display
     */
    public void setProductList(List<Product> products) {
        originalProductList.setAll(products);
    }



    /**
     * Opens the dialog for adding or editing a product,
     * then updates the observable product list and the cache accordingly.
     *
     * @param productToEdit the product to edit, or null to create a new one
     */
    private void openProductDialog(Product productToEdit) {
        try {
            // Load the FXML using the AppView enum (centralized path)
            FXMLLoader loader = new FXMLLoader(AppView.ADD_PRODUCT.getViewUrl());
            Parent root = loader.load();
            AddProductController controller = loader.getController();




            // Pass the product to edit, if applicable
            if (productToEdit != null) {
                controller.setProductToEdit(productToEdit);
            }

            // Show the dialog window (modal)
            Stage dialogStage = new Stage();
            Scene scene = new Scene(root);

            // Apply global application CSS to the dialog (needed since this is a new Scene and doesn't inherit styles)
            scene.getStylesheets().add(AppCSS.APP_STYLE.getCssPath());

            dialogStage.setScene(scene);
            dialogStage.showAndWait();


            // Retrieve the result from the form
            Product result = controller.getResult();

            if (result != null) {
                if (productToEdit == null) {
                    // New product: add to both view and cache
                    originalProductList.add(result);
                    DataCache.products.add(result);
                } else {
                    // Edited product: replace in both view and cache
                    int listIndex = originalProductList.indexOf(productToEdit);
                    int cacheIndex = DataCache.products.indexOf(productToEdit);
                    if (listIndex != -1) originalProductList.set(listIndex, result);
                    if (cacheIndex != -1) DataCache.products.set(cacheIndex, result);
                }
            }

        } catch (IOException e) {
            LoggerManager.logError("Error loading product dialog", e);
            e.printStackTrace();

        }
    }




    /**
     * Handles the action of adding a new product.
     */
    @FXML
    private void addProduct() {
        openProductDialog(null);
    }

    /**
     * Opens the edit dialog for the given product.
     * @param product the product to edit
     */
    private void editProduct(Product product) {
        openProductDialog(product);
    }

    /**
     * Deletes the given product after user confirmation and updates the UI and cache.
     * This avoids a full database reload by directly removing the product from memory.
     *
     * @param product the product to delete
     */
    private void deleteProduct(Product product) {
        // Show a confirmation dialog before deletion
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("אישור מחיקה");
        alert.setHeaderText(null);
        alert.setContentText("האם אתה בטוח שברצונך למחוק את \"" + product.getName() + "\"?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // 1. Delete from the database
                ProductDAO.deleteProduct(product.getId());

                // 2. Remove the product from the cache (for global use)
                DataCache.products.remove(product);

                // 3. Remove the product from the controller's list (updates the table view)
                originalProductList.remove(product);
            }
        });
    }



}
