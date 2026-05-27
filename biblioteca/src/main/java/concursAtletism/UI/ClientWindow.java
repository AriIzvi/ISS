package concursAtletism.UI;

import concursAtletism.domain.Book;
import concursAtletism.domain.User;
import concursAtletism.service.BookService;
import concursAtletism.service.CartService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;
import java.util.Collections;

public class ClientWindow extends JFrame {

    // Paleta de culori
    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color BG_BUTTER_CARD = new Color(255, 253, 245);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private BookService bookService;
    private CartService cartService;
    private User currentUser;
    private JPanel resultsPanel;

    public ClientWindow(BookService bookService,CartService cartService, User user) {
        this.bookService = bookService;
        this.cartService = cartService;
        this.currentUser = user;

        setTitle("Magazin Cărți - " + user.getEmail());
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_GREIGE);

        initGUI();
        refreshResults(bookService.getAllBooks());
    }

    private void initGUI() {
        setLayout(new BorderLayout());

        // 1. Header
        // 1. Header principal
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(MAIN_SAGE_GREEN);
        header.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblWelcome = new JLabel("Salutare, " + currentUser.getEmail());
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblWelcome.setForeground(TXT_CHOCOLATE);
        header.add(lblWelcome, BorderLayout.WEST);

        JLabel lblLogo = new JLabel("MAGAZIN DE CĂRȚI", SwingConstants.CENTER);
        lblLogo.setForeground(TXT_CHOCOLATE);
        lblLogo.setFont(new Font("SansSerif", Font.BOLD, 20));
        header.add(lblLogo, BorderLayout.CENTER);

        // !!! REPARARE: Creăm un panou suport pentru butoanele din dreapta !!!
        JPanel rightControlsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightControlsPanel.setOpaque(false); // Îi dăm fundal transparent ca să păstreze culoarea header-ului

        // Butonul A: Librăria Mea
        JButton btnMyLibrary = new RoundedButton("📚 Librăria Mea");
        btnMyLibrary.setBackground(MAIN_SAGE_GREEN);
        btnMyLibrary.setForeground(TXT_CHOCOLATE);
        btnMyLibrary.addActionListener(e -> {
            LibraryWindow libWindow = new LibraryWindow(this, bookService, currentUser);
            libWindow.setVisible(true);
        });
        rightControlsPanel.add(btnMyLibrary); // Îl punem în panoul mic

        // Butonul B: Coșul meu
        JButton btnViewCart = new RoundedButton("🛒 Coșul meu");
        btnViewCart.setBackground(MAIN_SAGE_GREEN);
        btnViewCart.setForeground(TXT_CHOCOLATE);
        btnViewCart.addActionListener(e -> {
            CartWindow cartWindow = new CartWindow(this, cartService, bookService, currentUser);
            cartWindow.setVisible(true);
        });
        rightControlsPanel.add(btnViewCart); // Îl punem tot în panoul mic

        // Adăugăm panoul care conține AMBELE butoane în partea dreaptă a header-ului
        header.add(rightControlsPanel, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // 2. Zona de Căutare
        JPanel centerContainer = new JPanel(new BorderLayout());
        centerContainer.setOpaque(false);

        JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 25));
        searchBarPanel.setOpaque(false);

        JTextField txtSearch = new JTextField(25);
        styleTextField(txtSearch);

        JButton btnSearch = new RoundedButton("Caută (Titlu, Autor)");
        btnSearch.setBackground(MAIN_SAGE_GREEN);
        btnSearch.setForeground(TXT_CHOCOLATE);

        searchBarPanel.add(txtSearch);
        searchBarPanel.add(btnSearch);
        centerContainer.add(searchBarPanel, BorderLayout.NORTH);

        // 3. Zona de Rezultate
        resultsPanel = new JPanel(new GridLayout(0, 3, 30, 30));
        resultsPanel.setBackground(BG_GREIGE);
        resultsPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

        JScrollPane scrollPane = new JScrollPane(resultsPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(BG_GREIGE);
        centerContainer.add(scrollPane, BorderLayout.CENTER);

        add(centerContainer, BorderLayout.CENTER);

        // Eveniment Căutare (sd [Search])
        btnSearch.addActionListener(e -> {
            String attr = txtSearch.getText();
            if (attr.isEmpty()) {
                refreshResults(bookService.getAllBooks()); // Dacă e gol, arătăm tot
            } else {
                Book found = bookService.handleSearch(attr);
                if (found != null) {
                    refreshResults(Collections.singletonList(found));
                } else {
                    resultsPanel.removeAll();
                    resultsPanel.revalidate();
                    resultsPanel.repaint();
                    JOptionPane.showMessageDialog(this, "Nu am găsit nimic.");
                }
            }
        });
    }

    private void refreshResults(List<Book> books) {
        resultsPanel.removeAll();
        for (Book b : books) {
            resultsPanel.add(createBookCard(b));
        }
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }


    private JPanel createBookCard(Book book) {
        // Folosim RoundedPanel-ul tău pentru aspectul modern de card
        RoundedPanel card = new RoundedPanel(25, BG_BUTTER_CARD);
        card.setLayout(new BorderLayout(10, 15));
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 1. Zona de Text (Titlu și Autor)
        JPanel textInfo = new JPanel(new GridLayout(2, 1));
        textInfo.setOpaque(false);

        JLabel lblTitle = new JLabel(book.getTitle(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitle.setForeground(TXT_CHOCOLATE);

        JLabel lblAuthor = new JLabel(book.getAuthor(), SwingConstants.CENTER);
        lblAuthor.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblAuthor.setForeground(TXT_CHOCOLATE);

        textInfo.add(lblTitle);
        textInfo.add(lblAuthor);

        // 2. Zona de Imagine (Coperta)
        JLabel lblImg = new JLabel("", SwingConstants.CENTER);
        lblImg.setOpaque(true);
        lblImg.setBackground(new Color(235, 235, 235)); // Un gri foarte deschis
        lblImg.setPreferredSize(new Dimension(140, 200)); // Dimensiune optimă pentru copertă
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        // Încărcăm poza din folderul "coperte"
        String coverPath = "coperte/" + book.getCover(); // Presupunem că ai adăugat getCover() în clasa Book

        java.io.File file = new java.io.File(coverPath);
        if (book.getCover() != null && file.exists()) {
            ImageIcon icon = new ImageIcon(coverPath);
            // Redimensionăm imaginea să fie clară și să se potrivească în card
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(140, 200, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaledImg));
        } else {
            // Dacă nu găsim poza, punem un text de rezervă
            lblImg.setText("<html><center>Fără<br>Copertă</center></html>");
            lblImg.setForeground(Color.GRAY);
        }

        // 3. Butonul de Detalii
        JButton btnDetails = new RoundedButton("Vezi Detalii");
        btnDetails.setBackground(MAIN_SAGE_GREEN);
        btnDetails.setForeground(TXT_CHOCOLATE);
        btnDetails.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Logica din diagrama sd [Show]
        // În ClientWindow.java, în interiorul metodei createBookCard:
        btnDetails.addActionListener(e -> {
            // Pasăm 'this' ca prim parametru (fereastra ClientWindow curentă)
            BookWindow bookDetails = new BookWindow(this, book, bookService, cartService, currentUser);
            bookDetails.setVisible(true);
        });

        // Asamblarea cardului
        card.add(textInfo, BorderLayout.NORTH);
        card.add(lblImg, BorderLayout.CENTER);
        card.add(btnDetails, BorderLayout.SOUTH);

        return card;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("SansSerif", Font.PLAIN, 16));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TXT_CHOCOLATE, 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
    }

    // Componente UI Custom
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