package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Models.PriceListItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceListItemDAO {

    public static List<PriceListItem> getAllItems() {
        List<PriceListItem> items = new ArrayList<>();
        String sql = "SELECT * FROM price_list_items";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                PriceListItem item = new PriceListItem(
                        rs.getInt("id"),
                        rs.getInt("price_list_id"),
                        rs.getInt("product_id"),
                        rs.getDouble("price")
                );
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static List<PriceListItem> getItemsForPriceList(int priceListId) {
        List<PriceListItem> items = new ArrayList<>();
        String sql = "SELECT * FROM price_list_items WHERE price_list_id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, priceListId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                PriceListItem item = new PriceListItem(
                        rs.getInt("id"),
                        rs.getInt("price_list_id"),
                        rs.getInt("product_id"),
                        rs.getDouble("price")
                );
                items.add(item);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }

    public static void insertItem(PriceListItem item) {
        String sql = "INSERT INTO price_list_items (price_list_id, product_id, price) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getPriceListId());
            stmt.setInt(2, item.getProductId());
            stmt.setDouble(3, item.getPrice());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateItem(PriceListItem item) {
        String sql = "UPDATE price_list_items SET price_list_id = ?, product_id = ?, price = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getPriceListId());
            stmt.setInt(2, item.getProductId());
            stmt.setDouble(3, item.getPrice());
            stmt.setInt(4, item.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteItem(int id) {
        String sql = "DELETE FROM price_list_items WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
