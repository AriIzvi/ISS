package concursAtletism.repository;

import concursAtletism.domain.Role;
import concursAtletism.domain.User;
import java.sql.*;

public class UserRepository {
    // Aici avem obiectul nostru de conexiune
    private ConnectDB connectDB;

    // Constructorul primește acum obiectul ConnectDB
    public UserRepository(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public User findBy(String email, String password) {
        String sql = "SELECT id, email, password, role FROM users WHERE email = ? AND password = ?";

        // Folosim obiectul pentru a obține conexiunea
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
                            Role.valueOf(rs.getString("role"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}