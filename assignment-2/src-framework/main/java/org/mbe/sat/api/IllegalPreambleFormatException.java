package org.mbe.sat.api;

/**
 * Thrown to indicate that a preamble is not formatted correctly.
 */
public class IllegalPreambleFormatException extends IllegalArgumentException {

    /**
     * Constructs an <code>IllegalProblemFormatException</code> with the specified detail message.
     *
     * @param s the detail message
     */
    public IllegalPreambleFormatException(String s) {
        super(s);
    }

    /**
     * Constructs a new <code>IllegalProblemFormatException</code> with the specified detail message and cause.
     *
     * <p>Note that the detail message associated with <code>cause</code> is <i>not</i> automatically incorporated in
     * this exception's detail message.
     *
     * @param message the detail message (which is saved for later retrieval by the {@link Throwable#getMessage()}
     *                method).
     * @param cause   the cause (which is saved for later retrieval by the {@link Throwable#getCause()} method).  (A
     *                <tt>null</tt> value is permitted, and indicates that the cause is nonexistent or unknown.)
     */
    public IllegalPreambleFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
