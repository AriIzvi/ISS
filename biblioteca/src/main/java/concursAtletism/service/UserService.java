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

    /**
     * Interceptează fluxul de înregistrare conform diagramei [Create Account]
     * Verifică dacă parolele coincid și trimite datele mai departe spre repo.
     */
    public String handleRegister(String email, String password, String confirmPassword,
                                 String fullName, String dob, String address, String phone) {

        // Pasul 7-8: Verificăm dacă parolele coincid
        if (!password.equals(confirmPassword)) {
            return "PASSWORDS_DO_NOT_MATCH"; // Pasul 8.1
        }

        // Aici poți simula pasul de verificare documente dacă vrei ("valid identity documents provided")
        // În mod normal, într-o aplicație reală, verifici dacă datele sunt valide.
        boolean validDocuments = true;
        if (!validDocuments) {
            return "IDENTITY_DOCUMENTS_MISSING_OR_INVALID"; // Pasul 11
        }

        // Creăm obiectul User (Implicit dăm rolul de CLIENT celor care se înregistrează)
        User newUser = new User(null, email, password, concursAtletism.domain.Role.CLIENT, fullName, dob, address, phone);

        // Pasul 9: create new account în repository
        boolean success = userRepository.save(newUser);

        if (success) {
            // Pasul 9.1: Aici s-ar trimite mailul de verificare (simulat prin mesaj)
            System.out.println("Pasul 9.1: Se trimite email de verificare pentru contul: " + email);
            return "SUCCESS"; // Pasul 9.1.1
        } else {
            return "CREATION_FAILED"; // Pasul 10
        }
    }
}