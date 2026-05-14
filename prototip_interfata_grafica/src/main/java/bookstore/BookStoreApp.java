package bookstore;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class BookStoreApp extends JFrame {

    // Paleta de culori
    private static final Color BG_GREIGE = new Color(242, 238, 233); // Gri deschis cald (Light Greige)
    private static final Color BG_BUTTER_CARD = new Color(255, 253, 245); // Crem luminos pentru carduri
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);    // Maro ciocolatiu stins (Text)
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161); // Verde prăfuit (Sage Green / Bannere)
    private static final Color HOVER_SAGE = new Color(145, 166, 144); // Accent verde prăfuit

    private CardLayout cardLayout;
    private JPanel mainCardPanel;
    private JPanel navBarPanel;

    public BookStoreApp() {
        setTitle("Kindle Store Prototype");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_GREIGE);

        // Configurare Navigation Bar (ascuns la login)
        navBarPanel = createNavBar();
        navBarPanel.setVisible(false);
        add(navBarPanel, BorderLayout.NORTH);

        // Configurare Main Card Panel
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);
        mainCardPanel.setBackground(BG_GREIGE);

        // Adăugare ecrane
        mainCardPanel.add(createLoginPanel(), "Login");
        mainCardPanel.add(createStorePanel(), "Store");
        mainCardPanel.add(createBookDetailsPanel(), "Details");
        mainCardPanel.add(createCartPanel(), "Cart");
        mainCardPanel.add(createCheckoutPanel(), "Checkout");
        mainCardPanel.add(createLibraryPanel(), "Library");

        add(mainCardPanel, BorderLayout.CENTER);
    }

    private JPanel createNavBar() {
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        nav.setBackground(MAIN_SAGE_GREEN);

        JButton btnStore = createNavButton("Magazin Cărți");
        JButton btnCart = createNavButton("Coș de Cumpărături");
        JButton btnLibrary = createNavButton("Biblioteca Mea / Cont");
        JButton btnLogout = createNavButton("Logout");

        btnStore.addActionListener(e -> cardLayout.show(mainCardPanel, "Store"));
        btnCart.addActionListener(e -> cardLayout.show(mainCardPanel, "Cart"));
        btnLibrary.addActionListener(e -> cardLayout.show(mainCardPanel, "Library"));
        btnLogout.addActionListener(e -> {
            navBarPanel.setVisible(false);
            cardLayout.show(mainCardPanel, "Login");
        });

        nav.add(btnStore);
        nav.add(btnCart);
        nav.add(btnLibrary);
        nav.add(btnLogout);

        return nav;
    }

    private JButton createNavButton(String text) {
        JButton btn = new RoundedButton(text);
        btn.setForeground(TXT_CHOCOLATE);
        btn.setBackground(MAIN_SAGE_GREEN);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        return btn;
    }

    private JButton createAccentButton(String text) {
        JButton btn = new RoundedButton(text);
        btn.setBackground(MAIN_SAGE_GREEN);
        btn.setForeground(TXT_CHOCOLATE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 16));
        return btn;
    }

    private JLabel createStyledLabel(String text, int size, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setForeground(TXT_CHOCOLATE);
        label.setFont(new Font("SansSerif", isBold ? Font.BOLD : Font.PLAIN, size));
        return label;
    }

    // --- ECRAN LOGIN ---
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_GREIGE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;

        JLabel title = createStyledLabel("Autentificare", 32, true);
        panel.add(title, gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        panel.add(createStyledLabel("Email:", 18, false), gbc);
        gbc.gridx = 1;
        JTextField txtEmail = new JTextField(15);
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createStyledLabel("Parolă:", 18, false), gbc);
        gbc.gridx = 1;
        JPasswordField txtPass = new JPasswordField(15);
        panel.add(txtPass, gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        JButton btnLogin = createAccentButton("Intră în cont");
        btnLogin.addActionListener(e -> {
            navBarPanel.setVisible(true);
            cardLayout.show(mainCardPanel, "Store");
        });
        panel.add(btnLogin, gbc);

        gbc.gridy++;
        JButton btnReg = new JButton("Nu ai cont? Înregistrează-te");
        btnReg.setForeground(TXT_CHOCOLATE);
        btnReg.setBackground(BG_GREIGE);
        btnReg.setBorderPainted(false);
        btnReg.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReg.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnReg.setForeground(HOVER_SAGE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnReg.setForeground(TXT_CHOCOLATE);
            }
        });
        panel.add(btnReg, gbc);

        return panel;
    }

    // --- ECRAN MAGAZIN (STORE) ---
    private JPanel createStorePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_GREIGE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30)); // Spacer mai mare

        // Bara de căutare
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(BG_GREIGE);
        JTextField txtSearch = new JTextField(30);
        txtSearch.setFont(new Font("SansSerif", Font.PLAIN, 16));
        txtSearch.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TXT_CHOCOLATE, 1, true),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        JButton btnSearch = createAccentButton("Caută (Titlu, Autor)");
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        panel.add(searchPanel, BorderLayout.NORTH);

        // Grid cu cărți - aerisit
        JPanel gridPanel = new JPanel(new GridLayout(2, 3, 40, 40)); // Gap-uri mai mari
        gridPanel.setBackground(BG_GREIGE);
        gridPanel.setBorder(new EmptyBorder(20, 10, 20, 10));

        for (int i = 1; i <= 6; i++) {
            RoundedPanel bookPan = new RoundedPanel(20, BG_BUTTER_CARD);
            bookPan.setLayout(new BorderLayout(10, 10));
            bookPan.setBorder(new EmptyBorder(15, 15, 15, 15));
            
            JLabel lblBook = createStyledLabel("Carte Exemplu " + i, 18, true);
            lblBook.setHorizontalAlignment(SwingConstants.CENTER);
            JLabel lblAuthor = createStyledLabel("Autor " + i, 14, false);
            lblAuthor.setHorizontalAlignment(SwingConstants.CENTER);
            
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.setOpaque(false);
            infoPanel.add(lblBook);
            infoPanel.add(lblAuthor);

            JButton btnDetails = createAccentButton("Vezi Detalii");
            btnDetails.addActionListener(e -> cardLayout.show(mainCardPanel, "Details"));

            // Placeholder imagine carte
            JLabel lblImage = new JLabel("Imagine", SwingConstants.CENTER);
            lblImage.setOpaque(true);
            lblImage.setBackground(new Color(235, 235, 235));
            lblImage.setPreferredSize(new Dimension(100, 150));
            lblImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));

            bookPan.add(lblImage, BorderLayout.CENTER);
            bookPan.add(infoPanel, BorderLayout.NORTH);
            bookPan.add(btnDetails, BorderLayout.SOUTH);
            
            gridPanel.add(bookPan);
        }

        panel.add(new JScrollPane(gridPanel), BorderLayout.CENTER);
        return panel;
    }

    // --- ECRAN DETALII CARTE ---
    private JPanel createBookDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(BG_GREIGE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        // Info carte
        JPanel infoPanel = new JPanel(new GridLayout(5, 1));
        infoPanel.setBackground(BG_GREIGE);
        infoPanel.add(createStyledLabel("Carte Exemplu 1", 28, true));
        infoPanel.add(createStyledLabel("Autor: Autor 1", 20, false));
        infoPanel.add(createStyledLabel("Preț: 45.00 LEI", 22, true));
        
        JButton btnAddToCart = createAccentButton("Adaugă în Coș");
        btnAddToCart.addActionListener(e -> JOptionPane.showMessageDialog(this, "Adăugat în coș!", "Succes", JOptionPane.INFORMATION_MESSAGE));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnPanel.setBackground(BG_GREIGE);
        btnPanel.add(btnAddToCart);
        infoPanel.add(btnPanel);

        // Recenzii
        JPanel reviewsPanel = new JPanel(new BorderLayout());
        reviewsPanel.setBackground(BG_GREIGE);

        JPanel reviewsHeaderPanel = new JPanel(new BorderLayout());
        reviewsHeaderPanel.setBackground(BG_GREIGE);
        reviewsHeaderPanel.add(createStyledLabel("Recenzii utilizatori:", 18, true), BorderLayout.WEST);

        JTextArea txtReviews = new JTextArea("User123: O carte absolut extraordinară!\nCititorFidel: Mi-a plăcut foarte mult intriga.");
        txtReviews.setEditable(false);
        txtReviews.setBackground(BG_BUTTER_CARD);
        txtReviews.setBorder(BorderFactory.createLineBorder(TXT_CHOCOLATE));

        JButton btnAddReview = createAccentButton("Adaugă o recenzie");
        btnAddReview.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnAddReview.addActionListener(e -> {
            String newReview = JOptionPane.showInputDialog(panel, "Scrie recenzia ta aici:", "Adaugă recenzie", JOptionPane.PLAIN_MESSAGE);
            if (newReview != null && !newReview.trim().isEmpty()) {
                txtReviews.append("\nTu: " + newReview);
            }
        });
        reviewsHeaderPanel.add(btnAddReview, BorderLayout.EAST);

        reviewsPanel.add(reviewsHeaderPanel, BorderLayout.NORTH);
        reviewsPanel.add(new JScrollPane(txtReviews), BorderLayout.CENTER);

        panel.add(infoPanel, BorderLayout.NORTH);
        panel.add(reviewsPanel, BorderLayout.CENTER);

        // Buton back
        JButton btnBack = new JButton("<- Înapoi la Magazin");
        btnBack.setBackground(MAIN_SAGE_GREEN);
        btnBack.setForeground(TXT_CHOCOLATE);
        btnBack.addActionListener(e -> cardLayout.show(mainCardPanel, "Store"));
        panel.add(btnBack, BorderLayout.SOUTH);

        return panel;
    }

    // --- ECRAN COȘ DE CUMPĂRĂTURI ---
    private JPanel createCartPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BG_GREIGE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        panel.add(createStyledLabel("Coșul tău", 28, true), BorderLayout.NORTH);

        // Lista de iteme
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_GREIGE);

        for (int i = 1; i <= 2; i++) {
            RoundedPanel item = new RoundedPanel(15, BG_BUTTER_CARD);
            item.setLayout(new BorderLayout(15, 15));
            item.setBorder(new EmptyBorder(15, 15, 15, 15));
            item.setMaximumSize(new Dimension(800, 140));

            // Imagine carte (Placeholder)
            JLabel lblImage = new JLabel("Img Carte", SwingConstants.CENTER);
            lblImage.setOpaque(true);
            lblImage.setBackground(new Color(235, 235, 235));
            lblImage.setPreferredSize(new Dimension(80, 110));
            lblImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            item.add(lblImage, BorderLayout.WEST);

            // Detalii carte (Centru)
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setOpaque(false);
            
            JLabel lblTitle = createStyledLabel("Carte " + i + " - Titlu Captivant", 20, true);
            JLabel lblAuthor = createStyledLabel("Autor " + i, 16, false);
            JLabel lblDesc = createStyledLabel("Aceasta este o scurtă descriere a cărții, pentru a umple spațiul din coș.", 14, false);
            lblDesc.setForeground(Color.DARK_GRAY);

            detailsPanel.add(lblTitle);
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            detailsPanel.add(lblAuthor);
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            detailsPanel.add(lblDesc);

            item.add(detailsPanel, BorderLayout.CENTER);

            // Preț și Buton ștergere (Dreapta)
            JPanel pricePanel = new JPanel(new BorderLayout(10, 10));
            pricePanel.setOpaque(false);
            
            JLabel lblPrice = createStyledLabel("45.00 LEI", 20, true);
            lblPrice.setHorizontalAlignment(SwingConstants.RIGHT);
            
            JButton btnRemove = new RoundedButton("Șterge");
            btnRemove.setBackground(new Color(220, 50, 50));
            btnRemove.setForeground(Color.WHITE);
            
            pricePanel.add(lblPrice, BorderLayout.NORTH);
            pricePanel.add(btnRemove, BorderLayout.SOUTH);

            item.add(pricePanel, BorderLayout.EAST);

            listPanel.add(item);
            listPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        panel.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        // Checkout button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setBackground(BG_GREIGE);
        bottomPanel.add(createStyledLabel("Total: 90.00 LEI", 24, true));
        JButton btnCheckout = createAccentButton("Către Checkout");
        btnCheckout.addActionListener(e -> cardLayout.show(mainCardPanel, "Checkout"));
        bottomPanel.add(btnCheckout);

        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // --- ECRAN CHECKOUT (CARD) ---
    private JPanel createCheckoutPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(BG_GREIGE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(createStyledLabel("Finalizare Comandă - Plata cu Cardul", 26, true), gbc);

        gbc.gridwidth = 1; gbc.gridy++;
        panel.add(createStyledLabel("Nume pe Card:", 16, false), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(20), gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createStyledLabel("Număr Card:", 16, false), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(20), gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createStyledLabel("Data Expirării (LL/AA):", 16, false), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(10), gbc);

        gbc.gridx = 0; gbc.gridy++;
        panel.add(createStyledLabel("CVV:", 16, false), gbc);
        gbc.gridx = 1;
        panel.add(new JTextField(5), gbc);

        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnPay = createAccentButton("Plătește 90.00 LEI");
        btnPay.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Plată realizată cu succes! Cărțile sunt acum în biblioteca ta.", "Succes", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(mainCardPanel, "Library");
        });
        panel.add(btnPay, gbc);

        return panel;
    }

    // --- ECRAN BIBLIOTECA MEA / CONT ---
    private JPanel createLibraryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_GREIGE);
        panel.setBorder(new EmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        headerPanel.setBackground(BG_GREIGE);
        headerPanel.add(createStyledLabel("Contul Meu: utilizator@email.com", 22, true));
        headerPanel.add(createStyledLabel("Biblioteca Mea (Descărcări disponibile):", 26, true));
        panel.add(headerPanel, BorderLayout.NORTH);

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(BG_GREIGE);
        listPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        for (int i = 1; i <= 3; i++) {
            RoundedPanel item = new RoundedPanel(15, BG_BUTTER_CARD);
            item.setLayout(new BorderLayout(15, 15));
            item.setBorder(new EmptyBorder(15, 15, 15, 15));
            item.setMaximumSize(new Dimension(800, 140));

            // Imagine carte (Placeholder)
            JLabel lblImage = new JLabel("Img Carte", SwingConstants.CENTER);
            lblImage.setOpaque(true);
            lblImage.setBackground(new Color(235, 235, 235));
            lblImage.setPreferredSize(new Dimension(80, 110));
            lblImage.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
            item.add(lblImage, BorderLayout.WEST);

            // Detalii carte (Centru)
            JPanel detailsPanel = new JPanel();
            detailsPanel.setLayout(new BoxLayout(detailsPanel, BoxLayout.Y_AXIS));
            detailsPanel.setOpaque(false);
            
            JLabel lblTitle = createStyledLabel("Carte Cumpărată " + i, 20, true);
            JLabel lblAuthor = createStyledLabel("Autor " + i, 16, false);
            
            detailsPanel.add(lblTitle);
            detailsPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            detailsPanel.add(lblAuthor);

            item.add(detailsPanel, BorderLayout.CENTER);

            // Buton Descărcare (Dreapta)
            JPanel actionPanel = new JPanel(new BorderLayout());
            actionPanel.setOpaque(false);

            JButton btnDownload = createAccentButton("Descarcă EPUB/PDF");
            btnDownload.setFont(new Font("SansSerif", Font.PLAIN, 14));
            btnDownload.addActionListener(e -> JOptionPane.showMessageDialog(this, "Descărcare începută..."));
            
            actionPanel.add(btnDownload, BorderLayout.CENTER);

            item.add(actionPanel, BorderLayout.EAST);

            listPanel.add(item);
            listPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        }

        panel.add(new JScrollPane(listPanel), BorderLayout.CENTER);

        return panel;
    }

    public static void main(String[] args) {
        // Trecere la un LookAndFeel mai plăcut dacă e disponibil
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            new BookStoreApp().setVisible(true);
        });
    }

    // --- CUSTOM UI COMPONENTS ---
    
    class RoundedPanel extends JPanel {
        private int radius;
        private Color backgroundColor;

        public RoundedPanel(int radius, Color bgColor) {
            super();
            this.radius = radius;
            this.backgroundColor = bgColor;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Drop shadow subtil
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(3, 3, getWidth() - 4, getHeight() - 4, radius, radius);
            
            // Fundal
            g2.setColor(backgroundColor);
            g2.fillRoundRect(0, 0, getWidth() - 5, getHeight() - 5, radius, radius);
            
            g2.dispose();
        }
    }

    class RoundedButton extends JButton {
        private int radius = 12;

        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new EmptyBorder(8, 15, 8, 15));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            if (getModel().isArmed()) {
                g2.setColor(getBackground().darker());
            } else if (getModel().isRollover()) {
                if (getBackground().equals(MAIN_SAGE_GREEN)) {
                    g2.setColor(HOVER_SAGE); 
                } else {
                    g2.setColor(getBackground().brighter());
                }
            } else {
                g2.setColor(getBackground());
            }
            
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            super.paintComponent(g);
            g2.dispose();
        }
        
        @Override
        protected void paintBorder(Graphics g) {
            // Fără border clasic
        }
    }
}
