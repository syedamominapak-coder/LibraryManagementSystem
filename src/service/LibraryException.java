package service;

/**
 * Checked exception raised for validation errors and missing books.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public class LibraryException extends Exception {

    /**
     * Creates an exception with a user-friendly message.
     *
     * @param message the error message shown to the user
     */
    public LibraryException(String message) {
        super(message);
    }
}
