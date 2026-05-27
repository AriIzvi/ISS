package concursAtletism.domain;

public class Review {
    private Long id;
    private Long bookId;
    private String userEmail;
    private int rating;
    private String comment;

    public Review() {}

    public Review(Long id, Long bookId, String userEmail, int rating, String comment) {
        this.id = id;
        this.bookId = bookId;
        this.userEmail = userEmail;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters și Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}