package model;

/**
 * A single book in the library, identified by a unique ID.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public class Book {

    private final String id;
    private String title;
    private String author;
    private BookStatus status;

    /**
     * Creates a book that is initially available.
     *
     * @param id     unique identifier of the book
     * @param title  title of the book
     * @param author author of the book
     */
    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.status = BookStatus.AVAILABLE;
    }

    /**
     * Returns the unique ID of this book.
     *
     * @return the book ID
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the title of this book.
     *
     * @return the book title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Changes the title of this book.
     *
     * @param title the new title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the author of this book.
     *
     * @return the book author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Changes the author of this book.
     *
     * @param author the new author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the current loan status of this book.
     *
     * @return the current status
     */
    public BookStatus getStatus() {
        return status;
    }

    /**
     * Changes the loan status of this book.
     *
     * @param status the new status, one of AVAILABLE, ISSUED or RESERVED
     */
    public void setStatus(BookStatus status) {
        this.status = status;
    }
}
