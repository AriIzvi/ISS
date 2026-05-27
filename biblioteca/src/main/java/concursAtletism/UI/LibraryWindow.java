package concursAtletism.UI;

import concursAtletism.domain.Book;
import concursAtletism.domain.User;
import concursAtletism.service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.util.List;

public class LibraryWindow extends JDialog {
    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color BG_BUTTER_CARD = new Color(255, 253, 245);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private User user;

    public LibraryWindow(JFrame parent, BookService bookService, User user) {
        super(parent, "Librăria Mea Personală", true);
        this.user = user;
        setSize(800, 600);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(BG_GREIGE);
        setLayout(new BorderLayout());

        // Header
        JLabel lblTitle = new JLabel("📚 Raftul Meu de Cărți", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(TXT_CHOCOLATE);
        lblTitle.setBorder(new EmptyBorder(15, 15, 15, 15));
        add(lblTitle, BorderLayout.NORTH);

        // Grid Rezultate
        JPanel gridPanel = new JPanel(new GridLayout(0, 3, 20, 20));
        gridPanel.setBackground(BG_GREIGE);
        gridPanel.setBorder(new EmptyBorder(20, 30, 20, 30));

        List<Book> myBooks = bookService.getMyLibrary(user.getEmail());

        if (myBooks.isEmpty()) {
            setLayout(new GridBagLayout());
            JLabel lblEmpty = new JLabel("Nu ai cumpărat nicio carte încă. Biblioteca ta e goală!");
            lblEmpty.setFont(new Font("SansSerif", Font.ITALIC, 14));
            lblEmpty.setForeground(Color.GRAY);
            add(lblEmpty);
        } else {
            for (Book b : myBooks) {
                gridPanel.add(createSimpleBookCard(b, bookService)); // <-- Trimitem și bookService
            }
            JScrollPane scroll = new JScrollPane(gridPanel);
            scroll.setBorder(null);
            add(scroll, BorderLayout.CENTER);
        }
    }

    private JPanel createSimpleBookCard(Book book, BookService bookService) {
        // Folosim un layout BorderLayout cu spațiere
        JPanel card = new JPanel(new BorderLayout(10, 12)) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_BUTTER_CARD);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panou de text (Titlu sus, Autor imediat sub el)
        JPanel textInfo = new JPanel(new GridLayout(2, 1, 2, 2));
        textInfo.setOpaque(false);

        JLabel lblTitle = new JLabel(book.getTitle(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblTitle.setForeground(TXT_CHOCOLATE);

        JLabel lblAuthor = new JLabel("de " + book.getAuthor(), SwingConstants.CENTER);
        lblAuthor.setFont(new Font("SansSerif", Font.ITALIC, 12));
        lblAuthor.setForeground(Color.DARK_GRAY);

        textInfo.add(lblTitle);
        textInfo.add(lblAuthor);
        card.add(textInfo, BorderLayout.NORTH);

        // Copertă centrală
        JLabel lblImg = new JLabel("", SwingConstants.CENTER);
        lblImg.setOpaque(true);
        lblImg.setBackground(new Color(235, 235, 235));
        lblImg.setPreferredSize(new Dimension(120, 170));
        lblImg.setBorder(BorderFactory.createLineBorder(new Color(210, 210, 210)));

        String coverPath = "coperte/" + book.getCover();
        java.io.File file = new java.io.File(coverPath);
        if (book.getCover() != null && file.exists()) {
            ImageIcon icon = new ImageIcon(coverPath);
            Image img = icon.getImage();
            Image scaledImg = img.getScaledInstance(120, 170, Image.SCALE_SMOOTH);
            lblImg.setIcon(new ImageIcon(scaledImg));
        } else {
            lblImg.setText("<html><center>Fără<br>Copertă</center></html>");
            lblImg.setForeground(Color.GRAY);
        }
        card.add(lblImg, BorderLayout.CENTER);

        // !!! ELEMENTUL NOU: Butonul de vizualizare identic ca funcționalitate !!!
        JButton btnView = new RoundedButton("Vizualizează carte");
        btnView.setBackground(MAIN_SAGE_GREEN);
        btnView.setForeground(TXT_CHOCOLATE);
        btnView.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnView.addActionListener(e -> {
            // Pasăm 'this' ca prim parametru (fereastra LibraryWindow curentă)
            BookWindow bookDetails = new BookWindow(this, book, bookService, null, user);
            bookDetails.setVisible(true);
        });
        card.add(btnView, BorderLayout.SOUTH);

        return card;
    }

    // Adaugă asta jos de tot în fișier, înainte de ultima acoladă '}' a clasei principale
    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));
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