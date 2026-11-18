package ui;

import app.AppInfo;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import model.Book;
import model.BookStatus;
import service.LibraryException;
import service.LibraryService;

/** Swing window for adding, viewing, updating, deleting and issuing books. */
public class LibraryFrame extends JFrame {

    private final LibraryService service = new LibraryService();
    private final JTextField idField = new JTextField(12);
    private final JTextField titleField = new JTextField(20);
    private final JTextField authorField = new JTextField(20);
    private final JComboBox<BookStatus> statusCombo =
            new JComboBox<>(BookStatus.values());
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Author", "Status"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable table = new JTable(tableModel);
    private final JPanel tablePanel = new JPanel(new CardLayout());
    private final JTextArea messageArea = new JTextArea(3, 40);

    public LibraryFrame() {
        setTitle("Library Book Management System - " + AppInfo.MEMBERS);
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(buildInputPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getSelectionModel().addListSelectionListener(event -> loadSelectedBook());
        updateTable();
        showMessage("Library is empty. Add your first book using the form above.");
    }

    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Book details"));
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(5, 5, 5, 5);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Book ID:"), c);
        c.gridx = 1;
        panel.add(idField, c);
        c.gridx = 2;
        panel.add(new JLabel("Status:"), c);
        c.gridx = 3;
        panel.add(statusCombo, c);

        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Title:"), c);
        c.gridx = 1;
        panel.add(titleField, c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Author:"), c);
        c.gridx = 1;
        panel.add(authorField, c);

        JPanel buttons = new JPanel();
        buttons.add(makeButton("Add", event -> addBook()));
        buttons.add(makeButton("Update", event -> updateBook()));
        buttons.add(makeButton("Delete", event -> deleteBook()));
        buttons.add(makeButton("Set Status", event -> setStatus()));
        buttons.add(makeButton("Clear", event -> clearForm()));
        c.gridx = 2;
        c.gridy = 1;
        c.gridwidth = 2;
        c.gridheight = 2;
        c.fill = GridBagConstraints.BOTH;
        panel.add(buttons, c);
        return panel;
    }

    private JPanel buildTablePanel() {
        JScrollPane scrollPane = new JScrollPane(table);
        JLabel emptyLabel = new JLabel("Library is empty", SwingConstants.CENTER);
        emptyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        tablePanel.add(scrollPane, "table");
        tablePanel.add(emptyLabel, "empty");
        return tablePanel;
    }

    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        messageArea.setBorder(BorderFactory.createTitledBorder("Messages"));
        panel.add(new JScrollPane(messageArea), BorderLayout.CENTER);

        JLabel footer = new JLabel("Group Members: " + AppInfo.MEMBERS, SwingConstants.CENTER);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    private JButton makeButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    private void addBook() {
        try {
            service.addBook(idField.getText(), titleField.getText(), authorField.getText());
            updateTable();
            showMessage("Book '" + idField.getText().trim() + "' added successfully.");
        } catch (LibraryException e) {
            showError(e);
        }
    }

    private void updateBook() {
        try {
            service.updateBook(idField.getText(), titleField.getText(), authorField.getText());
            updateTable();
            showMessage("Book '" + idField.getText().trim() + "' updated successfully.");
        } catch (LibraryException e) {
            showError(e);
        }
    }

    private void deleteBook() {
        try {
            service.deleteBook(idField.getText());
            updateTable();
            clearForm();
            showMessage("Book deleted successfully.");
        } catch (LibraryException e) {
            showError(e);
        }
    }

    private void setStatus() {
        try {
            BookStatus status = (BookStatus) statusCombo.getSelectedItem();
            service.setStatus(idField.getText(), status);
            updateTable();
            showMessage("Status set to " + status + " for book '" + idField.getText().trim() + "'.");
        } catch (LibraryException e) {
            showError(e);
        }
    }

    private void clearForm() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        statusCombo.setSelectedItem(BookStatus.AVAILABLE);
        table.clearSelection();
        showMessage("Form cleared.");
    }

    private void loadSelectedBook() {
        int row = table.getSelectedRow();
        if (row < 0) {
            return;
        }
        idField.setText(String.valueOf(table.getValueAt(row, 0)));
        titleField.setText(String.valueOf(table.getValueAt(row, 1)));
        authorField.setText(String.valueOf(table.getValueAt(row, 2)));
        statusCombo.setSelectedItem(table.getValueAt(row, 3));
    }

    private void updateTable() {
        tableModel.setRowCount(0);
        List<Book> books = service.getAll();
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                book.getId(), book.getTitle(), book.getAuthor(), book.getStatus()
            });
        }
        CardLayout cards = (CardLayout) tablePanel.getLayout();
        cards.show(tablePanel, books.isEmpty() ? "empty" : "table");
    }

    private void showMessage(String text) {
        messageArea.setText(text);
    }

    private void showError(LibraryException e) {
        messageArea.setText("Error: " + e.getMessage());
        JOptionPane.showMessageDialog(this, e.getMessage(), "Library Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
