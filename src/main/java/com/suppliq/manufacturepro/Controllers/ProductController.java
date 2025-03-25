package com.suppliq.manufacturepro.Controllers;

import com.suppliq.manufacturepro.Database.ProductDAO;
import com.suppliq.manufacturepro.Models.Product;
import com.suppliq.manufacturepro.Utils.LoggerManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductController {

    @FXML private TableView<Product> productTable;
    @FXML private TableColumn<Product, Integer> columnId;
    @FXML private TableColumn<Product, String> columnName;
    @FXML private TableColumn<Product, String> columnDescription;
    @FXML private TableColumn<Product, Double> columnPrice;
    @FXML private TableColumn<Product, Integer> columnStock;
    @FXML private TableColumn<Product, String> columnCategory;
    @FXML private TableColumn<Product, Void> columnActions;
    @FXML private TextField searchField;

    private ObservableList<Product> originalProductList = FXCollections.observableArrayList();

    @FXML private Button addProductButton;

    @FXML
    public void initialize() {
        // Bind table columns to product properties
        columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        columnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        columnStock.setCellValueFactory(new PropertyValueFactory<>("stockQuantity"));
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

        // Add Edit/Delete buttons in each row
        columnActions.setCellFactory(param -> new TableCell<>() {
            private final Button editButton = new Button(" ערוך ✏ ");
            private final Button deleteButton = new Button("מחק 🗑 ");
            private final HBox actionBox = new HBox(10, editButton, deleteButton);

            {
                editButton.getStyleClass().addAll("edit-button", "action-button", "button");
                deleteButton.getStyleClass().addAll("delete-button", "action-button", "button");
                editButton.setOnAction(event -> editProduct(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> deleteProduct(getTableView().getItems().get(getIndex())));
                addProductButton.setOnAction(e -> addProduct());
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });

        // Load data into table
        loadProducts();
    }


    private void loadProducts() {
        originalProductList = ProductDAO.getProducts();
        setupFiltering();
    }

    private void setupFiltering() {
        FilteredList<Product> filteredData = new FilteredList<>(originalProductList, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return product.getName().toLowerCase().contains(lowerCaseFilter)
                        || product.getDescription().toLowerCase().contains(lowerCaseFilter)
                        || product.getCategory().toLowerCase().contains(lowerCaseFilter);
            });

        });

        SortedList<Product> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(productTable.comparatorProperty());

        productTable.setItems(sortedData);
    }

    private void openProductDialog(Product productToEdit) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/suppliq/manufacturepro/Views/add-product.fxml"));
            Parent root = loader.load();

            AddProductController controller = loader.getController();

            if (productToEdit != null) {
                controller.setProductToEdit(productToEdit);
            }

            Stage dialogStage = new Stage();
            LoggerManager.logInfo(String.valueOf(productToEdit == null));
            dialogStage.setTitle(productToEdit == null ? "הוסף מוצר חדש" : "ערוך מוצר");
            LoggerManager.logInfo(dialogStage.getTitle());
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadProducts(); // רענון אחרי פעולה

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addProduct() {
        openProductDialog(null);
    }

    private void editProduct(Product product) {
        openProductDialog(product);
    }


    private void deleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("אישור מחיקה");
        alert.setHeaderText(null);
        alert.setContentText("האם אתה בטוח שברצונך למחוק את \"" + product.getName() + "\"?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ProductDAO.deleteProduct(product.getId());
                loadProducts();
            }
        });
    }

}
