package service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.Book;
import model.BookStatus;

/** In-memory business logic for the library. Contains no GUI code. */
public class LibraryService {

    private final Map<String, Book> books = new LinkedHashMap<>();

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
            throw new LibraryException("A book with ID '" + cleanId + "' already exists. Use a unique ID.");
        }

        books.put(cleanId, new Book(cleanId, cleanTitle, cleanAuthor));
    }

    public Book findById(String id) {
        if (id == null) {
            return null;
        }
        return books.get(id.trim());
    }

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

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

    public void deleteBook(String id) throws LibraryException {
        Book book = findById(id);
        if (book == null) {
            throw new LibraryException("No book found with ID '" + id + "'. Nothing was deleted.");
        }
        books.remove(book.getId());
    }

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
