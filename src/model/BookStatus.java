package model;

/**
 * The three loan states a library book can have.
 *
 * @author Syeda Momina Sherazi (534663), Muqaddus Habib (534742)
 */
public enum BookStatus {

    /** The book is on the shelf and available to borrow. */
    AVAILABLE,

    /** The book has been issued to a reader. */
    ISSUED,

    /** The book has been reserved by a reader. */
    RESERVED
}
