package test;

import app.AppInfo;
import java.util.List;
import model.Book;
import service.LibraryException;
import service.LibraryService;

/** Plain-Java test runner for the eight required LibraryService cases. */
public class LibraryServiceTest {

    private static int passed = 0;

    public static void main(String[] args) {
        LibraryService service = new LibraryService();

        System.out.println("Group Members: " + AppInfo.MEMBERS);
        System.out.println("Test Case | Expected Result | Actual Result | PASS/FAIL");
        System.out.println("---------------------------------------------------------------------------------------------------");

        testAddValidBook(service);
        testEmptyTitle(service);
        testDuplicateId(service);
        testViewAll(service);
        testViewEmpty();
        testUpdateExisting(service);
        testUpdateMissing(service);
        testDeleteExisting(service);

        System.out.println("---------------------------------------------------------------------------------------------------");
        System.out.println("Total: " + passed + "/8 tests passed.");
    }

    private static void testAddValidBook(LibraryService service) {
        String actual;
        try {
            service.addBook("B001", "Clean Code", "Robert Martin");
            Book added = service.findById("B001");
            actual = added != null && "Clean Code".equals(added.getTitle())
                    ? "Book is added" : "Book was not added";
        } catch (LibraryException e) {
            actual = "Unexpected error: " + e.getMessage();
        }
        record("1. Add a valid book", "Book is added", actual);
    }

    private static void testEmptyTitle(LibraryService service) {
        String actual;
        try {
            service.addBook("B002", "   ", "Someone");
            actual = "No validation message";
        } catch (LibraryException e) {
            actual = e.getMessage().toLowerCase().contains("title")
                    ? "Validation message shown" : "Wrong message: " + e.getMessage();
        }
        record("2. Add a book with an empty title", "Validation message shown", actual);
    }

    private static void testDuplicateId(LibraryService service) {
        String actual;
        try {
            service.addBook("B001", "Another Book", "Another Author");
            actual = "No error message";
        } catch (LibraryException e) {
            actual = e.getMessage().contains("already exists")
                    ? "Error message shown" : "Wrong message: " + e.getMessage();
        }
        record("3. Add a book with a duplicate ID", "Error message shown", actual);
    }

    private static void testViewAll(LibraryService service) {
        String actual;
        try {
            service.addBook("B002", "Effective Java", "Joshua Bloch");
        } catch (LibraryException e) {
            actual = "Setup failed: " + e.getMessage();
            record("4. View all books", "All books displayed", actual);
            return;
        }
        List<Book> books = service.getAll();
        actual = books.size() == 2 ? "All books displayed"
                : "Expected 2 books, found " + books.size();
        record("4. View all books", "All books displayed", actual);
    }

    private static void testViewEmpty() {
        LibraryService emptyService = new LibraryService();
        List<Book> books = emptyService.getAll();
        String actual = books.isEmpty()
                ? "Appropriate message shown" : "Empty library was not detected";
        record("5. View an empty library", "Appropriate message shown", actual);
    }

    private static void testUpdateExisting(LibraryService service) {
        String actual;
        try {
            service.updateBook("B001", "Refactoring", "Martin Fowler");
            Book updated = service.findById("B001");
            actual = "Refactoring".equals(updated.getTitle())
                    && "Martin Fowler".equals(updated.getAuthor())
                    ? "Details updated" : "Details were not updated";
        } catch (LibraryException e) {
            actual = "Unexpected error: " + e.getMessage();
        }
        record("6. Update an existing book", "Details updated", actual);
    }

    private static void testUpdateMissing(LibraryService service) {
        String actual;
        try {
            service.updateBook("B999", "Ghost Book", "Nobody");
            actual = "Missing book was not handled";
        } catch (LibraryException e) {
            actual = e.getMessage().contains("No book found")
                    ? "Handled gracefully" : "Wrong message: " + e.getMessage();
        }
        record("7. Update a non-existing book", "Handled gracefully", actual);
    }

    private static void testDeleteExisting(LibraryService service) {
        String actual;
        try {
            service.deleteBook("B002");
            actual = service.findById("B002") == null
                    ? "Book removed" : "Book was not removed";
        } catch (LibraryException e) {
            actual = "Unexpected error: " + e.getMessage();
        }
        record("8. Delete an existing book", "Book removed", actual);
    }

    private static void record(String name, String expected, String actual) {
        boolean ok = expected.equals(actual);
        if (ok) {
            passed++;
        }
        System.out.printf("%-45s | %-25s | %-25s | %s%n",
                name, expected, actual, ok ? "PASS" : "FAIL");
    }
}
