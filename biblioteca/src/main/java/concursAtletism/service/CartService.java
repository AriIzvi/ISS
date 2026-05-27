package concursAtletism.service;

import concursAtletism.domain.CartItem;
import concursAtletism.repository.CartRepository;
import java.util.List;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CartService {
    private CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartItem> getCartForUser(String email) {
        return cartRepository.findByUserEmail(email);
    }

    public boolean addToCart(String email, Long bookId) {
        if (email == null || bookId == null) return false;
        return cartRepository.save(email, bookId);
    }

    public boolean removeFromCart(Long cartItemId) {
        return cartRepository.delete(cartItemId);
    }

    /**
     * Validează datele cardului introduse de utilizator.
     * Returnează un string cu mesajul de eroare, sau null dacă totul e în regulă.
     */
    public String handlePayment(String cardNumber, String expiryDate, String cvv) {
        // 1. Validare Număr Card (trebuie să aibă exact 16 cifre, fără spații)
        String cleanCardNumber = cardNumber.replaceAll("\\s+", "");
        if (!cleanCardNumber.matches("^\\d{16}$")) {
            return "Numărul cardului trebuie să conțină exact 16 cifre!";
        }

        // 2. Validare CVV (exact 3 cifre)
        if (!cvv.matches("^\\d{3}$")) {
            return "Codul CVV trebuie să fie format din exact 3 cifre!";
        }

        // 3. Validare Dată Expirare (format MM/YY sau MM/YYYY și să fie în viitor)
        if (!expiryDate.matches("^(0[1-9]|1[0-2])/([0-9]{2}|[0-9]{4})$")) {
            return "Formatul datei de expirare trebuie să fie MM/YY sau MM/YYYY (ex: 12/28)!";
        }

        try {
            String[] parts = expiryDate.split("/");
            int month = Integer.parseInt(parts[0]);
            int year = Integer.parseInt(parts[1]);

            // Ajustăm anul dacă e introdus în format scurt de 2 cifre (ex: 28 -> 2028)
            if (year < 100) {
                year += 2000;
            }

            YearMonth cardExpiry = YearMonth.of(year, month);
            YearMonth currentMonth = YearMonth.now();

            if (cardExpiry.isBefore(currentMonth)) {
                return "Cardul este expirat! Introduceți un card valabil.";
            }
        } catch (Exception e) {
            return "Data de expirare este invalidă!";
        }

        // Dacă toate validările au trecut cu succes
        return null;
    }
}