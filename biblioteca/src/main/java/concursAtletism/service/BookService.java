package concursAtletism.service;

import concursAtletism.domain.Book;
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
}