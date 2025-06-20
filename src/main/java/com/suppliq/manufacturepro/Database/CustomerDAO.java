package com.suppliq.manufacturepro.Database;

import com.suppliq.manufacturepro.Models.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class CustomerDAO {

    public static ObservableList<Customer> getCustomers() {
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        String query = "SELECT c.*, p.name AS price_list_name " +
                "FROM customers c " +
                "LEFT JOIN price_lists p ON c.price_list_id = p.id";

        try (Connection conn = DatabaseConnector.getConnection()) {
            assert conn != null;
            try (PreparedStatement stmt = conn.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    Customer customer = new Customer(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("address"),
                            rs.getInt("price_list_id")
                    );
                    customer.setPriceListName(rs.getString("price_list_name") != null ? rs.getString("price_list_name") : "מחירון רגיל");
                    customers.add(customer);
                }

            }
        } catch (SQLException e) {
            System.err.println("❌ Error fetching customers: " + e.getMessage());
        }

        return customers;
    }

    public static void insertCustomer(Customer customer) {
        String query = "INSERT INTO customers (name, email, phone, address, price_list_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getAddress());

            if (customer.getPriceListId() > 0) {
                stmt.setInt(5, customer.getPriceListId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error inserting customer: " + e.getMessage());
        }
    }

    public static void updateCustomer(Customer customer) {
        String query = "UPDATE customers SET name = ?, email = ?, phone = ?, address = ?, price_list_id = ? WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getEmail());
            stmt.setString(3, customer.getPhone());
            stmt.setString(4, customer.getAddress());

            if (customer.getPriceListId() > 0) {
                stmt.setInt(5, customer.getPriceListId());
            } else {
                stmt.setNull(5, Types.INTEGER);
            }

            stmt.setInt(6, customer.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error updating customer: " + e.getMessage());
        }
    }

    public static void deleteCustomer(int customerId) {
        String query = "DELETE FROM customers WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("❌ Error deleting customer: " + e.getMessage());
        }
    }
}
