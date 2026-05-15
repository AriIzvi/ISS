package concursAtletism.domain;

public class Book {
    private Long id;
    private String title;
    private String author;
    private String isbn;
    private String genre;
    private String cover;

    public Book() {}

    public Book(Long id, String title, String author, String isbn, String genre, String cover) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.genre = genre;
        this.cover = cover;
    }

    // Getters și Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getCover() { return cover; }
    public void setCover(String cover) { this.cover = cover; }

    @Override
    public String toString() {
        return "Book{" + "title='" + title + '\'' + ", author='" + author + '\'' + ", isbn='" + isbn + '\'' + '}';
    }
}
