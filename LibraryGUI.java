import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class LibraryGUI extends JFrame {
    private LibraryManager manager;
    private JTable bookTable;
    private JTable memberTable;
    private DefaultTableModel bookTableModel;
    private DefaultTableModel memberTableModel;

    public LibraryGUI() {
        try {
            manager = new LibraryManager();
        } catch (IOException e) {
            showError("Failed to load data: " + e.getMessage());
            return;
        }

        setTitle("Library Management System");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Books", createBooksPanel());
        tabbedPane.addTab("Members", createMembersPanel());
        tabbedPane.addTab("Borrow / Return", createBorrowReturnPanel());

        add(tabbedPane);

        setVisible(true);
    }

    private JPanel createBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        bookTableModel = new DefaultTableModel(new Object[]{"ID", "Title", "Status"}, 0);
        bookTable = new JTable(bookTableModel);
        refreshBookTable();

        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel form = new JPanel();
        JTextField titleField = new JTextField(20);
        JButton addButton = new JButton("Add Book");

        form.add(new JLabel("Title:"));
        form.add(titleField);
        form.add(addButton);

        addButton.addActionListener(e -> {
            String title = titleField.getText().trim();
            if (!title.isEmpty()) {
                try {
                    manager.addBook(title);
                    refreshBookTable();
                    titleField.setText("");
                } catch (IOException ex) {
                    showError("Failed to add book.");
                }
            }
        });

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createMembersPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        memberTableModel = new DefaultTableModel(new Object[]{"ID", "Name", "Borrowed Books"}, 0);
        memberTable = new JTable(memberTableModel);
        refreshMemberTable();

        panel.add(new JScrollPane(memberTable), BorderLayout.CENTER);

        JPanel form = new JPanel();
        JTextField nameField = new JTextField(20);
        JButton addButton = new JButton("Add Member");

        form.add(new JLabel("Name:"));
        form.add(nameField);
        form.add(addButton);

        addButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (!name.isEmpty()) {
                try {
                    manager.addMember(name);
                    refreshMemberTable();
                    nameField.setText("");
                } catch (IOException ex) {
                    showError("Failed to add member.");
                }
            }
        });

        panel.add(form, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createBorrowReturnPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));

        JPanel borrowPanel = new JPanel();
        JTextField bookIdBorrow = new JTextField(5);
        JTextField memberIdBorrow = new JTextField(5);
        JButton borrowButton = new JButton("Borrow");

        borrowPanel.add(new JLabel("Book ID:"));
        borrowPanel.add(bookIdBorrow);
        borrowPanel.add(new JLabel("Member ID:"));
        borrowPanel.add(memberIdBorrow);
        borrowPanel.add(borrowButton);

        borrowButton.addActionListener(e -> {
            try {
                manager.borrowBook(bookIdBorrow.getText().trim(), memberIdBorrow.getText().trim());
                refreshBookTable();
                refreshMemberTable();
            } catch (IOException ex) {
                showError("Failed to borrow book.");
            }
        });

        JPanel returnPanel = new JPanel();
        JTextField bookIdReturn = new JTextField(5);
        JButton returnButton = new JButton("Return");

        returnPanel.add(new JLabel("Book ID:"));
        returnPanel.add(bookIdReturn);
        returnPanel.add(returnButton);

        returnButton.addActionListener(e -> {
            try {
                manager.returnBook(bookIdReturn.getText().trim());
                refreshBookTable();
                refreshMemberTable();
            } catch (IOException ex) {
                showError("Failed to return book.");
            }
        });

        panel.add(borrowPanel);
        panel.add(returnPanel);
        return panel;
    }

    private void refreshBookTable() {
        bookTableModel.setRowCount(0);
        for (Book book : manager.getBooks()) {
            bookTableModel.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.isAvailable() ? "Available" : "Borrowed"
            });
        }
    }

    private void refreshMemberTable() {
        memberTableModel.setRowCount(0);
        for (Member member : manager.getMembers()) {
            memberTableModel.addRow(new Object[]{
                member.getMemberId(),
                member.getName(),
                String.join(", ", member.getBorrowedBooks())
            });
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LibraryGUI::new);
    }
}
