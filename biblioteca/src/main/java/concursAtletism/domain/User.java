package concursAtletism.domain;

public class User {
    private Long id;
    private String email;
    private String password;
    private Role role;

    // Câmpuri noi adăugate din diagrama Create Account
    private String fullName;
    private String dateOfBirth;
    private String address;
    private String phoneNumber;

    public User() {}

    // Constructor complet pentru înregistrare/încărcare
    public User(Long id, String email, String password, Role role, String fullName, String dateOfBirth, String address, String phoneNumber) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters și Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public String toString() {
        return "User{" + "email='" + email + '\'' + ", role=" + role + ", fullName='" + fullName + '\'' + '}';
    }
}