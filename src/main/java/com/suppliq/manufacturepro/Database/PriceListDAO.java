package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Models.PriceList;
import com.suppliq.manufacturepro.Utils.LoggerManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PriceListDAO {

    public static List<PriceList> getAllPriceLists() {
        List<PriceList> priceLists = new ArrayList<>();
        String sql = "SELECT * FROM price_lists";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                priceLists.add(new PriceList(id, name));
            }
        } catch (SQLException e) {
            LoggerManager.logError("Error fetching price lists", e);
        }

        return priceLists;
    }

    public static void addPriceList(PriceList priceList) {
        String sql = "INSERT INTO price_lists (name) VALUES (?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, priceList.getName());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logError("Error adding price list", e);
        }
    }

    public static void updatePriceList(PriceList priceList) {
        String sql = "UPDATE price_lists SET name = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, priceList.getName());
            pstmt.setInt(2, priceList.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logError("Error updating price list", e);
        }
    }

    public static void deletePriceList(int id) {
        String sql = "DELETE FROM price_lists WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            LoggerManager.logError("Error deleting price list", e);
        }
    }
}
