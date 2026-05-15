package concursAtletism.repository;

import concursAtletism.domain.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookRepository {
    private ConnectDB connectDB;

    public BookRepository(ConnectDB connectDB) {
        this.connectDB = connectDB;
    }

    /**
     * Metoda findByAttribute identificată în diagrama de secvență [Search]
     * Caută textul primit atât în coloana 'title', cât și în 'author'
     */
    public List<Book> findByAttribute(String attribute) {
        List<Book> books = new ArrayList<>();
        // Folosim LIKE cu % ca să găsească și potriviri parțiale (ex: "Emin" găsește "Eminescu")
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + attribute + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(new Book(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("isbn"),
                            rs.getString("genre"),
                            rs.getString("cover")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = connectDB.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                books.add(new Book(
                        rs.getLong("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("genre"),
                        rs.getString("cover")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la findAll: " + e.getMessage());
        }
        return books;
    }

    /**
     * Metoda pentru Importul masiv de cărți observat în diagrama [Import]
     */
    public void save(Book book) {
        // AM ADĂUGAT: un al cincilea "?" pentru cover
        String sql = "INSERT INTO books (title, author, isbn, genre, cover) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getIsbn());
            stmt.setString(4, book.getGenre());
            stmt.setString(5, book.getCover()); // Acum indexul 5 corespunde celui de-al 5-lea '?'

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}