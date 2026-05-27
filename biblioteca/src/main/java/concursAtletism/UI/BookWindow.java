package concursAtletism.UI;

import concursAtletism.domain.Book;
import concursAtletism.domain.Review;
import concursAtletism.domain.User;
import concursAtletism.service.CartService;
import concursAtletism.service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class BookWindow extends JDialog {

    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color BG_BUTTER_CARD = new Color(255, 253, 245);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private BookService bookService;
    private User currentUser;
    private CartService cartService;

    public BookWindow(Window parent, Book book, BookService bookService, CartService cartService, User user) {
        // Apelăm constructorul de bază din JDialog (titlu, modalitate)
        super(parent, "Detalii Carte: " + book.getTitle(), ModalityType.APPLICATION_MODAL);

        this.bookService = bookService;
        this.cartService = cartService;
        this.currentUser = user;

        setSize(520, 850);
        // Schimbăm DISPOSE_ON_CLOSE să fie specific pentru JDialog
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parent); // Se va centra perfect peste fereastra care o deschide
        getContentPane().setBackground(BG_GREIGE);

        initGUI(book);
    }

    private void initGUI(Book book) {
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(BG_GREIGE);
        mainContent.setBorder(new EmptyBorder(25, 25, 25, 25));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        int currentGridY = 0;

        // 1. Copertă
        JLabel lblImage = new JLabel("", SwingConstants.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(225, 225, 225));
        lblImage.setPreferredSize(new Dimension(160, 230));
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        String coverPath = "coperte/" + book.getCover();
        File file = new File(coverPath);
        if (book.getCover() != null && file.exists()) {
            ImageIcon icon = new ImageIcon(coverPath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(160, 230, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImg));
        } else {
            lblImage.setText("<html><center>Imaginea nu este<br>disponibilă</center></html>");
            lblImage.setForeground(Color.GRAY);
        }

        gbc.gridx = 0; gbc.gridy = currentGridY; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(lblImage, gbc);

        // 2. Titlu
        currentGridY++;
        JLabel lblTitle = new JLabel(book.getTitle(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(TXT_CHOCOLATE);
        gbc.gridy = currentGridY;
        mainContent.add(lblTitle, gbc);

        // 3. Detalii tehnice carte
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        currentGridY++;
        gbc.gridx = 0; gbc.gridy = currentGridY;
        mainContent.add(createLabel("Autor:"), gbc);
        gbc.gridx = 1;
        mainContent.add(createValueLabel(book.getAuthor()), gbc);

        currentGridY++;
        gbc.gridx = 0; gbc.gridy = currentGridY;
        mainContent.add(createLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        mainContent.add(createValueLabel(book.getIsbn()), gbc);

        currentGridY++;
        gbc.gridx = 0; gbc.gridy = currentGridY;
        mainContent.add(createLabel("Gen:"), gbc);
        gbc.gridx = 1;
        mainContent.add(createValueLabel(book.getGenre()), gbc);

        // --- ZONA DE RECENZII (MAXIM 2) ---
        currentGridY++;
        gbc.gridx = 0; gbc.gridy = currentGridY; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 5, 10);
        JLabel lblRevTitle = new JLabel("Recenzii Clienți:");
        lblRevTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblRevTitle.setForeground(TXT_CHOCOLATE);
        mainContent.add(lblRevTitle, gbc);

        List<Review> reviews = bookService.getTop2ReviewsForBook(book.getId());

        if (reviews.isEmpty()) {
            currentGridY++;
            gbc.gridy = currentGridY;
            gbc.insets = new Insets(5, 10, 5, 10);
            JLabel lblNoReviews = new JLabel("Încă nu s-au adăugat recenzii pentru această carte.", SwingConstants.CENTER);
            lblNoReviews.setFont(new Font("SansSerif", Font.ITALIC, 13));
            lblNoReviews.setForeground(Color.GRAY);
            mainContent.add(lblNoReviews, gbc);
        } else {
            for (Review r : reviews) {
                currentGridY++;
                gbc.gridy = currentGridY;
                gbc.insets = new Insets(5, 10, 5, 10);
                mainContent.add(createReviewCard(r), gbc);
            }
        }

        // --- ZONA BUTOANELOR ORDONATE ---

        // Butonul 1: Adaugă Recenzie (Doar pentru CLIENT)
        if (currentUser != null && currentUser.getRole() == concursAtletism.domain.Role.CLIENT) {
            currentGridY++;
            gbc.gridx = 0; gbc.gridy = currentGridY; gbc.gridwidth = 2;
            gbc.insets = new Insets(20, 10, 5, 10);
            gbc.anchor = GridBagConstraints.CENTER;

            JButton btnAddReview = new RoundedButton("Adaugă Recenzie");
            btnAddReview.setBackground(MAIN_SAGE_GREEN);
            btnAddReview.setForeground(TXT_CHOCOLATE);
            btnAddReview.setFont(new Font("SansSerif", Font.BOLD, 14));

            btnAddReview.addActionListener(e -> {
                openAddReviewDialog(book);
            });

            mainContent.add(btnAddReview, gbc);
        }

        // Butonul 2: Adaugă în Coș (Doar pentru CLIENT)
        if (currentUser != null && currentUser.getRole() == concursAtletism.domain.Role.CLIENT) {
            currentGridY++;
            gbc.gridx = 0; gbc.gridy = currentGridY; gbc.gridwidth = 2;
            // Spațiere mai mică sus pentru că vine imediat sub celălalt buton
            gbc.insets = new Insets(8, 10, 5, 10);
            gbc.anchor = GridBagConstraints.CENTER;

            JButton btnAddToCart = new RoundedButton("🛒 Adaugă în Coș");
            btnAddToCart.setBackground(MAIN_SAGE_GREEN);
            btnAddToCart.setForeground(TXT_CHOCOLATE);
            btnAddToCart.setFont(new Font("SansSerif", Font.BOLD, 14));

            btnAddToCart.addActionListener(e -> {
                boolean success = cartService.addToCart(currentUser.getEmail(), book.getId());
                if (success) {
                    JOptionPane.showMessageDialog(this, "Cartea a fost adăugată în coș cu succes!", "Succes", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Eroare la adăugarea în coș.", "Eroare", JOptionPane.ERROR_MESSAGE);
                }
            });

            mainContent.add(btnAddToCart, gbc);
        }

        // Butonul 3: Înapoi la Magazin
        currentGridY++;
        gbc.gridx = 0; gbc.gridy = currentGridY; gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;

        JButton btnClose = new RoundedButton("Înapoi la Magazin");
        btnClose.setBackground(new Color(210, 205, 195));
        btnClose.setForeground(TXT_CHOCOLATE);
        btnClose.setFont(new Font("SansSerif", Font.PLAIN, 14));
        btnClose.addActionListener(e -> this.dispose());

        mainContent.add(btnClose, gbc);

        add(new JScrollPane(mainContent), BorderLayout.CENTER);
    }

    private JPanel createReviewCard(Review review) {
        RoundedPanel card = new RoundedPanel(15, BG_BUTTER_CARD);
        card.setLayout(new BorderLayout(5, 5));
        card.setBorder(new EmptyBorder(8, 12, 8, 12));

        String stars = "⭐".repeat(review.getRating());

        JLabel lblUserAndStars = new JLabel("<html><b>" + review.getUserEmail() + "</b> a acordat " + stars + "</html>");
        lblUserAndStars.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblUserAndStars.setForeground(TXT_CHOCOLATE);

        JLabel lblComment = new JLabel("<html><i>\"" + review.getComment() + "\"</i></html>");
        lblComment.setFont(new Font("SansSerif", Font.PLAIN, 13));
        lblComment.setForeground(Color.DARK_GRAY);

        card.add(lblUserAndStars, BorderLayout.NORTH);
        card.add(lblComment, BorderLayout.CENTER);

        return card;
    }

    private void openAddReviewDialog(Book book) {
        JDialog dialog = new JDialog(this, "Scrie o recenzie pentru " + book.getTitle(), true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.getContentPane().setBackground(BG_GREIGE);
        dialog.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        dialog.add(createLabel("Notă (1-5):"), gbc);
        gbc.gridx = 1;
        Integer[] ratings = {5, 4, 3, 2, 1};
        JComboBox<Integer> comboRating = new JComboBox<>(ratings);
        dialog.add(comboRating, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        dialog.add(createLabel("Comentariu:"), gbc);
        gbc.gridx = 1;
        JTextArea txtComment = new JTextArea(4, 20);
        txtComment.setLineWrap(true);
        txtComment.setWrapStyleWord(true);
        JScrollPane scrollComment = new JScrollPane(txtComment);
        dialog.add(scrollComment, gbc);

        JButton btnSubmit = new RoundedButton("Trimite Recenzia");
        btnSubmit.setBackground(MAIN_SAGE_GREEN);
        btnSubmit.setForeground(TXT_CHOCOLATE);

        btnSubmit.addActionListener(ev -> {
            String comment = txtComment.getText().trim();
            int rating = (int) comboRating.getSelectedItem();

            if (comment.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Te rugăm să scrii un scurt comentariu!", "Atenție", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Review review = new Review(null, book.getId(), currentUser.getEmail(), rating, comment);
            boolean success = bookService.handleRegisterReview(review);

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Recenzia ta a fost adăugată cu succes!");
                dialog.dispose();

                this.getContentPane().removeAll();
                this.initGUI(book);
                this.revalidate();
                this.repaint();
            } else {
                JOptionPane.showMessageDialog(dialog, "A apărut o eroare la salvarea recenziei.", "Eroare", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        dialog.add(btnSubmit, gbc);

        dialog.setVisible(true);
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 16));
        l.setForeground(TXT_CHOCOLATE);
        return l;
    }

    private JLabel createValueLabel(String text) {
        JLabel l = new JLabel(text != null ? text : "Nespecificat");
        l.setFont(new Font("SansSerif", Font.PLAIN, 16));
        l.setForeground(Color.DARK_GRAY);
        return l;
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

    class RoundedPanel extends JPanel {
        private int radius;
        private Color bgColor;
        public RoundedPanel(int radius, Color bgColor) {
            this.radius = radius;
            this.bgColor = bgColor;
            setOpaque(false);
        }
        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bgColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
        }
    }
}