package concursAtletism.repository;

import concursAtletism.domain.Book;
import concursAtletism.domain.Review;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import concursAtletism.domain.PurchasedBook;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    /**
     * Salvează o recenzie nouă în baza de date conform diagramei [Add review]
     */
    public boolean saveReview(Review review) {
        String sql = "INSERT INTO reviews (book_id, user_email, rating, comment) VALUES (?, ?, ?, ?)";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, review.getBookId());
            stmt.setString(2, review.getUserEmail());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Aduce cel mult 2 recenzii pentru o anumită carte din baza de date
     */
    public List<Review> findTop2ReviewsByBookId(Long bookId) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT id, book_id, user_email, rating, comment FROM reviews WHERE book_id = ? LIMIT 2";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, bookId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(new Review(
                            rs.getLong("id"),
                            rs.getLong("book_id"),
                            rs.getString("user_email"),
                            rs.getInt("rating"),
                            rs.getString("comment")
                    ));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la incarcarea recenziilor: " + e.getMessage());
        }
        return reviews;
    }

    /**
     * Salvează o carte în librăria personală a utilizatorului după plată
     */
    public boolean savePurchase(String email, Long bookId) {
        String sql = "INSERT INTO purchased_books (user_email, book_id, purchase_date) VALUES (?, ?, ?)";
        String currentDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setLong(2, bookId);
            stmt.setString(3, currentDate);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Returnează toate cărțile cumpărate de un utilizator pentru Librăria Personală
     */
    public List<Book> findPurchasedBooksByEmail(String email) {
        List<Book> purchased = new ArrayList<>();
        String sql = "SELECT b.* FROM books b JOIN purchased_books pb ON b.id = pb.book_id WHERE pb.user_email = ?";

        try (Connection conn = connectDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    purchased.add(new Book(
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
        return purchased;
    }
}