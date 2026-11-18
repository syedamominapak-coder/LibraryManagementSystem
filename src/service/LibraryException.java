package service;

/** Checked exception for validation errors and missing books. */
public class LibraryException extends Exception {

    public LibraryException(String message) {
        super(message);
    }
}
