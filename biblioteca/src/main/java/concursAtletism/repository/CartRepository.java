package concursAtletism.repository;

import concursAtletism.domain.Book;
import concursAtletism.domain.CartItem;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    private ConnectDB connectDB;

    public CartRepository(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    public List<CartItem> findByUserEmail(String email) {
        List<CartItem> items = new ArrayList<>();
        String sql = "SELECT c.id, c.user_email, c.quantity, b.id as book_id, b.title, b.author, b.isbn, b.genre, b.cover " +
                "FROM cart_items c JOIN books b ON c.book_id = b.id WHERE c.user_email = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book book = new Book(
                            rs.getLong("book_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getString("genre"),
                            rs.getString("cover")
                    );
                    items.add(new CartItem(rs.getLong("id"), rs.getString("user_email"), book, rs.getInt("quantity")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return items;
    }

    public boolean save(String email, Long bookId) {
        // Dacă există deja cartea în coș, doar îi creștem cantitatea
        String checkSql = "SELECT id, quantity FROM cart_items WHERE user_email = ? AND book_id = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, email);
            checkStmt.setLong(2, bookId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    String updateSql = "UPDATE cart_items SET quantity = quantity + 1 WHERE id = ?";
                    try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                        updateStmt.setLong(1, rs.getLong("id"));
                        return updateStmt.executeUpdate() > 0;
                    }
                }
            }

            // Dacă nu există, o inserăm de la zero
            String insertSql = "INSERT INTO cart_items (user_email, book_id, quantity) VALUES (?, ?, 1)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, email);
                insertStmt.setLong(2, bookId);
                return insertStmt.executeUpdate() > 0;
            }
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean delete(Long cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, cartItemId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}