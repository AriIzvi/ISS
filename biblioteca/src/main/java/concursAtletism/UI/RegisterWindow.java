package concursAtletism.UI;

import concursAtletism.service.UserService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterWindow extends JDialog {

    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private UserService userService;

    public RegisterWindow(JFrame parent, UserService userService) {
        super(parent, "Înregistrare Cont Nou", true);
        this.userService = userService;

        setSize(550, 650);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BG_GREIGE);

        initGUI();
    }

    private void initGUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BG_GREIGE);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titlu Fereastră
        JLabel lblTitle = new JLabel("Creează Cont", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(TXT_CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        mainPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Definim câmpurile de text din diagramă
        JTextField txtName = addFormRow(mainPanel, "Nume Complet:", gbc, 1);
        JTextField txtDob = addFormRow(mainPanel, "Data Nașterii (ZZ/LL/AAAA):", gbc, 2);
        JTextField txtAddress = addFormRow(mainPanel, "Adresă:", gbc, 3);
        JTextField txtPhone = addFormRow(mainPanel, "Număr Telefon:", gbc, 4);
        JTextField txtEmail = addFormRow(mainPanel, "Email / Utilizator:", gbc, 5);

        // Câmpurile de parole
        gbc.gridx = 0; gbc.gridy = 6;
        mainPanel.add(createStyledLabel("Parolă:"), gbc);
        gbc.gridx = 1;
        JPasswordField txtPass = new JPasswordField(15);
        styleTextField(txtPass);
        mainPanel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy = 7;
        mainPanel.add(createStyledLabel("Confirmă Parolă:"), gbc);
        gbc.gridx = 1;
        JPasswordField txtConfirmPass = new JPasswordField(15);
        styleTextField(txtConfirmPass);
        mainPanel.add(txtConfirmPass, gbc);

        // Buton de trimitere formular (Pasul 8)
        gbc.gridx = 0; gbc.gridy = 8; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);

        JButton btnRegister = new RoundedButton("Înregistrează-te");
        btnRegister.setBackground(MAIN_SAGE_GREEN);
        btnRegister.setForeground(TXT_CHOCOLATE);
        btnRegister.setFont(new Font("SansSerif", Font.BOLD, 16));

        btnRegister.addActionListener(e -> {
            // Preluăm toate datele introduse (Pașii 1-7)
            String name = txtName.getText();
            String dob = txtDob.getText();
            String address = txtAddress.getText();
            String phone = txtPhone.getText();
            String email = txtEmail.getText();
            String password = new String(txtPass.getPassword());
            String confirmPassword = new String(txtConfirmPass.getPassword());

            if(name.isEmpty() || dob.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Toate câmpurile sunt obligatorii!", "Atenție", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Trimitem totul prin Service
            String result = userService.handleRegister(email, password, confirmPassword, name, dob, address, phone);

            // Evaluăm rezultatele din blocurile 'alt' ale diagramei
            switch (result) {
                case "SUCCESS":
                    JOptionPane.showMessageDialog(this, "Cont creat cu succes! Un email de verificare a fost trimis.", "Succes", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                    break;
                case "PASSWORDS_DO_NOT_MATCH":
                    JOptionPane.showMessageDialog(this, "Parolele introduse nu coincid!", "Eroare", JOptionPane.ERROR_MESSAGE);
                    break;
                case "IDENTITY_DOCUMENTS_MISSING_OR_INVALID":
                    JOptionPane.showMessageDialog(this, "Documentele de identitate sunt invalide!", "Eroare", JOptionPane.ERROR_MESSAGE);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Eroare: Crearea contului a eșuat în baza de date.", "Eroare", JOptionPane.ERROR_MESSAGE);
                    break;
            }
        });

        mainPanel.add(btnRegister, gbc);
        add(mainPanel);
    }

    private JTextField addFormRow(JPanel panel, String labelText, GridBagConstraints gbc, int row) {
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createStyledLabel(labelText), gbc);
        gbc.gridx = 1;
        JTextField textField = new JTextField(15);
        styleTextField(textField);
        panel.add(textField, gbc);
        return textField;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(TXT_CHOCOLATE);
        label.setFont(new Font("SansSerif", Font.PLAIN, 14));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TXT_CHOCOLATE, 1, true),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
    }

    class RoundedButton extends JButton {
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
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}