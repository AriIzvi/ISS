package concursAtletism.UI;

import concursAtletism.domain.Book;
import concursAtletism.domain.User;
import concursAtletism.service.BookService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class AdminWindow extends JFrame {

    // Culorile tale semnătură
    private static final Color BG_GREIGE = new Color(242, 238, 233);
    private static final Color TXT_CHOCOLATE = new Color(82, 61, 56);
    private static final Color MAIN_SAGE_GREEN = new Color(162, 181, 161);

    private BookService bookService;
    private User adminUser;

    public AdminWindow(BookService bookService, User user) {
        this.bookService = bookService;
        this.adminUser = user;

        setTitle("Panou Administrare - " + user.getEmail());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_GREIGE);

        initGUI();
    }

    private void initGUI() {
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(MAIN_SAGE_GREEN);
        header.setBorder(new EmptyBorder(10, 20, 10, 20));

        JLabel lblAdmin = new JLabel("Admin: " + adminUser.getEmail());
        lblAdmin.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblAdmin.setForeground(TXT_CHOCOLATE);
        header.add(lblAdmin);
        add(header, BorderLayout.NORTH);

        // Zona Centrală
        JPanel content = new JPanel(new GridBagLayout());
        content.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);

        JLabel lblTitle = new JLabel("Gestionare Bibliotecă");
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblTitle.setForeground(TXT_CHOCOLATE);
        gbc.gridx = 0; gbc.gridy = 0;
        content.add(lblTitle, gbc);

        // Butonul de Import (Cel din diagrama sd [Import])
        JButton btnImport = new RoundedButton("Importă Listă Cărți");
        btnImport.setBackground(MAIN_SAGE_GREEN);
        btnImport.setForeground(TXT_CHOCOLATE);
        btnImport.setPreferredSize(new Dimension(300, 50));

        btnImport.addActionListener(e -> {
            // Deschidem un JDialog care arată ca un notepad
            JDialog importDialog = new JDialog(this, "Notepad Import Cărți", true);
            importDialog.setSize(500, 400);
            importDialog.setLocationRelativeTo(this);
            importDialog.setLayout(new BorderLayout());

            // Notepad-ul (JTextArea)
            JTextArea notepad = new JTextArea();
            notepad.setFont(new Font("Monospaced", Font.PLAIN, 14));
            notepad.setToolTipText("Format: Titlu,Autor,ISBN,Gen (linie nouă pentru fiecare carte)");

            JScrollPane scrollPane = new JScrollPane(notepad);
            importDialog.add(scrollPane, BorderLayout.CENTER);

            // Butonul de procesare
            JButton btnProcess = new RoundedButton("Procesează și Salvează");
            btnProcess.setBackground(MAIN_SAGE_GREEN);

            btnProcess.addActionListener(ev -> {
                String fullText = notepad.getText();
                if (fullText.isEmpty()) {
                    JOptionPane.showMessageDialog(importDialog, "Păi notepad-ul e gol, n-avem ce importa!");
                    return;
                }

                // Transformăm textul în listă de Book
                List<Book> booksToImport = parseTextToBooks(fullText);

                // Trimitem la Service
                boolean done = bookService.handleImport(booksToImport);

                if (done) {
                    JOptionPane.showMessageDialog(importDialog, "Import realizat cu succes!");
                    importDialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(importDialog, "A apărut o eroare la salvare în baza de date.");
                }
            });

            importDialog.add(btnProcess, BorderLayout.SOUTH);
            importDialog.setVisible(true);
        });

        gbc.gridy = 1;
        content.add(btnImport, gbc);

        add(content, BorderLayout.CENTER);
    }

    private List<Book> parseTextToBooks(String text) {
        List<Book> list = new ArrayList<>();
        String[] lines = text.split("\n");

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split(",");

            // VERIFICARE: Acum avem 5 câmpuri (Titlu, Autor, ISBN, Gen, Coperta)
            if (parts.length == 5) {
                Book b = new Book(
                        null,
                        parts[0].trim(), // Titlu
                        parts[1].trim(), // Autor
                        parts[2].trim(), // ISBN
                        parts[3].trim(), // Gen
                        parts[4].trim()  // Coperta (ex: moara.jpg)
                );
                list.add(b);
            } else if (parts.length == 4) {
                // DACĂ adminul uită să pună coperta, punem una default să nu crape
                Book b = new Book(
                        null,
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim(),
                        parts[3].trim(),
                        "default.jpg" // Valoare de rezervă
                );
                list.add(b);
            }
        }
        return list;
    }


    // Refolosim butonul tău fain
    class RoundedButton extends JButton {
        public RoundedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
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