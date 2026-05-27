package concursAtletism.repository;

import concursAtletism.domain.Role;
import concursAtletism.domain.User;
import java.sql.*;

public class UserRepository {
    private ConnectDB connectDB;

    public UserRepository(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public User findBy(String email, String password) {
        String sql = "SELECT id, email, password, role, full_name, date_of_birth, address, phone_number FROM users WHERE email = ? AND password = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getLong("id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role")),
                            rs.getString("full_name"),
                            rs.getString("date_of_birth"),
                            rs.getString("address"),
                            rs.getString("phone_number")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Pasul 9 din diagramă: Salvarea efectivă în SGBD
     */
    public boolean save(User user) {
        String sql = "INSERT INTO users (email, password, role, full_name, date_of_birth, address, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole().name());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getDateOfBirth());
            stmt.setString(6, user.getAddress());
            stmt.setString(7, user.getPhoneNumber());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}