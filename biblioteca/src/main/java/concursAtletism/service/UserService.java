package concursAtletism.service;

import concursAtletism.domain.User;
import concursAtletism.repository.UserRepository;

public class UserService {
    private UserRepository userRepository;

    // Constructorul primește repository-ul (Dependency Injection)
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Metoda handleLogin identificată în diagrama de secvență
     *
     */
    public User handleLogin(String email, String password) {
        // Apelăm repository-ul pentru a căuta userul în baza de date
        User user = userRepository.findBy(email, password);

        if (user != null) {
            // Dacă l-am găsit, îl returnăm către LoginWindow
            System.out.println("No, salutare! Te-ai logat cu succes ca: " + user.getRole());
            return user;
        } else {
            // Dacă e null, înseamnă că datele sunt greșite
            System.out.println("Eroare: Email sau parolă incorectă.");
            return null;
        }
    }
}