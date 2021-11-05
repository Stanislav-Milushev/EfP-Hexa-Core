package org.mbe.sat.api.parser;

import org.mbe.sat.api.IllegalCommentFormatException;

/**
 * Used to parse comment definitions formatted in the DIMACS format. Usually comments should not be parsed, but they can
 * contain additional data.
 *
 * @param <T> the type of the returned comment
 */
public interface IDimacsCommentParser<T> {

    /**
     * Parses the given DIMACS comment and returns the result.
     *
     * @param dimacsCommentDefinition the DIMACS comment that is parsed
     * @return the parsed comment
     *
     * @throws IllegalCommentFormatException if the comment is not formatted correctly according to the DIMACS format
     */
    T parse(String dimacsCommentDefinition);
}
