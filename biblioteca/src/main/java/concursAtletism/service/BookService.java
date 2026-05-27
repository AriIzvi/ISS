package concursAtletism.service;

import concursAtletism.domain.Book;
import concursAtletism.domain.Review;
import concursAtletism.repository.BookRepository;

import java.util.List;

public class BookService {
    private BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Metoda handleSearch identificată în diagrama [Search]
     *
     */
    public Book handleSearch(String attribute) {
        // Căutăm în repo (care verifică și titlu și autor)
        List<Book> foundBooks = bookRepository.findByAttribute(attribute);

        if (!foundBooks.isEmpty()) {
            // În diagramă se returnează un singur obiect 'book' pentru a fi afișat în BookWindow
            return foundBooks.get(0);
        }
        return null;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    /**
     * Metoda handleImport identificată în diagrama [Import]
     *
     */
    public boolean handleImport(List<Book> books) {
        try {
            for (Book b : books) {
                bookRepository.save(b);
            }
            // Returnează 'done' (true) ca să afișeze mesajul de succes în AdminWindow
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Înregistrează recenzia primită din UI în stratul de persistență
     */
    public boolean handleRegisterReview(Review review) {
        // Aici se pot face și alte validări dacă e cazul (ex: nota să fie între 1 și 5)
        if (review.getRating() < 1 || review.getRating() > 5) {
            return false;
        }
        return bookRepository.saveReview(review);
    }

    /**
     * Returnează top 2 recenzii pentru o carte dată
     */
    public List<Review> getTop2ReviewsForBook(Long bookId) {
        if (bookId == null) {
            return new java.util.ArrayList<>();
        }
        return bookRepository.findTop2ReviewsByBookId(bookId);
    }

    /**
     * Înregistrează cumpărarea unei liste de cărți (coșul finalizat)
     */
    public void registerPurchase(String email, List<Long> bookIds) {
        for (Long id : bookIds) {
            bookRepository.savePurchase(email, id);
        }
    }

    /**
     * Preia cărțile cumpărate de un client
     */
    public List<Book> getMyLibrary(String email) {
        if (email == null) return new java.util.ArrayList<>();
        return bookRepository.findPurchasedBooksByEmail(email);
    }
}