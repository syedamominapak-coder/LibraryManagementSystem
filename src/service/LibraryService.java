package service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Book;
import model.BookStatus;

/**
 * In-memory business logic for the library. This class contains no GUI code so
 * it can be tested headlessly.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public class LibraryService {

    /** Books stored in memory, keyed by their unique ID. */
    private final Map<String, Book> books = new LinkedHashMap<>();

    /**
     * Creates an empty library.
     */
    public LibraryService() {
    }

    /**
     * Adds a book after checking that the ID is unique and the text fields are
     * not empty.
     *
     * @param id     unique identifier of the new book
     * @param title  title of the new book
     * @param author author of the new book
     * @throws LibraryException if the ID or text fields are empty, or the ID is
     *                          already used
     */
    public void addBook(String id, String title, String author) throws LibraryException {
        String cleanId = id == null ? "" : id.trim();
        String cleanTitle = title == null ? "" : title.trim();
        String cleanAuthor = author == null ? "" : author.trim();

        if (cleanId.isEmpty()) {
            throw new LibraryException("Book ID must not be empty.");
        }
        if (cleanTitle.isEmpty()) {
            throw new LibraryException("Title must not be empty. Please enter a title.");
        }
        if (cleanAuthor.isEmpty()) {
            throw new LibraryException("Author must not be empty. Please enter an author.");
        }
        if (books.containsKey(cleanId)) {
            throw new LibraryException("A book with ID '" + cleanId + "' already exists. Please use a different, unique ID.");
        }

        books.put(cleanId, new Book(cleanId, cleanTitle, cleanAuthor));
    }

    /**
     * Looks up a book by its ID.
     *
     * @param id the ID to search for
     * @return the matching book, or {@code null} when no book has that ID
     */
    public Book findById(String id) {
        if (id == null) {
            return null;
        }
        return books.get(id.trim());
    }

    /**
     * Returns all books in insertion order.
     *
     * @return a new list containing every book currently stored
     */
    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    /**
     * Replaces the title and author of an existing book.
     *
     * @param id     ID of the book to update
     * @param title  new title
     * @param author new author
     * @throws LibraryException if the book does not exist, or a text field is empty
     */
    public void updateBook(String id, String title, String author) throws LibraryException {
        Book book = findById(id);
        if (book == null) {
            throw new LibraryException("No book found with ID '" + id + "'. Nothing was updated.");
        }

        String cleanTitle = title == null ? "" : title.trim();
        String cleanAuthor = author == null ? "" : author.trim();
        if (cleanTitle.isEmpty()) {
            throw new LibraryException("Title must not be empty. Please enter a title.");
        }
        if (cleanAuthor.isEmpty()) {
            throw new LibraryException("Author must not be empty. Please enter an author.");
        }

        book.setTitle(cleanTitle);
        book.setAuthor(cleanAuthor);
    }

    /**
     * Removes a book from the library.
     *
     * @param id ID of the book to remove
     * @throws LibraryException if no book has that ID
     */
    public void deleteBook(String id) throws LibraryException {
        Book book = findById(id);
        if (book == null) {
            throw new LibraryException("No book found with ID '" + id + "'. Nothing was deleted.");
        }
        books.remove(book.getId());
    }

    /**
     * Sets the loan status of an existing book.
     *
     * @param id     ID of the book to change
     * @param status one of AVAILABLE, ISSUED or RESERVED
     * @throws LibraryException if the book does not exist, or the status is null
     */
    public void setStatus(String id, BookStatus status) throws LibraryException {
        Book book = findById(id);
        if (book == null) {
            throw new LibraryException("No book found with ID '" + id + "'. Status was not changed.");
        }
        if (status == null) {
            throw new LibraryException("Please choose a status: AVAILABLE, ISSUED or RESERVED.");
        }
        book.setStatus(status);
    }
}
