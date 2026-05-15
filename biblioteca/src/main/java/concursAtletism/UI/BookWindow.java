package concursAtletism.UI;

import concursAtletism.domain.Book;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class BookWindow extends JFrame {

    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    public BookWindow(Book book) {
        setTitle("Detalii Carte: " + book.getTitle());
        setSize(500, 700); // Am mărit puțin înălțimea pentru a lăsa poza să "respire"
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_GREIGE);

        initGUI(book);
    }

    private void initGUI(Book book) {
        setLayout(new BorderLayout());

        JPanel mainContent = new JPanel(new GridBagLayout());
        mainContent.setBackground(BG_GREIGE);
        mainContent.setBorder(new EmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 1. Imagine / Copertă Reală
        JLabel lblImage = new JLabel("", SwingConstants.CENTER);
        lblImage.setOpaque(true);
        lblImage.setBackground(new Color(225, 225, 225));
        lblImage.setPreferredSize(new Dimension(200, 300)); // Dimensiune mai generoasă aici
        lblImage.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180), 1));

        // Logica de încărcare a copertei
        String coverPath = "coperte/" + book.getCover();
        File file = new File(coverPath);

        if (book.getCover() != null && file.exists()) {
            ImageIcon icon = new ImageIcon(coverPath);
            Image img = icon.getImage();
            // Scalare la 200x300 pentru fereastra de detalii
            Image scaledImg = img.getScaledInstance(200, 300, Image.SCALE_SMOOTH);
            lblImage.setIcon(new ImageIcon(scaledImg));
        } else {
            lblImage.setText("<html><center>Imaginea nu este<br>disponibilă</center></html>");
            lblImage.setForeground(Color.GRAY);
        }

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(lblImage, gbc);

        // 2. Titlu
        JLabel lblTitle = new JLabel(book.getTitle(), SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 28));
        lblTitle.setForeground(TXT_CHOCOLATE);
        gbc.gridy = 1;
        mainContent.add(lblTitle, gbc);

        // 3. Detalii
        gbc.gridwidth = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        mainContent.add(createLabel("Autor:"), gbc);
        gbc.gridx = 1;
        mainContent.add(createValueLabel(book.getAuthor()), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        mainContent.add(createLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        mainContent.add(createValueLabel(book.getIsbn()), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        mainContent.add(createLabel("Gen:"), gbc);
        gbc.gridx = 1;
        mainContent.add(createValueLabel(book.getGenre()), gbc);

        // 4. Buton Închidere
        JButton btnClose = new RoundedButton("Înapoi la Magazin");
        btnClose.setBackground(MAIN_SAGE_GREEN);
        btnClose.setForeground(TXT_CHOCOLATE);
        btnClose.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnClose.addActionListener(e -> this.dispose());

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        gbc.insets = new Insets(40, 10, 10, 10);
        gbc.anchor = GridBagConstraints.CENTER;
        mainContent.add(btnClose, gbc);

        add(new JScrollPane(mainContent), BorderLayout.CENTER); // JScrollPane în caz că ecranul e mic
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
}