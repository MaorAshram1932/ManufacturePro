package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.DataCache;
import com.suppliq.manufacturepro.Database.PriceListDAO;
import com.suppliq.manufacturepro.Database.PriceListItemDAO;
import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Models.Customer;
import com.suppliq.manufacturepro.Models.PriceList;
import com.suppliq.manufacturepro.Models.PriceListItem;
import com.suppliq.manufacturepro.Models.Product;
import com.suppliq.manufacturepro.Utils.ColumnWidths;
import com.suppliq.manufacturepro.Utils.LoggerManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PriceListController {

    @FXML private TableView<PriceList> priceListTable;
    @FXML private TableColumn<PriceList, String> columnPriceListName;
    @FXML private TableColumn<PriceList, Void> columnActions;

    @FXML private TableView<PriceListItem> priceListItemTable;
    @FXML private TableColumn<PriceListItem, String> columnProductName;
    @FXML private TableColumn<PriceListItem, Double> columnPrice;
    @FXML private TextField searchField;

    // Observable list of all price lists – automatically syncs with UI
    private final ObservableList<PriceList> priceLists = FXCollections.observableArrayList();

    // Observable list of all price list items – automatically syncs with UI
    private final ObservableList<PriceListItem> priceListItems = FXCollections.observableArrayList();
    private final Map<Integer, String> productNameMap = new HashMap<>();

    /**
     * Initializes the priceList controller.
     */
    @FXML
    public void initialize() {
        setupTableColumns();      // Bind table columns to model fields
        configureTableColumns();  // Set up columns and value factories
        setupActionButtons();     // Add "Edit" and "Delete" buttons to each row
        loadProductNames();       // Load all product names into local map for display
        loadPriceLists();         // Load all price lists into the main table
        configureRowSelection();  // Handle user row clicks
        setupFiltering();         // Enable live search filtering by name
    }


    /**
     * Binds all table columns to the corresponding model fields
     * using PropertyValueFactory or custom logic where needed.
     */
    private void setupTableColumns() {
        // PriceList table (overview)
        columnPriceListName.setCellValueFactory(new PropertyValueFactory<>("name"));

        // PriceListItem table (items per price list)
        columnProductName.setCellValueFactory(cellData -> {
            int productId = cellData.getValue().getProductId();
            String name = productNameMap.getOrDefault(productId, "לא ידוע");
            return new SimpleStringProperty(name);
        });

        columnPrice.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPrice()).asObject());
    }

    /**
     * Configures column widths, layout policies, and table resize behavior.
     */
    private void configureTableColumns() {
        // PriceList table (overview)
        ColumnWidths.setSmartWidth(columnPriceListName, ColumnWidths.MID_TXT, ColumnWidths.SHORT_TXT);
        ColumnWidths.setSmartWidth(columnActions, ColumnWidths.MID_TXT, ColumnWidths.SHORT_TXT);

        // PriceListItem table (items)
        ColumnWidths.setSmartWidth(columnProductName, ColumnWidths.LONG_TXT, ColumnWidths.MID_TXT);
        ColumnWidths.setSmartWidth(columnPrice, ColumnWidths.MID_NUM, ColumnWidths.SHORT_NUM);

        // Set table resize policies
        priceListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        priceListItemTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    /**
     * Creates a custom cell factory for the "Actions" column that adds "Edit" and "Delete" buttons
     * to each row. Each button is styled and triggers its corresponding handler method using the price list
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

                // Get the PriceList instance associated with this row
                PriceList currentPriceList = getTableView().getItems().get(getIndex());

                // Set action for the "Edit" button – opens the price list in edit mode
                editButton.setOnAction(event -> editPriceList(currentPriceList));

                // Set action for the "Delete" button – prompts for confirmation and deletes the price list
                //deleteButton.setOnAction(event -> deletePriceList(currentPriceList));

                // Display both buttons in the current table cell
                setGraphic(actionBox);
            }
        });
    }

    /**
     * Loads all product names into a local map (product ID → name)
     * for display in the price list items table.
     */
    private void loadProductNames() {
        for (Product p : DataCache.products) {
            productNameMap.put(p.getId(), p.getName());
        }
    }

    /**
     * Loads price lists from the DataCache into the observable list.
     */
    private void loadPriceLists() {
        priceLists.setAll(DataCache.priceLists);
        if (DataCache.priceLists.isEmpty()) {
            LoggerManager.logWarning("⚠️ Warning: DataCache.priceLists is empty — did the DAO run?");
        }
    }


    /**
     * Combines all products with their corresponding prices for the selected price list.
     * If a special price exists for a product, it will be used; otherwise, the base product price is used.
     * The merged list is displayed in the lower table (priceListItemTable).
     *
     * @param priceListId the ID of the selected price list to display
     */
    private void showMergedItemsForPriceList(int priceListId) {
        // Create a map of product ID → special price for the selected price list
        Map<Integer, Double> specialPrices = PriceListItemDAO.getItemsForPriceList(priceListId).stream()
                .collect(Collectors.toMap(PriceListItem::getProductId, PriceListItem::getPrice));

        // Prepare a merged list that includes all products with the correct final price
        ObservableList<PriceListItem> mergedList = FXCollections.observableArrayList();

        for (Product product : DataCache.products) {
            // Use special price if available; otherwise fallback to base product price
            double finalPrice = specialPrices.getOrDefault(product.getId(), product.getPrice());
            mergedList.add(new PriceListItem(priceListId, product.getId(), finalPrice));
        }

        // Update the observable list and bind it to the item table
        priceListItems.setAll(mergedList);
        priceListItemTable.setItems(priceListItems);
    }

    private void configureRowSelection() {
        priceListTable.setRowFactory(tv -> {
            TableRow<PriceList> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 1) {
                    PriceList selected = row.getItem();
                    showMergedItemsForPriceList(selected.getId());
                }
            });
            return row;
        });
    }


    private void setupFiltering() {
        FilteredList<PriceList> filteredData = new FilteredList<>(priceLists, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(priceList -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return priceList.getName().toLowerCase().contains(lowerCaseFilter);
            });
        });

        SortedList<PriceList> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(priceListTable.comparatorProperty());

        priceListTable.setItems(sortedData);
    }


    private void openPriceListDialog(PriceList priceListToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/suppliq/manufacturepro/Views/add-pricelist-view.fxml"));
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("הוסף מחירון חדש");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            // רענון רשימת המחירונים
            loadPriceLists();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addPriceList() {
        openPriceListDialog(null);
    }

    private void editPriceList(PriceList priceList) {
        openPriceListDialog(priceList);
    }


    public void setPriceListList(List<PriceList> priceLists) {
        this.priceLists.setAll(priceLists);
    }



}
