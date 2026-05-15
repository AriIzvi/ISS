package concursAtletism.UI;

import concursAtletism.domain.Role; // Importăm Enum-ul pentru verificare
import concursAtletism.domain.User;
import concursAtletism.UI.AdminWindow;
import concursAtletism.UI.ClientWindow;
import concursAtletism.service.BookService;
import concursAtletism.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginWindow extends JFrame {

    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private UserService userService;
    private BookService bookService; // Avem nevoie de el pentru a-l pasa ferestrelor următoare

    // Am adăugat BookService în constructor
    public LoginWindow(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;

        setTitle("Autentificare - Concurs Atletism");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_GREIGE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);

        JLabel lblTitle = new JLabel("Autentificare");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 32));
        lblTitle.setForeground(TXT_CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        mainPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(15);
        styleTextField(txtEmail);
        mainPanel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        mainPanel.add(createStyledLabel("Parolă:"), gbc);
        gbc.gridx = 1;
        JPasswordField txtPass = new JPasswordField(15);
        styleTextField(txtPass);
        mainPanel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnLogin = new RoundedButton("Intră în cont");
        btnLogin.setBackground(MAIN_SAGE_GREEN);
        btnLogin.setForeground(TXT_CHOCOLATE);
        btnLogin.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText();
            String password = new String(txtPass.getPassword());

            User user = userService.handleLogin(email, password);

            if (user != null) {
                // --- LOGICA DIN DIAGRAMĂ (blocul alt [rol]) ---
                if (user.getRole() == Role.CLIENT) {
                    // 12: create ClientWindow(user) -> 13: show()
                    ClientWindow cw = new ClientWindow(bookService, user);
                    cw.setVisible(true);
                } else if (user.getRole() == Role.ADMIN) {
                    // 12.1: create AdminWindow(user) -> 13.1: show()
                    AdminWindow aw = new AdminWindow(bookService, user);
                    aw.setVisible(true);
                }

                // 14: hide() (LoginWindow se închide)
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Date incorecte!", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        mainPanel.add(btnLogin, gbc);
        add(mainPanel);
    }

    // Metodele de styling rămân neschimbate...
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TXT_CHOCOLATE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 18));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TXT_CHOCOLATE, 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }

    class RoundedButton extends JButton {
        private int radius = 15;
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 25, 10, 25));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}