package ui;

import app.AppInfo;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
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
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import model.Book;
import model.BookStatus;
import service.LibraryException;
import service.LibraryService;

/**
 * Swing window for adding, viewing, updating, deleting and issuing books. The
 * window title and footer both show the names and IDs of the group members.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public class LibraryFrame extends JFrame {

    /** In-memory business logic used by all buttons. */
    private final LibraryService service = new LibraryService();
    /** Input field for the unique book ID. */
    private final JTextField idField = new JTextField(12);
    /** Input field for the book title. */
    private final JTextField titleField = new JTextField(20);
    /** Input field for the book author. */
    private final JTextField authorField = new JTextField(20);
    /** Combo box for choosing AVAILABLE, ISSUED or RESERVED. */
    private final JComboBox<BookStatus> statusCombo =
            new JComboBox<>(BookStatus.values());
    /** Read-only data model behind the book table. */
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Author", "Status"}, 0) {
        /**
         * Keeps the table read-only so changes go through the buttons.
         *
         * @param row    the row index
         * @param column the column index
         * @return always false, because cells must not be edited directly
         */
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    /** Table showing every book in the library. */
    private final JTable table = new JTable(tableModel);
    /** Card panel that swaps the table with the empty-library label. */
    private final JPanel tablePanel = new JPanel(new CardLayout());
    /** Area used for success messages and friendly errors. */
    private final JTextArea messageArea = new JTextArea(3, 40);

    /**
     * Builds the window, wires the buttons to the service and shows the table.
     */
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

    /**
     * Creates the form with input fields, the status combo box and the
     * Add, Update, Delete, Set Status and Clear buttons.
     *
     * @return the configured input panel
     */
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

    /**
     * Creates the centre panel that swaps between the table and a large
     * "Library is empty" label.
     *
     * @return the panel holding both card views
     */
    private JPanel buildTablePanel() {
        JScrollPane scrollPane = new JScrollPane(table);
        JLabel emptyLabel = new JLabel("Library is empty", SwingConstants.CENTER);
        emptyLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 22));
        tablePanel.add(scrollPane, "table");
        tablePanel.add(emptyLabel, "empty");
        return tablePanel;
    }

    /**
     * Creates the message area and the footer with the member names and IDs.
     *
     * @return the configured bottom panel
     */
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

    /**
     * Creates a button with an action listener.
     *
     * @param text     the label printed on the button
     * @param listener the action performed when the button is clicked
     * @return the new button
     */
    private JButton makeButton(String text, java.awt.event.ActionListener listener) {
        JButton button = new JButton(text);
        button.addActionListener(listener);
        return button;
    }

    /**
     * Adds the book typed into the form and refreshes the table.
     */
    private void addBook() {
        try {
            service.addBook(idField.getText(), titleField.getText(), authorField.getText());
            updateTable();
            showMessage("Book '" + idField.getText().trim() + "' added successfully.");
        } catch (LibraryException e) {
            showError(e);
        }
    }

    /**
     * Updates the book whose ID is typed into the form.
     */
    private void updateBook() {
        try {
            service.updateBook(idField.getText(), titleField.getText(), authorField.getText());
            updateTable();
            showMessage("Book '" + idField.getText().trim() + "' updated successfully.");
        } catch (LibraryException e) {
            showError(e);
        }
    }

    /**
     * Deletes the book whose ID is typed into the form.
     */
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

    /**
     * Applies the status chosen in the combo box to the current book.
     */
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

    /**
     * Empties the input fields and resets the status combo box.
     */
    private void clearForm() {
        idField.setText("");
        titleField.setText("");
        authorField.setText("");
        statusCombo.setSelectedItem(BookStatus.AVAILABLE);
        table.clearSelection();
        showMessage("Form cleared.");
    }

    /**
     * Copies the selected table row into the form for editing or status changes.
     */
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

    /**
     * Rebuilds the table rows and shows the empty-library message when needed.
     */
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

    /**
     * Writes a normal message to the message area.
     *
     * @param text the message to display
     */
    private void showMessage(String text) {
        messageArea.setText(text);
    }

    /**
     * Reports a service error in the message area and in an error dialog, never
     * as a stack trace.
     *
     * @param e the exception raised by the service
     */
    private void showError(LibraryException e) {
        messageArea.setText("Error: " + e.getMessage());
        JOptionPane.showMessageDialog(this, e.getMessage(), "Library Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Single place for the GUI palette, fonts and look-and-feel setup.
     *
     * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
     */
    public static final class Theme {

        /** Dark navy background of the header bar. */
        public static final Color HEADER_BG = new Color(33, 45, 61);
        /** Light text used for the header title. */
        public static final Color HEADER_TITLE_FG = new Color(255, 255, 255);
        /** Softer light text used for the members line in the header. */
        public static final Color HEADER_MEMBERS_FG = new Color(190, 206, 224);
        /** Light neutral background of the whole window. */
        public static final Color BACKGROUND = new Color(245, 247, 250);
        /** Accent colour of the Add button. */
        public static final Color ACCENT = new Color(41, 98, 173);
        /** Lighter accent shown while hovering the Add button. */
        public static final Color ACCENT_HOVER = new Color(56, 126, 219);
        /** Red-ish colour of the Delete button. */
        public static final Color DELETE = new Color(183, 55, 55);
        /** Lighter red shown while hovering the Delete button. */
        public static final Color DELETE_HOVER = new Color(214, 69, 69);
        /** Neutral colour of the other action buttons. */
        public static final Color NEUTRAL_BUTTON = new Color(90, 103, 117);
        /** Lighter neutral shown while hovering the other buttons. */
        public static final Color NEUTRAL_BUTTON_HOVER = new Color(112, 128, 145);
        /** Grey background of disabled action buttons. */
        public static final Color DISABLED_BUTTON = new Color(190, 195, 200);
        /** Normal dark text colour. */
        public static final Color TEXT = new Color(33, 37, 41);
        /** Muted grey for small text and the empty-library message. */
        public static final Color MUTED = new Color(108, 117, 125);
        /** Green colour for successful status-bar messages. */
        public static final Color SUCCESS = new Color(25, 135, 84);
        /** Red colour for error status-bar messages. */
        public static final Color ERROR = new Color(200, 35, 51);
        /** Background of the table header. */
        public static final Color TABLE_HEADER_BG = new Color(222, 230, 240);
        /** Background of every second table row. */
        public static final Color ROW_ALT = new Color(236, 241, 247);
        /** Background of AVAILABLE status cells. */
        public static final Color AVAILABLE_BG = new Color(223, 245, 230);
        /** Foreground of AVAILABLE status cells. */
        public static final Color AVAILABLE_FG = new Color(20, 110, 60);
        /** Background of ISSUED status cells. */
        public static final Color ISSUED_BG = new Color(255, 235, 225);
        /** Foreground of ISSUED status cells. */
        public static final Color ISSUED_FG = new Color(170, 60, 20);
        /** Background of RESERVED status cells. */
        public static final Color RESERVED_BG = new Color(255, 245, 215);
        /** Foreground of RESERVED status cells. */
        public static final Color RESERVED_FG = new Color(140, 95, 10);
        /** Standard spacing and padding used by every panel. */
        public static final int PAD = 12;
        /** Shared UI font: Segoe UI 13pt, or Dialog 13pt as fallback. */
        public static final Font FONT = createFont();
        /** Font of the header title. */
        public static final Font HEADER_TITLE_FONT = FONT.deriveFont(Font.BOLD, 17f);
        /** Font of the members line in the header. */
        public static final Font HEADER_MEMBERS_FONT = FONT.deriveFont(Font.PLAIN, 12f);
        /** Font of the centred empty-library message. */
        public static final Font EMPTY_FONT = FONT.deriveFont(Font.BOLD, 22f);
        /** Font of the bottom status bar. */
        public static final Font STATUS_FONT = FONT.deriveFont(Font.BOLD, 12f);
        /** Font of the small grey footer. */
        public static final Font FOOTER_FONT = FONT.deriveFont(Font.PLAIN, 11f);

        /**
         * Prevents instantiation of this utility class.
         */
        private Theme() {
        }

        /**
         * Selects the system look and feel, falling back to Nimbus and then
         * silently to the platform default. Call before creating any window.
         */
        public static void applyLookAndFeel() {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception firstFailure) {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception secondFailure) {
                    // Keep the default look and feel; the application still works.
                }
            }
        }

        /**
         * Builds the shared font, preferring Segoe UI when it is installed.
         *
         * @return Segoe UI 13pt when available, otherwise Dialog 13pt
         */
        private static Font createFont() {
            Font preferred = new Font("Segoe UI", Font.PLAIN, 13);
            String[] families = GraphicsEnvironment.getLocalGraphicsEnvironment()
                    .getAvailableFontFamilyNames();
            for (String family : families) {
                if ("Segoe UI".equalsIgnoreCase(family)) {
                    return preferred;
                }
            }
            return new Font(Font.DIALOG, Font.PLAIN, 13);
        }
    }
}
