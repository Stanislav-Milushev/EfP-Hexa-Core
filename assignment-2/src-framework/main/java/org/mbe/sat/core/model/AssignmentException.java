package org.mbe.sat.core.model;

/**
 * Exception that indicates that an error occurred that is related to an {@link Assignment}.
 */
public class AssignmentException extends RuntimeException {

    /**
     * Constructs a new assignment exception with the specified detail message. The cause is not initialized, and may
     * subsequently be initialized by a call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for later retrieval by the {@link #getMessage()}
     *                method.
     */
    public AssignmentException(String message) {
        super(message);
    }
}
