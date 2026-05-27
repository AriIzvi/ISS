package concursAtletism.domain;

public class CartItem {
    private Long id;
    private String userEmail;
    private Book book; // Păstrăm obiectul Book complet ca să-i știm titlul, prețul, coperta etc.
    private int quantity;

    public CartItem(Long id, String userEmail, Book book, int quantity) {
        this.id = id;
        this.userEmail = userEmail;
        this.book = book;
        this.quantity = quantity;
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}