package ui;

import app.AppInfo;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import model.Book;
import model.BookStatus;
import service.LibraryException;
import service.LibraryService;

/**
 * Polished Swing window for adding, viewing, updating, deleting and issuing
 * books. It has a dark header, a themed table with colour-coded statuses, a
 * live search field, a colour-coded status bar and a footer that shows both
 * group members and their IDs.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public class LibraryFrame extends JFrame {

    /** Index of the Status column in the book table. */
    private static final int STATUS_COLUMN = 3;

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
    /** Centred message shown when the table has no rows. */
    private final JLabel emptyLabel = new JLabel("Library is empty", SwingConstants.CENTER);
    /** Text field that filters the table as the user types. */
    private final JTextField searchField = new JTextField(18);
    /** Slim bar showing the last success or error message. */
    private final JLabel statusBar = new JLabel("Ready");
    /** Adds a new book. */
    private final JButton addButton;
    /** Updates the selected book. */
    private final JButton updateButton;
    /** Deletes the selected book. */
    private final JButton deleteButton;
    /** Changes the status of the selected book. */
    private final JButton statusButton;
    /** Clears the form and the table selection. */
    private final JButton clearButton;

    /**
     * Builds the window, wires the buttons to the service and shows the table.
     */
    public LibraryFrame() {
        setTitle("Library Book Management System - " + AppInfo.MEMBERS);
        setSize(950, 650);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(Theme.BACKGROUND);

        addButton = makeButton("Add", event -> addBook(), Theme.ACCENT,
                Theme.ACCENT_HOVER, KeyEvent.VK_A, "Add the book in the form (Alt+A)");
        updateButton = makeButton("Update", event -> updateBook(), Theme.NEUTRAL_BUTTON,
                Theme.NEUTRAL_BUTTON_HOVER, KeyEvent.VK_U, "Update the selected book (Alt+U)");
        deleteButton = makeButton("Delete", event -> deleteBook(), Theme.DELETE,
                Theme.DELETE_HOVER, KeyEvent.VK_D, "Delete the selected book (Alt+D)");
        statusButton = makeButton("Set Status", event -> setStatus(), Theme.NEUTRAL_BUTTON,
                Theme.NEUTRAL_BUTTON_HOVER, KeyEvent.VK_S, "Set the selected book's status (Alt+S)");
        clearButton = makeButton("Clear", event -> clearForm(), Theme.NEUTRAL_BUTTON,
                Theme.NEUTRAL_BUTTON_HOVER, KeyEvent.VK_C, "Clear the form (Alt+C)");
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        statusButton.setEnabled(false);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Theme.BACKGROUND);
        topPanel.add(buildHeaderPanel(), BorderLayout.NORTH);
        topPanel.add(buildInputPanel(), BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable();
        table.getSelectionModel().addListSelectionListener(event -> {
            loadSelectedBook();
            updateButtonStates();
        });
        updateTable();
        showMessage("Library is empty. Add your first book using the form above.");
    }

    /**
     * Creates the dark header bar with the application title and the members
     * line in lighter text.
     *
     * @return the configured header panel
     */
    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(0, 2));
        header.setBackground(Theme.HEADER_BG);
        header.setBorder(BorderFactory.createEmptyBorder(
                Theme.PAD, Theme.PAD, Theme.PAD, Theme.PAD));

        JLabel title = new JLabel("Library Book Management System");
        title.setFont(Theme.HEADER_TITLE_FONT);
        title.setForeground(Theme.HEADER_TITLE_FG);
        header.add(title, BorderLayout.WEST);

        JLabel members = new JLabel(AppInfo.MEMBERS);
        members.setFont(Theme.HEADER_MEMBERS_FONT);
        members.setForeground(Theme.HEADER_MEMBERS_FG);
        header.add(members, BorderLayout.SOUTH);
        return header;
    }

    /**
     * Creates the form with input fields, the status combo box and the
     * Add, Update, Delete, Set Status and Clear buttons.
     *
     * @return the configured input panel
     */
    private JPanel buildInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(
                Theme.PAD, Theme.PAD, Theme.PAD, Theme.PAD));

        Dimension fieldSize = new Dimension(170, 30);
        idField.setPreferredSize(fieldSize);
        titleField.setPreferredSize(fieldSize);
        authorField.setPreferredSize(fieldSize);
        statusCombo.setPreferredSize(fieldSize);
        idField.setFont(Theme.FONT);
        titleField.setFont(Theme.FONT);
        authorField.setFont(Theme.FONT);
        statusCombo.setFont(Theme.FONT);

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, Theme.PAD, 10, Theme.PAD);
        c.anchor = GridBagConstraints.WEST;

        JLabel section = new JLabel("Book details");
        section.setFont(Theme.FONT.deriveFont(Font.BOLD));
        section.setForeground(Theme.TEXT);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        panel.add(section, c);
        c.gridwidth = 1;

        addLabelledField(panel, c, 0, 1, "Book ID:", idField);
        addLabelledField(panel, c, 2, 1, "Status:", statusCombo);
        addLabelledField(panel, c, 0, 2, "Title:", titleField);
        addLabelledField(panel, c, 2, 2, "Author:", authorField);

        JPanel buttons = new JPanel();
        buttons.setOpaque(false);
        buttons.add(addButton);
        buttons.add(updateButton);
        buttons.add(deleteButton);
        buttons.add(statusButton);
        buttons.add(clearButton);
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 4;
        c.anchor = GridBagConstraints.EAST;
        panel.add(buttons, c);
        return panel;
    }

    /**
     * Adds one label and its field to the form grid.
     *
     * @param panel the form panel
     * @param c     shared grid constraints
     * @param x     column of the label
     * @param y     row of the label and field
     * @param text  text of the label
     * @param field the field placed in the next column
     */
    private void addLabelledField(JPanel panel, GridBagConstraints c, int x, int y,
                                  String text, JComponent field) {
        JLabel label = new JLabel(text);
        label.setFont(Theme.FONT);
        label.setForeground(Theme.TEXT);
        c.gridx = x;
        c.gridy = y;
        c.fill = GridBagConstraints.NONE;
        panel.add(label, c);
        c.gridx = x + 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, c);
    }

    /**
     * Creates the centre panel that swaps between the table and a large
     * "Library is empty" label.
     *
     * @return the panel holding both card views
     */
    private JPanel buildTablePanel() {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(Color.WHITE);
        emptyLabel.setFont(Theme.EMPTY_FONT);
        emptyLabel.setForeground(Theme.MUTED);
        tablePanel.setBackground(Theme.BACKGROUND);
        tablePanel.add(scrollPane, "table");
        tablePanel.add(emptyLabel, "empty");

        searchField.setFont(Theme.FONT);
        searchField.setPreferredSize(new Dimension(220, 30));
        searchField.setToolTipText("Filter by ID, title or author");
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            /**
             * Refreshes the table after text is inserted.
             *
             * @param event the document event
             */
            @Override
            public void insertUpdate(DocumentEvent event) {
                updateTable();
            }

            /**
             * Refreshes the table after text is removed.
             *
             * @param event the document event
             */
            @Override
            public void removeUpdate(DocumentEvent event) {
                updateTable();
            }

            /**
             * Refreshes the table when the document changes style.
             *
             * @param event the document event
             */
            @Override
            public void changedUpdate(DocumentEvent event) {
                updateTable();
            }
        });

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(Theme.FONT);
        searchLabel.setForeground(Theme.TEXT);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, Theme.PAD, 10));
        searchPanel.setBackground(Theme.BACKGROUND);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);

        JPanel tableArea = new JPanel(new BorderLayout());
        tableArea.setBackground(Theme.BACKGROUND);
        tableArea.add(searchPanel, BorderLayout.NORTH);
        tableArea.add(tablePanel, BorderLayout.CENTER);
        return tableArea;
    }

    /**
     * Applies the row height, header style, column widths and custom renderer
     * to the book table.
     */
    private void styleTable() {
        table.setRowHeight(28);
        table.setFont(Theme.FONT);
        table.setBackground(Color.WHITE);
        table.setForeground(Theme.TEXT);
        table.setGridColor(new Color(225, 230, 235));
        table.setSelectionBackground(new Color(205, 222, 245));
        table.setSelectionForeground(Theme.TEXT);
        table.setDefaultRenderer(Object.class, new StatusCellRenderer());

        table.getTableHeader().setFont(Theme.FONT.deriveFont(Font.BOLD));
        table.getTableHeader().setBackground(Theme.TABLE_HEADER_BG);
        table.getTableHeader().setForeground(Theme.TEXT);
        table.getTableHeader().setReorderingAllowed(false);
        table.getColumnModel().getColumn(0).setPreferredWidth(100);
        table.getColumnModel().getColumn(1).setPreferredWidth(240);
        table.getColumnModel().getColumn(2).setPreferredWidth(190);
        table.getColumnModel().getColumn(STATUS_COLUMN).setPreferredWidth(120);
    }

    /**
     * Creates the slim status bar and the footer with the member names and IDs.
     *
     * @return the configured bottom panel
     */
    private JPanel buildBottomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Theme.BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(
                0, Theme.PAD, Theme.PAD, Theme.PAD));

        statusBar.setFont(Theme.STATUS_FONT);
        statusBar.setForeground(Theme.SUCCESS);
        statusBar.setOpaque(true);
        statusBar.setBackground(Color.WHITE);
        statusBar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
        panel.add(statusBar, BorderLayout.CENTER);

        JLabel footer = new JLabel("Group Members: " + AppInfo.MEMBERS, SwingConstants.CENTER);
        footer.setFont(Theme.FOOTER_FONT);
        footer.setForeground(Theme.MUTED);
        panel.add(footer, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Creates a styled button with a mnemonic, tooltip and hover colour.
     *
     * @param text     the label printed on the button
     * @param listener the action performed when the button is clicked
     * @param base     the normal background colour
     * @param hover    the background colour while the mouse is over the button
     * @param mnemonic the keyboard mnemonic key code
     * @param tooltip  the tooltip shown on hover
     * @return the new button
     */
    private JButton makeButton(String text, ActionListener listener, Color base,
                               Color hover, int mnemonic, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(Theme.FONT);
        button.setPreferredSize(new Dimension(115, 32));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(false);
        button.setBackground(base);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(6, 10, 6, 10));
        button.setMnemonic(mnemonic);
        button.setToolTipText(tooltip);
        button.addActionListener(listener);
        button.addMouseListener(new MouseAdapter() {
            /**
             * Applies the hover colour when the mouse enters the button.
             *
             * @param event the mouse event
             */
            @Override
            public void mouseEntered(MouseEvent event) {
                if (button.isEnabled()) {
                    button.setBackground(hover);
                }
            }

            /**
             * Restores the base colour when the mouse leaves the button.
             *
             * @param event the mouse event
             */
            @Override
            public void mouseExited(MouseEvent event) {
                if (button.isEnabled()) {
                    button.setBackground(base);
                }
            }
        });
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
        updateButtonStates();
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
     * Rebuilds the table from the books that match the current search text and
     * shows the correct empty message.
     */
    private void updateTable() {
        String query = searchField.getText() == null
                ? "" : searchField.getText().trim().toLowerCase();
        List<Book> visibleBooks = new ArrayList<>();
        for (Book book : service.getAll()) {
            if (matchesFilter(book, query)) {
                visibleBooks.add(book);
            }
        }

        tableModel.setRowCount(0);
        for (Book book : visibleBooks) {
            tableModel.addRow(new Object[]{
                book.getId(), book.getTitle(), book.getAuthor(), book.getStatus()
            });
        }
        emptyLabel.setText(query.isEmpty() ? "Library is empty" : "No matching books");
        CardLayout cards = (CardLayout) tablePanel.getLayout();
        cards.show(tablePanel, visibleBooks.isEmpty() ? "empty" : "table");
        updateButtonStates();
    }

    /**
     * Checks whether a book matches the search text.
     *
     * @param book  the book being tested
     * @param query the lowercase search text
     * @return true when the query is empty or matches ID, title or author
     */
    private boolean matchesFilter(Book book, String query) {
        if (query.isEmpty()) {
            return true;
        }
        return book.getId().toLowerCase().contains(query)
                || book.getTitle().toLowerCase().contains(query)
                || book.getAuthor().toLowerCase().contains(query);
    }

    /**
     * Enables the row-dependent buttons only when a table row is selected.
     */
    private void updateButtonStates() {
        boolean hasSelection = table.getSelectedRow() >= 0;
        setActionEnabled(updateButton, hasSelection, Theme.NEUTRAL_BUTTON);
        setActionEnabled(deleteButton, hasSelection, Theme.DELETE);
        setActionEnabled(statusButton, hasSelection, Theme.NEUTRAL_BUTTON);
    }

    /**
     * Sets a button's enabled state and its normal background colour.
     *
     * @param button  the button to update
     * @param enabled whether the button can be clicked
     * @param base    the colour restored when the button is enabled
     */
    private void setActionEnabled(JButton button, boolean enabled, Color base) {
        button.setEnabled(enabled);
        button.setBackground(enabled ? base : Theme.DISABLED_BUTTON);
    }

    /**
     * Writes a success message in green to the status bar.
     *
     * @param text the message to display
     */
    private void showMessage(String text) {
        statusBar.setText(text);
        statusBar.setForeground(Theme.SUCCESS);
    }

    /**
     * Reports a service error in red in the status bar and in an error dialog,
     * never as a stack trace.
     *
     * @param e the exception raised by the service
     */
    private void showError(LibraryException e) {
        statusBar.setText("Error: " + e.getMessage());
        statusBar.setForeground(Theme.ERROR);
        JOptionPane.showMessageDialog(this, e.getMessage(), "Library Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Stripes alternate rows and colour-codes the Status column.
     *
     * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
     */
    private static final class StatusCellRenderer extends DefaultTableCellRenderer {

        /**
         * Builds a cell renderer with row stripes and status colours.
         *
         * @param table    the table being rendered
         * @param value    the value of the current cell
         * @param isSelected whether the row is selected
         * @param hasFocus  whether the cell has focus
         * @param row      the row index
         * @param column   the column index
         * @return the configured renderer component
         */
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            if (!isSelected) {
                Color background = (row % 2 == 0) ? Color.WHITE : Theme.ROW_ALT;
                Color foreground = Theme.TEXT;
                if (column == STATUS_COLUMN && value instanceof BookStatus) {
                    switch ((BookStatus) value) {
                        case AVAILABLE:
                            background = Theme.AVAILABLE_BG;
                            foreground = Theme.AVAILABLE_FG;
                            break;
                        case ISSUED:
                            background = Theme.ISSUED_BG;
                            foreground = Theme.ISSUED_FG;
                            break;
                        case RESERVED:
                            background = Theme.RESERVED_BG;
                            foreground = Theme.RESERVED_FG;
                            break;
                        default:
                            break;
                    }
                }
                component.setBackground(background);
                component.setForeground(foreground);
            }
            return component;
        }
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
