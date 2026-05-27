package concursAtletism.UI;

import concursAtletism.service.BookService;
import concursAtletism.service.CartService;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class PaymentWindow extends JDialog {
    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private CartService cartService;
    private boolean paymentSuccessful = false;

    private BookService bookService;

    private String userEmail;
    private java.util.ArrayList<Long> itemsToBuy;

    public PaymentWindow(JDialog parent, CartService cartService, BookService bookService,String userEmail, java.util.ArrayList<Long> itemsToBuy, double totalAmount) {
        super(parent, "Finalizare Plată", true);
        this.cartService = cartService;
        this.bookService = bookService;
        this.userEmail = userEmail;
        this.itemsToBuy = itemsToBuy;

        setSize(450, 400);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BG_GREIGE);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 15, 12, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titlu / Sumă de plată
        JLabel lblSummary = new JLabel("<html><center>Total de plată: <b>" + String.format("%.2f", totalAmount) + " RON</b></center></html>", SwingConstants.CENTER);
        lblSummary.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblSummary.setForeground(TXT_CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(lblSummary, gbc);

        gbc.gridwidth = 1;

        // Câmp: Număr Card
        gbc.gridx = 0; gbc.gridy = 1;
        add(createLabel("Număr Card:"), gbc);
        gbc.gridx = 1;
        JTextField txtCardNumber = new JTextField(16);
        styleTextField(txtCardNumber);
        add(txtCardNumber, gbc);

        // Câmp: Data Expirare
        gbc.gridx = 0; gbc.gridy = 2;
        add(createLabel("Expiră (MM/YY):"), gbc);
        gbc.gridx = 1;
        JTextField txtExpiry = new JTextField(5);
        styleTextField(txtExpiry);
        add(txtExpiry, gbc);

        // Câmp: CVV
        gbc.gridx = 0; gbc.gridy = 3;
        add(createLabel("CVV:"), gbc);
        gbc.gridx = 1;
        JPasswordField txtCvv = new JPasswordField(3);
        styleTextField(txtCvv);
        add(txtCvv, gbc);

        // Buton Execută Plata
        JButton btnPay = new RoundedButton("Confirmă Plata");
        btnPay.setBackground(MAIN_SAGE_GREEN);
        btnPay.setForeground(TXT_CHOCOLATE);
        btnPay.setFont(new Font("SansSerif", Font.BOLD, 14));

        btnPay.addActionListener(e -> {
            String cardNumber = txtCardNumber.getText().trim();
            String expiry = txtExpiry.getText().trim();
            String cvv = new String(txtCvv.getPassword()).trim();

            // Apelăm validările din CartService
            String errorMsg = cartService.handlePayment(cardNumber, expiry, cvv);

            if (errorMsg != null) {
                JOptionPane.showMessageDialog(this, errorMsg, "Eroare Validare", JOptionPane.ERROR_MESSAGE);
            } else {
                // !!! LOGICA NOUĂ: Salvăm cărțile în Librăria Personală !!!
                bookService.registerPurchase(userEmail, itemsToBuy);

                JOptionPane.showMessageDialog(this, "Plată procesată cu succes! Cărțile au fost adăugate în Librăria ta.", "Succes", JOptionPane.INFORMATION_MESSAGE);
                paymentSuccessful = true;
                dispose();
            }
        });

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        gbc.insets = new Insets(25, 15, 5, 15);
        add(btnPay, gbc);

        // Buton Anulare
        JButton btnCancel = new RoundedButton("Anulează");
        btnCancel.setBackground(new Color(210, 205, 195));
        btnCancel.setForeground(TXT_CHOCOLATE);
        btnCancel.addActionListener(e -> dispose());

        gbc.gridy = 5;
        gbc.insets = new Insets(5, 15, 15, 15);
        add(btnCancel, gbc);
    }

    public boolean isPaymentSuccessful() {
        return paymentSuccessful;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(TXT_CHOCOLATE);
        return l;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TXT_CHOCOLATE, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
    }

    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(10, 20, 10, 20));
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
            super.paintComponent(g);
            g2.dispose();
        }
    }
}