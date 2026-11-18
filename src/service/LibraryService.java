package service;

import java.util.LinkedHashMap;
import java.util.Map;
import model.Book;

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
}
