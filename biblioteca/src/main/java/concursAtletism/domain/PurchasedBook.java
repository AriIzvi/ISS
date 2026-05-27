package concursAtletism.domain;

public class PurchasedBook {
    private Long id;
    private String userEmail;
    private Book book;
    private String purchaseDate;

    public PurchasedBook(Long id, String userEmail, Book book, String purchaseDate) {
        this.id = id;
        this.userEmail = userEmail;
        this.book = book;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() { return id; }
    public String getUserEmail() { return userEmail; }
    public Book getBook() { return book; }
    public String getPurchaseDate() { return purchaseDate; }
}