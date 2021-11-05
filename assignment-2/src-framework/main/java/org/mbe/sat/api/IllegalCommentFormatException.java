package org.mbe.sat.api;

/**
 * Thrown to indicate that a comment is not formatted correctly.
 */
public class IllegalCommentFormatException extends IllegalArgumentException {

    /**
     * Constructs an <code>IllegalClauseFormatException</code> with the specified detail message.
     *
     * @param s the detail message
     */
    public IllegalCommentFormatException(String s) {
        super(s);
    }
}
