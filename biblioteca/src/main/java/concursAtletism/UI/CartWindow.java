package concursAtletism.UI;

import concursAtletism.domain.CartItem;
import concursAtletism.domain.User;
import concursAtletism.service.BookService;
import concursAtletism.service.CartService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CartWindow extends JDialog {
    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color BG_BUTTER_CARD = new Color(255, 253, 245);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private CartService cartService;
    private BookService bookService;
    private User currentUser;
    private JPanel listPanel;

    public CartWindow(JFrame parent, CartService cartService, BookService bookService, User user) {
        super(parent, "Coșul meu de cumpărături", true);
        this.cartService = cartService;
        this.bookService = bookService;
        this.currentUser = user;

        setSize(500, 600);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BG_GREIGE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("Coșul tău de cumpărături", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(TXT_CHOCOLATE);
        lblTitle.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(lblTitle, BorderLayout.NORTH);

        // Panou listă produse
        listPanel = new JPanel();
        listPanel.setBackground(BG_GREIGE);
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        // Footer cu Buton Închidere / Finalizare
        // În constructorul clasei CartWindow.java, înlocuiește panoul footer existent cu ăsta:
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        footer.setBackground(BG_GREIGE);
        footer.setBorder(new EmptyBorder(15, 15, 15, 15));

        JButton btnClose = new RoundedButton("Înapoi la cumpărături");
        btnClose.setBackground(new Color(210, 205, 195));
        btnClose.setForeground(TXT_CHOCOLATE);
        btnClose.addActionListener(e -> dispose());
        footer.add(btnClose);

// BUTON NOU: Finalizează Comanda
        JButton btnCheckout = new RoundedButton("💳 Finalizează Comanda");
        btnCheckout.setBackground(MAIN_SAGE_GREEN);
        btnCheckout.setForeground(TXT_CHOCOLATE);
        btnCheckout.setFont(new Font("SansSerif", Font.BOLD, 13));

        btnCheckout.addActionListener(e -> {
            List<CartItem> items = cartService.getCartForUser(currentUser.getEmail());
            if (items.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Coșul tău este gol!", "Atenție", JOptionPane.WARNING_MESSAGE);
                return;
            }

            double totalAmount = 0;
            java.util.ArrayList<Long> bookIds = new java.util.ArrayList<>();
            for (CartItem item : items) {
                totalAmount += (item.getQuantity() * 45.0);
                bookIds.add(item.getBook().getId()); // Colectăm ID-urile cărților cumpărate
            }

            // Pasăm și bookService, email-ul și lista de ID-uri către fereastra de plată
            PaymentWindow paymentWindow = new PaymentWindow(this, cartService, bookService, currentUser.getEmail(), bookIds, totalAmount);
            paymentWindow.setVisible(true);

            if (paymentWindow.isPaymentSuccessful()) {
                this.dispose();
            }
        });

        footer.add(btnCheckout);
        add(footer, BorderLayout.SOUTH);

        refreshCart();
    }

    private void refreshCart() {
        listPanel.removeAll();
        List<CartItem> items = cartService.getCartForUser(currentUser.getEmail());

        if (items.isEmpty()) {
            JLabel lblEmpty = new JLabel("Coșul tău este gol.", SwingConstants.CENTER);
            lblEmpty.setFont(new Font("SansSerif", Font.ITALIC, 14));
            lblEmpty.setForeground(Color.GRAY);
            lblEmpty.setAlignmentX(Component.CENTER_ALIGNMENT);
            listPanel.add(Box.createVerticalStrut(50));
            listPanel.add(lblEmpty);
        } else {
            for (CartItem item : items) {
                listPanel.add(createCartItemRow(item));
                listPanel.add(Box.createVerticalStrut(10));
            }
        }
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createCartItemRow(CartItem item) {
        JPanel row = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_BUTTER_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.dispose();
            }
        };
        row.setOpaque(false);
        row.setBorder(new EmptyBorder(10, 15, 10, 15));
        row.setMaximumSize(new Dimension(460, 70));

        JLabel lblInfo = new JLabel("<html><b>" + item.getBook().getTitle() + "</b><br>Cantitate: " + item.getQuantity() + "</html>");
        lblInfo.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblInfo.setForeground(TXT_CHOCOLATE);
        row.add(lblInfo, BorderLayout.CENTER);

        // Opțiunea de ștergere din diagramă (Delete item)
        JButton btnDelete = new JButton("❌");
        btnDelete.setContentAreaFilled(false);
        btnDelete.setBorderPainted(false);
        btnDelete.setFocusPainted(false);
        btnDelete.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDelete.addActionListener(e -> {
            if (cartService.removeFromCart(item.getId())) {
                refreshCart();
            }
        });
        row.add(btnDelete, BorderLayout.EAST);

        return row;
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