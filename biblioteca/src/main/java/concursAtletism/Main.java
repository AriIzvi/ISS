package concursAtletism;

import concursAtletism.UI.LoginWindow;
import concursAtletism.repository.CartRepository;
import concursAtletism.repository.ConnectDB;
import concursAtletism.repository.UserRepository;
import concursAtletism.repository.BookRepository;
import concursAtletism.service.CartService;
import concursAtletism.service.UserService;
import concursAtletism.service.BookService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // 1. Inițializăm conexiunea la SQLite
        ConnectDB connection = new ConnectDB("jdbc:sqlite:/Users/izvia/Documents/ISS/biblioteca/biblioteca.db");

        // 2. Inițializăm Repository-urile
        UserRepository userRepo = new UserRepository(connection);
        BookRepository bookRepo = new BookRepository(connection);
        CartRepository cartRepository = new CartRepository(connection);

        // 3. Inițializăm Service-urile
        UserService userService = new UserService(userRepo);
        BookService bookService = new BookService(bookRepo);
        CartService cartService = new CartService(cartRepository);

        // 4. Pornim interfața grafică (LoginWindow)
        SwingUtilities.invokeLater(() -> {
            LoginWindow login = new LoginWindow(userService, bookService, cartService);
            login.setVisible(true);
        });
    }
}